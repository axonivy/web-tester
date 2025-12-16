#!/bin/bash
set -e

mvn -B versions:set -DnewVersion=${1} -DprocessAllModules
mvn -B versions:commit -DprocessAllModules
sed -i -E "/<parent>/,/<\/parent>/s/<version>[^<]*<\/version>/<version>${1}<\/version>/" pom.test.xml
mvn -B -f pom.test.xml versions:set -DnewVersion=${1} -DprocessAllModules
mvn -B -f pom.test.xml versions:commit -DprocessAllModules
