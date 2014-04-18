#!/bin/bash
# A S McLellan June 2012
# Generate documentation for WASP and deploy to website
PROJECT_HOME=/home/wasp/project/wasp
PLUGINS_HOME=/home/wasp/project/wasp-plugins
CATALINA_HOME=/usr/local/tomcat/tomcat/current
MAVEN_HOME=/opt/maven/current
JAVA_HOME=/usr/java/latest
PATH=${JAVA_HOME}/bin:${ANT_HOME}/bin:${MAVEN_HOME}/bin:${PATH}

rm -rf /var/www/html/documentation/wasp-*
rm -rf /var/www/html/documentation/sphinx
rm -rf /var/www/html/documentation/wasp-plugins/*

mkdir /var/www/html/documentation/sphinx
echo Generating Sphinx documentation...
cd $PROJECT_HOME/wasp-doc/src/sphinx/plugin
make html
mkdir /var/www/html/documentation/sphinx/plugin
cp -R $PROJECT_HOME/wasp-doc/src/sphinx/plugin/build/* /var/www/html/documentation/sphinx/plugin
cd $PROJECT_HOME/wasp-doc/src/sphinx/developer
make html
mkdir /var/www/html/documentation/sphinx/developer
cp -R $PROJECT_HOME/wasp-doc/src/sphinx/developer/build/* /var/www/html/documentation/sphinx/developer

cd $PROJECT_HOME
for x in wasp-*
do
cd $x
echo Generating and deploying javadoc for ${x}…
mvn javadoc:javadoc
if [[ ! -d /var/www/html/documentation/$x ]]
then
mkdir /var/www/html/documentation/$x
fi
cp -R $PROJECT_HOME/$x/target/site/apidocs /var/www/html/documentation/$x
cd ../
done

cd $PROJECT_HOME/plugins
echo Generating and deploying javadoc for wasp-plugins…
for x in *
do
if [[ -d ${x} ]]
then
cd $x
echo Generating and deploying javadoc for ${x}…
mvn javadoc:javadoc
if [[ -d target/site/apidocs ]]
then
if [[ ! -d /var/www/html/documentation/wasp-plugins/$x ]]
then
mkdir -p /var/www/html/documentation/wasp-plugins/$x
fi
cp -R target/site/apidocs /var/www/html/documentation/wasp-plugins/$x
fi
cd ../
fi
done
cd $PLUGINS_HOME/wasp-config
mvn javadoc:javadoc
if [[ ! -d /var/www/html/documentation/wasp-plugins/wasp-config ]]
then
mkdir -p /var/www/html/documentation/wasp-plugins/wasp-config
fi
cp -R target/site/apidocs /var/www/html/documentation/wasp-plugins/wasp-config