#!/bin/bash

CATALINA_HOME=/path/to/cat
CATALINA_OPTS=-Xms256m -Xmx1024m -XX:MaxPermSize=256m

echo "

CATALINA_HOME=$CATALINA_HOME
CATALINA_OPTS=$CATALINA_OPTS" >> /home/wasp/.bash_profile

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
	git checkout ${w[1]}
	#switch tag
	mvn clean install -Dcatalina.home=$CATALINA_HOME -DskipTests=true
	cd ..
done



