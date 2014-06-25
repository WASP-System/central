#!/bin/sh -e

# Start or stop wasp-daemon on CentOS
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

NAME=wasp-daemon
PIDFILE=/var/run/wasp-daemon.pid
# TODO: fill in values for USER, GROUP, DAEMON_HOME and JAVA_HOME below
USER=
GROUP=
DAEMON_HOME=
JAVA_HOME=

#TODO: ensure the path to init-functions is set correctly
. /lib/lsb/init-functions
test -f ${DAEMON_JAR} || exit 0

case "$1" in
	start)	log_daemon_msg "Starting Wasp System Daemon Process" ${NAME}
	start-stop-daemon --start --quiet --pidfile $PIDFILE --chuid ${USER}:${GROUP} --user ${USER} --name ${NAME} --chdir ${DAEMON_HOME} --startas ${NAME} -m --background -- 
	log_end_msg $?
	;;
stop)	log_daemon_msg "Stopping Wasp System Daemon Process" ${NAME}
	start-stop-daemon --stop --quiet --pidfile $PIDFILE --name ${NAME} 
	log_end_msg $?
	;;
restart) log_daemon_msg "Restarting Wasp System Daemon Process" ${NAME} 
	start-stop-daemon --stop --retry 5 --quiet --pidfile $PIDFILE --name ${NAME}
	start-stop-daemon --start --quiet --pidfile $PIDFILE --chuid ${USER}:${GROUP} --user ${USER} --name ${NAME} --chdir ${DAEMON_HOME} --startas ${NAME} -m --background
	log_end_msg $?
	;;
reload|force-reload) log_daemon_msg "Reloading configuration files for Wasp System Daemon Process" ${NAME}
	log_end_msg 0
	;;
*)	log_action_msg "Usage: /etc/init.d/${NAME} {start|stop|restart|reload|force-reload}"
	exit 2
	;;
esac
exit 0
