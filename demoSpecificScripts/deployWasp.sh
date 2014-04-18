#!/bin/bash
# A S McLellan October 2011
# Script to checkout / update WASP Swarm project from svn then package and deploy with Maven
PROJECT_HOME=/home/wasp/project/wasp
PLUGINS_HOME=/home/wasp/project/wasp-plugins
CATALINA_HOME=/usr/local/tomcat/tomcat/current
MAVEN_HOME=/opt/maven/current
JAVA_HOME=/usr/java/latest
PATH=${JAVA_HOME}/bin:${ANT_HOME}/bin:${MAVEN_HOME}/bin:${PATH}
echo "cleaning up old plugins"
rm -rf $CATALINA_HOME/waspPlugins/*

if [ ! -f $PROJECT_HOME/pom.xml ]
then
echo "Error: $PROJECT_HOME/pom.xml does not exist. Going to exit without attempting build and deploy"
exit 1
fi

echo "Attempting to clean, package and deploy WASP Swarm"
cd $PROJECT_HOME
mvn -Dcatalina.home=$CATALINA_HOME -DskipTests=true clean install
cd $PLUGINS_HOME/wasp-config
mvn -Dcatalina.home=$CATALINA_HOME -DskipTests=true clean install
echo "Re-deploying current WASP instance"
cd $PROJECT_HOME/wasp-web
mvn tomcat:redeploy
cd $PROJECT_HOME/wasp-file
mvn tomcat:redeploy
