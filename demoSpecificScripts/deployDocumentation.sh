#!/bin/bash
# A S McLellan June 2012
# Generate documentation for WASP and deploy to website

PROJECT_HOME=/home/wasp/project/wasp
PLUGINS_HOME=/home/wasp/project/wasp-plugins

rm -rf /var/www/html/documentation/wasp-*
rm -rf /var/www/html/documentation/docbkx
rm -rf /var/www/html/documentation/wasp-plugins/*

echo Generating and deploying docbkx...
cd $PROJECT_HOME/wasp-doc
mvn clean install docbkx:generate-html docbkx:generate-pdf
cp -R $PROJECT_HOME/wasp-doc/target/docbkx /var/www/html/documentation
cp -R $PROJECT_HOME/wasp-doc/src/docbkx/content/figures /var/www/html/documentation/docbkx/html

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

echo Generating and deploying javadoc for wasp-plugins…
cd $PLUGINS_HOME
for x in *
do
cd $PLUGINS_HOME/$x
mvn javadoc:javadoc
if [[ -d $PLUGINS_HOME/$x/target/site/apidocs ]]
then
if [[ ! -d /var/www/html/documentation/wasp-plugins/$x ]]
then
mkdir -p /var/www/html/documentation/wasp-plugins/$x
fi
cp -R $PLUGINS_HOME/$x/target/site/apidocs /var/www/html/documentation/wasp-plugins/$x
fi
done