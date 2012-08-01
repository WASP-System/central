#!/bin/bash
# A S McLellan June 2012
# Generate documentation for WASP and deploy to website

PROJECT_HOME=/home/wasp/project/wasp

rm -rf /var/www/html/documentation/wasp-core
rm -rf /var/www/html/documentation/wasp-web
rm -rf /var/www/html/documentation/wasp-plugins
rm -rf /var/www/html/documentation/docbkx

mkdir /var/www/html/documentation/wasp-core
mkdir /var/www/html/documentation/wasp-web
mkdir /var/www/html/documentation/wasp-plugins

echo Generating and deploying docbkx...
cd $PROJECT_HOME/wasp-doc
mvn clean install docbkx:generate-html docbkx:generate-pdf
cp -R $PROJECT_HOME/wasp-doc/target/docbkx /var/www/html/documentation
cp -R $PROJECT_HOME/wasp-doc/src/docbkx/content/figures /var/www/html/documentation/docbkx/html

echo Generating and deploying javadoc for wasp-web…
cd $PROJECT_HOME/wasp-web
mvn javadoc:javadoc
cp -R $PROJECT_HOME/wasp-web/target/site/apidocs /var/www/html/documentation/wasp-web

echo Generating and deploying javadoc for wasp-core…
cd $PROJECT_HOME/wasp-core
mvn javadoc:javadoc
cp -R $PROJECT_HOME/wasp-core/target/site/apidocs /var/www/html/documentation/wasp-core

echo Generating and deploying javadoc for wasp-plugins…
cd $PROJECT_HOME/wasp-plugins
for x in `ls . | grep -v chipsom`
do
cd $PROJECT_HOME/wasp-plugins/$x
mvn javadoc:javadoc
if [ -d $PROJECT_HOME/wasp-plugins/$x/target/site/apidocs ]
then
if [ ! -d /var/www/html/documentation/wasp-plugins/$x ]
then
mkdir -p /var/www/html/documentation/wasp-plugins/$x
fi
cp -R $PROJECT_HOME/wasp-plugins/$x/target/site/apidocs /var/www/html/documentation/wasp-plugins/$x
fi
done 
