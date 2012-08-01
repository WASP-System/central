#!/bin/bash
# A S McLellan October 2011
# Script to checkout / update WASP Swarm project from svn then package and deploy with Maven 
PROJECT_HOME=/home/wasp/project/wasp
if [ ! -f $PROJECT_HOME/pom.xml ]
then
echo "Error: $PROJECT_HOME/pom.xml does not exist. Going to exit without attempting build and deploy"
exit 1
fi

echo "Attempting to clean, package and deploy WASP Swarm"
cd $PROJECT_HOME
mvn clean install
for x in `ls -d $PROJECT_HOME/wasp-plugins/* | grep -v chipsom`
do
cd $x
mvn -Dcatalina.home=$CATALINA_HOME clean install
done 
echo Undeploying current WASP instance
cd $PROJECT_HOME/wasp-web
mvn tomcat:stop
#mvn tomcat:undeploy
cd $PROJECT_HOME/wasp-exec
mvn -Dcatalina.home=$CATALINA_HOME clean install

cd $PROJECT_HOME/wasp-web
mvn tomcat:redeploy