#!/bin/bash

# called by the debian preseed at the last phase of installation,
# this script installs wasp and configures dependencies.  Any steps
# that require a running system should be performed in install.sh.

setup_tomcat(){
  cat > /var/lib/tomcat7/conf/tomcat-users.xml << EOF
<?xml version='1.0' encoding='utf-8'?>
  <!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  -->
<tomcat-users>
  <role rolename="manager-gui"/>
  <user username="tomcat" password="waspsystem" roles="manager-gui"/>
  <role rolename="manager-script"/>
  <user username="tomcat-script" password="waspsystem" roles="manager-script"/>
</tomcat-users>
EOF
  
  chown root:tomcat7 /var/lib/tomcat7/conf/tomcat-users.xml
  usermod -G tomcat7 -a wasp
  mkdir -p ${CATALINA_HOME}/waspPlugins
  chown tomcat7:tomcat7 ${CATALINA_HOME}/waspPlugins
  chmod 775 ${CATALINA_HOME}/waspPlugins/
  chown tomcat7:tomcat7 /usr/share/tomcat7/lib
  chmod 775 /usr/share/tomcat7/lib
  chown tomcat7:tomcat7 /var/log/tomcat7
  chmod 775 /var/log/tomcat7
  mkdir /var/log/wasp-daemon
  chown wasp:tomcat7 /var/log/wasp-daemon
  chmod 775 /var/log/wasp-daemon

  #logging
  rm /etc/tomcat7/logging.properties
  cat > ${CATALINA_HOME}/lib/logback.xml << "EOF"
<configuration debug="false">
 <contextListener class="ch.qos.logback.classic.jul.LevelChangePropagator"/>
 <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
  <encoder>
   <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
  </encoder>
 </appender>
 <appender name="LOGFILE" class="ch.qos.logback.core.FileAppender">
  <file>${catalina.base}/logs/tomcat.log</file>
  <encoder>
   <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
  </encoder>
 </appender>

 <root level="INFO">
  <appender-ref ref="STDOUT" />
  <appender-ref ref="LOGFILE" />
 </root>
</configuration>
EOF
  chown -R tomcat7:tomcat7 ${CATALINA_HOME}/lib/logback.xml 
}

setup_daemon(){
  cat > /etc/init.d/wasp-daemon << "EOF"
#!/bin/sh
# /etc/init.d/wasp-daemon
# based on tomcat7 init.d
### BEGIN INIT INFO
# Provides:          wasp-daemon
# Required-Start:    $all
# Required-Stop:     $remote_fs $syslog $newtork
# Should-Start:      $portmap
# Should-Stop:       $portmap
# X-Start-Before:    nis
# X-Stop-After:      nis
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# X-Interactive:     false
# Short-Description: WASP System Daemon
# Description:       System Daemon provides messaging and 
#                    batch flow control for the WASP System.
### END INIT INFO

set -e

PATH=/bin:/usr/bin:/sbin:/usr/sbin
NAME=wasp-daemon
DESC="WASP System Daemon"

if [ `id -u` -ne 0 ]; then
  echo "You need root privileges to run this script"
  exit 1
fi

. /lib/lsb/init-functions

if [ -r /etc/default/rcS ]; then
  . /etc/default/rcS
fi

USER=wasp
GROUP=tomcat7
JAVA_HOME=$(readlink -f /usr/bin/javac | sed "s:/bin/javac::")
CATALINA_HOME=/usr/share/tomcat7/

if [ -z "$JAVA_OPTS" ]; then
  JAVA_OPTS="-Djava.awt.headless=true -Xms128m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=256m -Dcatalina.home=$CATALINA_HOME"
fi

WASP_PID="/var/run/wasp-daemon.pid"

wasp_start(){
  wasp=/home/wasp/wasp/wasp-daemon/target/*[!s].jar
  start-stop-daemon --start -c $USER -g $GROUP -m --background --pidfile "$WASP_PID" --user $USER --exec "$JAVA_HOME/bin/java" --  $JAVA_OPTS -cp "`readlink -f $wasp`:$CATALINA_HOME/waspPlugins/*" edu.yu.einstein.wasp.daemon.StartDaemon
}

case "$1" in
  start)
    log_daemon_msg "Starting $DESC" "$NAME"
    if start-stop-daemon --test --start --pidfile "$WASP_PID" --user $USER --exec "$JAVA_HOME/bin/java" >/dev/null; then
      wasp_start 
      sleep 5
      if start-stop-daemon --test --start --pidfile "$WASP_PID" --user $USER --exec "$JAVA_HOME/bin/java" >/dev/null; then
	if [ -f "$WASP_PID" ]; then
	  rm -f "$WASP_PID"
	fi
	log_end_msg 1
      else
	log_end_msg 0
      fi
    else
      log_progress_msg "previously started"
      log_end_msg 0
    fi
  ;;
  stop)
    log_daemon_msg "Stopping $DESC" "$NAME"
    set +e
    if [ -f "$WASP_PID" ]; then
      start-stop-daemon --stop --pidfile "$WASP_PID" --user "$USER" --retry=TERM/20/KILL/5 >/dev/null
      if [ $? -eq 1 ]; then
        log_progress_msg "$DESC is not running but pid file exists"
      elif [ $? -eq 3 ]; then
        PID="`cat $WASP_PID`"
        log_failure_msg "Failed to stop $NAME (pid $PID)"
        exit 1
      fi
      rm -f "$WASP_PID"
      rm -rf "$JVM_TMP"
    else
      log_progress_msg "not running"
    fi
    log_end_msg 0
    set -e
  ;;
  
  status)
    set +e
    start-stop-daemon --test --start --pidfile "$WASP_PID" --user $USER --exec "$JAVA_HOME/bin/java" >/dev/null 2>&1
    if [ "$?" = "0" ]; then
      if [ -f "$WASP_PID" ]; then
        log_success_msg "$DESC is not running, but pid file exists."
        exit 1
      else
        log_success_msg "$DESC is not running."
        exit 3
      fi
    else
      log_success_msg "$DESC is running with pid `cat $WASP_PID`"
    fi
    set -e
  ;;
 
  restart|force-reload)
    if [ -f "$WASP_PID" ]; then
      $0 stop
      sleep 1
    fi
    $0 start
  ;;

  try-restart)
    if start-stop-daemon --test --start --pidfile "$WASP_PID" --user $USER --exec "$JAVA_HOME/bin/java" >/dev/null; then
      $0 start
    fi
  ;;

  *)
    log_success_msg "Usage: $0 {start|stop|restart|try-restart|force-reload|status}"
    exit 1
  ;;
esac
exit 0

EOF

}

build_wasp(){

  # These commands will be run as wasp user

  mkdir /home/wasp/.ssh
  chmod 700 /home/wasp/.ssh
  ssh-keygen -t rsa -N '' -f /home/wasp/.ssh/id_rsa

  mkdir -p /home/wasp/illumina
  mkdir -p /home/wasp/illumina-fastq
  mkdir -p /home/wasp/scratch
  mkdir -p /home/wasp/draft
  mkdir -p /home/wasp/results
  mkdir -p /home/wasp/remote
  mkdir -p /home/wasp/modules
  mkdir -p /home/wasp/metadata

  echo "export MODULEPATH=/home/wasp/modules:$MODULEPATH" > /home/wasp/.wasprc

  echo "
export CATALINA_HOME=$CATALINA_HOME" >> /home/wasp/.profile

  #will need to automate this to get specific tags (most recent? git describe --abbrev=0 --tags )
  
  WASP_MAIN=brent
  WASP_HOST=http://frankfurt.aecom.yu.edu/git/wasp/
  #WASP_HOST=https://github.com/WASP-System/

  WASP=( "${WASP_MAIN}|master" 'wasp-config|image' 'wasp-illumina|master' 'wasp-fastq|master' 'mps-tools|master' \
	'wasp-genericDnaSeq|master' 'wasp-chipseq|master' 'wasp-bisulfite|master' \
	'wasp-helptag|master' )

  mkdir -p /home/wasp/src
  cd /home/wasp/src

  for W in "${WASP[@]}"; do 
    w=(${W//|/ })	
    git clone ${WASP_HOST}${w[0]}.git
    cd ${w[0]}
    if [ "${w[1]}" != "master" ]; then
    	git checkout ${w[1]}
    fi
    #switch tag
    PROFILE=""
    if [ "${w[0]}" == "wasp-config" || "${w[0]}" == "wasp-web" ]; then
      PROFILE="-Pdeploy"
    fi
    mvn clean install ${PROFILE} -Dcatalina.home=$CATALINA_HOME -DskipTests=true
    cd ..
  done

  cat > /home/wasp/.m2/settings.xml << EOF
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
    http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository/>
  <interactiveMode/>
  <usePluginRegistry/>
  <offline/>
  <pluginGroups/>
  <servers>
    <server>
      <id>tomcat-localhost</id>
      <username>tomcat-script</username>
      <password>waspsystem</password>
    </server>
  </servers>
  <mirrors/>
  <proxies/>
  <profiles/>
  <activeProfiles/>
</settings>
EOF
  
  cd ~
  ln -s src/$WASP_MAIN/ wasp
  cat .ssh/id_rsa.pub > .ssh/authorized_keys
  ssh localhost -i .ssh/id_rsa -oStrictHostKeyChecking=no /bin/true

  mkdir /home/wasp/bin
  cd /home/wasp/bin && ln -s /home/wasp/wasp/wasp-cli/target/wasp .
  echo 'export PATH=/home/wasp/bin:$PATH' >> /home/wasp/.profile

}

on_first_boot() {
	cd /root
	wget http://waspsystem.org/install.sh
	sed -i 's:exit 0:bash /root/install.sh 2>\&1 | tee /home/wasp/install.log:' /etc/rc.local
}

export CATALINA_HOME=/usr/share/tomcat7/
echo '
export JAVA_HOME=$(readlink -f /usr/bin/javac | sed "s:/bin/javac::")' >> /etc/profile

setup_tomcat

export -f build_wasp 

su wasp -c "bash -c build_wasp"

setup_daemon

chmod +x /etc/init.d/wasp-daemon

on_first_boot

