#!/bin/bash

# Start or stop wasp-daemon for CentOS
#
# Andrew Mclellan <andrew.mclellan@einstein.yu.edu>

### BEGIN INIT INFO
# Provides:          wasp-daemon
# Required-Start:    $all
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start wasp-daemon at boot time
# Description:       Start the Wasp System daemon.
### END INIT INFO
PIDFILE=/var/run/wasp-daemon.pid
# TODO: set values for USER, DAEMON_HOME and JAVA_HOME below
USER=
DAEMON_HOME=
JAVA_HOME=
NAME=wasp-daemon

# TODO: ensure th path to functions is set correctly
. /etc/init.d/functions
test -f ${DAEMON_JAR} || exit 0

case "$1" in
start)	echo "Starting Wasp System Daemon Process" ${NAME}
	#daemon --pidfile=${PIDFILE} --user=${USER} ${DAEMON_HOME}/${NAME} &
	su $USER -c "${DAEMON_HOME}/${NAME} > /dev/null 2>&1 &"
	if [ $? -ne 0 ]; then
		echo_failure
	else
		while [ -z $PID ]; do
			sleep 1
			PID=$(pgrep -u wasp -f java.+wasp-daemon)
		done
		echo $PID > $PIDFILE
		echo_success
	fi 
	;;
stop)	echo "Stopping Wasp System Daemon Process" ${NAME}
	killproc -p $PIDFILE java -HUP
	if [ $? -eq 0 ]; then
	rm -f $PIDFILE
	echo_success
	else
	echo_failure
	fi
	;;
restart) echo "Restarting Wasp System Daemon Process" ${NAME} 
	start
	stop
	;;
status) status -p $PIDFILE ${NAME}
	;;
*)	echo "Usage: /etc/init.d/${NAME} {start|stop|restart|status}"
	exit 2
	;;
esac
exit 0
