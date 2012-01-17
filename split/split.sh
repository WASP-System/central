#!/bin/bash

# migrate project to a maven module/reactor project.

# modules:
#  - wasp-core
#    majority of classes, this will be broken up later into model and logic
#
#  - wasp-web
#    edu.yu.einstein.wasp.controller.*
#    edu.yu.einstein.wasp.load.*
#
#  - wasp-site
#    maven site
#

for x in main test; do
  for y in java resources; do
    for z in wasp-core wasp-web; do
      mkdir -p ${z}/src/${x}/${y}
    done
  done
done

# wasp-site
mkdir -p wasp-site/src
svn add --parents wasp-site/src
svn mv --parents src/docbkx wasp-site/src
svn mv --parents src/site wasp-site/src


# wasp-core
#
# lets get this out of the way.  Why are these in here?

svn mv --parents src/main/java/org wasp-core/src/main/java
svn mv --parents src/main/java/util wasp-core/src/main/java

# move web classes and files
svn mv --parents src/main/java/edu/yu/einstein/wasp/controller wasp-web/src/main/java/edu/yu/einstein/wasp
svn mv --parents src/main/java/edu/yu/einstein/wasp/load wasp-web/src/main/java/edu/yu/einstein/wasp
svn mv --parents src/main/java/edu/yu/einstein/wasp/taglib wasp-web/src/main/java/edu/yu/einstein/wasp

svn mv --parents src/main/webapp wasp-web/src/main/webapp
svn mv --parents src/test/config wasp-web/src/test/

# move WAPS core classes
svn mv --parents src/main/java/edu/yu/einstein/wasp/* wasp-core/src/main/java/edu/yu/einstein/wasp

svn mv --parents src/main/resources/* wasp-core/src/main/resources
svn mv --parents src/test/resources/* wasp-core/src/test/resources

# poms
cp split/pom-root.xml pom.xml
cp split/pom-site.xml wasp-site/pom.xml
cp split/pom-core.xml wasp-core/pom.xml
cp split/pom-web.xml wasp-web/pom.xml
