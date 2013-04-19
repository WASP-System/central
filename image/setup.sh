#!/bin/bash

# the first time that the root user logs in, Oracle java should be installed.
echo "

#Automate java install
ORACLE_JAVA=`dpkg --get-selections | grep -c oracle`
if [ \"$ORACLE_JAVA\" -lt 1 ]; then
	apt-get update
	apt-get --yes install oracle-java7-installer oracle-java7-set-default
fi
" >> /root/.profile



