#!/bin/bash
# A S McLellan October 2011
# Script to checkout / update WASP Swarm project from svn then package and deploy with Maven
PROJECT_HOME=/home/wasp/project/wasp
PLUGINS_HOME=/home/wasp/project/wasp-plugins
if [ ! -f $PROJECT_HOME/pom.xml ]
then
echo "Error: $PROJECT_HOME/pom.xml does not exist. Going to exit without attempting build and deploy"
exit 1
fi

echo "Attempting to clean, package and deploy WASP Swarm"
cd $PROJECT_HOME
mvn -Dcatalina.home=$CATALINA_HOME -DskipTests=true clean install
cd $PLUGINS_HOME/mps-tools
mvn -Dcatalina.home=$CATALINA_HOME -DskipTests=true clean install
cd $PLUGINS_HOME
for x in *
do
cd $x
mvn -Dcatalina.home=$CATALINA_HOME -DskipTests=true clean install
cd ../
done
echo "Re-deploying current WASP instance"
cd $PROJECT_HOME/wasp-web
mvn tomcat:redeploy