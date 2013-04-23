#!/bin/bash


build_wasp(){

  # These commands will be run as wasp user

  echo "
export CATALINA_HOME=$CATALINA_HOME" >> /home/wasp/.profile

  /usr/bin/mysqladmin -u root password 'wasp'

  #will need to automate this to get specific tags (most recent? git describe --abbrev=0 --tags )

  WASP=( 'central|master' 'wasp-config|image' 'wasp-illumina|master' 'mps-tools|master' \
	'wasp-genericDnaSeq|master' 'wasp-chipseq|master' 'wasp-bisulfite|master' \
	'wasp-helptag|master' )

  mkdir -p /home/wasp/src
  cd /home/wasp/src

  for W in "${WASP[@]}"; do 
    w=(${W//|/ })	
    git clone https://github.com/WASP-System/${w[0]}.git
    cd ${w[0]}
    #if [ "${w[1]}" != "master" ]; then
    #	git checkout ${w[1]}
    #fi
    #switch tag
    mvn clean install -Dcatalina.home=$CATALINA_HOME -DskipTests=true
    cd ..
  done

  cd central/wasp-web
  mvn tomcat:redeploy

}

export CATALINA_HOME=/var/lib/tomcat7/
echo '
export JAVA_HOME=$(readlink -f /usr/bin/javac | sed "s:/bin/javac::")' >> /etc/profile
usermod -G tomcat7 -a wasp
mkdir -p ${CATALINA_HOME}/waspPlugins
chown tomcat7:tomcat7 ${CATALINA_HOME}/waspPlugins
chmod 775 ${CATALINA_HOME}/waspPlugins/
chown tomcat7:tomcat7 /usr/share/tomcat7/lib
chmod 775 /usr/share/tomcat7/lib
ln -s /usr/share/tomcat7/lib ${CATALINA_HOME}/lib

export -f build_wasp 
su wasp -c "bash -c build_wasp"


