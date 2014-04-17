#!/bin/bash
WASP_HOME=/home/wasp
PROJECT_HOME=$WASP_HOME/project/wasp
PLUGINS_HOME=$WASP_HOME/project/wasp-plugins
CATALINA_HOME=/usr/local/tomcat/tomcat/current
MAVEN_HOME=/opt/maven/current
JAVA_HOME=/usr/java/latest
PATH=${JAVA_HOME}/bin:${ANT_HOME}/bin:${MAVEN_HOME}/bin:${PATH}
echo Shutting down WASP Batch if running...
for x in `ps -ef | grep edu.yu.einstein.wasp.daemon.StartDaemon | grep -v grep | awk '{print $2}'`
do
kill -9 $x
done

#echo Cleaning downloads folder...
#cd $CATALINA_HOME/downloads
#rm -rf

if [ -d $PROJECT_HOME/.git ]
then
echo "$PROJECT_HOME exists and under svn control. Going to update."
cd $PROJECT_HOME
git pull origin develop
elif [ -d $PROJECT_HOME ]
then
echo "Error: $PROJECT_HOME exists but is not under source control. Not sure what to do. Please clean up and try again"
exit 1
else
echo "$PROJECT_HOME doesn't exist. Going to create and pull demo branch from git."
mkdir -p $PROJECT_HOME
mkdir $PLUGINS_HOME
cd $WASP_HOME/project
git clone http://frankfurt.aecom.yu.edu/git/wasp/blessedRepository.git wasp
cd $PROJECT_HOME
git checkout --track remotes/origin/develop
fi

echo "Updating config plugin..."
if [ -d $PLUGINS_HOME/wasp-config ]
then
cd $PLUGINS_HOME/wasp-config
git pull origin demo
else
cd $PLUGINS_HOME
git clone http://frankfurt.aecom.yu.edu/git/wasp/wasp-config.git wasp-config
cd $PLUGINS_HOME/wasp-config
git checkout --track remotes/origin/demo
fi

echo "Restoring wasp database to demo default..."
mysql -u wasp --password=waspV2 -D wasp < $PROJECT_HOME/db/demo/waspDbAndBatchDemoData.sql
#mysql -u wasp --password=waspV2 -D wasp < $PROJECT_HOME/db/InitializeWaspDb.sql
#mysql -u wasp --password=waspV2 -D wasp < $PROJECT_HOME/db/createSpringBatchTables.sql
#mysql -u wasp --password=waspV2 -D wasp < $PROJECT_HOME/db/minimalDataLoad.sql
echo Updating and re-deploying WASP System...
$WASP_HOME/bin/deployWasp.sh

echo Starting wasp-daemon...
java -Xms128m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=256m -Dcatalina.home=$CATALINA_HOME -cp "$PROJECT_HOME/wasp-daemon/target/wasp-daemon-0.1.0-SNAPSHOT-release/wasp-daemon-0.1.0-SNAPSHOT/wasp-daemon-0.1.0-SNAPSHOT.jar:$CATALINA_HOME/waspPlugins/*" edu.yu.einstein.wasp.daemon.StartDaemon > /dev/null 2>&1 &

echo Re-building and deploying documentation..
$WASP_HOME/bin/deployDocumentation.sh

echo done.