#!/bin/bash

# setupWasp.sh
# asmclellan 2013

# user provided variables
#WASP_HOME=/path/to/waspSystem/base/folder
#CATALINA_HOME=/path/to/tomcat

# script variables
PROJECT_HOME=$WASP_HOME/wasp
PLUGINS_HOME=$WASP_HOME/wasp-plugins

## uncomment the following lines to stop the wasp-daemon (if you've manually started it or uncommented the lines to start it in this script and
## run it before)

#echo "Shutting down WASP Daemon if running..."
#for x in `ps -ef | grep edu.yu.einstein.wasp.daemon.StartDaemon | grep -v grep | awk '{print $2}'`
#do
#kill -9 $x
#done

if [ -d $PROJECT_HOME/.git ]
then
echo "$PROJECT_HOME exists and under svn control. Going to update."
cd $PROJECT_HOME
git pull origin master
elif [ -d $PROJECT_HOME ]
then
echo "Error: $PROJECT_HOME exists but is not under source control. Not sure what to do. Please clean up and try again"
exit 1
else
echo "$PROJECT_HOME doesn't exist. Going to create and pull demo branch from github."
cd $WASP_HOME
git clone git://github.com/WASP-System/central.git wasp
fi

# check initial plugins exist
if [ ! -d $PLUGINS_HOME ]
then
mkdir -p $PLUGINS_HOME
fi
cd $PLUGINS_HOME
if [ ! -d wasp-config ]
then
git clone git://github.com/WASP-System/wasp-config.git wasp-config
fi
if [ ! -d wasp-illumina ]
then
git clone git://github.com/WASP-System/wasp-illumina.git wasp-illumina
fi
if [ ! -d wasp-chipseq ]
then
git clone git://github.com/WASP-System/wasp-chipseq.git wasp-chipseq
fi
if [ ! -d wasp-genericDnaSeq ]
then
git clone git://github.com/WASP-System/wasp-genericDnaSeq.git wasp-genericDnaSeq
fi

echo "Updating plugins..."
for x in wasp-*
do
echo "    Currently updating $x..."
cd $x
git pull origin master
cd ../
done

echo "Updating and re-deploying Wasp System..."
echo "Attempting to clean, package and deploy WASP Swarm"
cd $PROJECT_HOME
mvn -Dcatalina.home=$CATALINA_HOME -DskipTests=false clean install
cd $PLUGINS_HOME
for x in *
do
cd $x
mvn -Dcatalina.home=$CATALINA_HOME -DskipTests=false clean install
cd ../
done
echo "Re-deploying current WASP instance"
cd $PROJECT_HOME/wasp-web
mvn tomcat:redeploy

## uncomment the following lines to start the wasp-daemon (required to activate job submission and analysis workflows flows)

#echo Starting wasp-daemon...
#cd $PROJECT_HOME/wasp-exec
#rm nohup.out
#nohup java -Xms128m -Xmx256m -XX:PermSize=128m -XX:MaxPermSize=256m -Dcatalina.home=$CATALINA_HOME -cp "target/wasp-exec-0.1.0-SNAPSHOT.jar:$CATALINA_HOME/waspPlugins/*" edu.yu.einstein.wasp.daemon.StartDaemon &


echo "done."