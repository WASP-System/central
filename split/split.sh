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
      svn add --parents ${z}/src/${x}/${y}
    done
  done
done

mkdir wasp-core/src/site
mkdir wasp-web/src/site

# wasp-site
#mkdir -p wasp-site/src
#svn add --parents wasp-site/src
#find src/docbkx -type f -exec svn mv --parents {} wasp-core/{} \;
#find src/site -type f -exec svn mv --parents {} wasp-core/{} \;

# wasp-core
#
# lets get this out of the way.  Why are these in here?

find src/main/java/org -type f -exec svn mv --parents {} wasp-core/{} \;
find src/main/java/util -type f -exec svn mv --parents {} wasp-core/{} \;
find src/main/java/test -type f -exec svn mv --parents {} wasp-core/{} \;

# move web classes and files
find src/main/java/edu/yu/einstein/wasp/controller -type f -exec svn mv --parents {} wasp-web/{} \;
find src/main/java/edu/yu/einstein/wasp/load -type f -exec svn mv --parents {} wasp-web/{} \;
find src/main/java/edu/yu/einstein/wasp/taglib -type f -exec svn mv --parents {} wasp-web/{} \;

find src/main/webapp -type f -exec svn mv --parents {} wasp-web/{} \;
find src/test/config -type f -exec svn mv --parents {} wasp-web/{} \;

# move WAPS core classes
find src/main/java/edu/yu/einstein/wasp/ -type f -exec svn mv --parents {} wasp-core/{} \;

find src/test/java/ -type f -exec svn mv --parents {} wasp-core/{} \;

find src/main/resources -type f -exec svn mv --parents {} wasp-core/{} \;
find src/test/resources -type f -exec svn mv --parents {} wasp-core/{} \;

# poms
cp split/pom-root.xml pom.xml
#cp split/pom-site.xml wasp-site/pom.xml
cp split/pom-core.xml wasp-core/pom.xml
cp split/pom-web.xml wasp-web/pom.xml
