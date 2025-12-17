#!/bin/bash
set -e

echo "Raising version and all submodules of pom.xml to ${1}"
mvn -B versions:set versions:commit -DnewVersion=${1} -DprocessAllModules

echo "Fix raise other versions in missed modules to ${1}"
sed -i -E "/<parent>/,/<\/parent>/s/<version>[^<]*<\/version>/<version>${1}<\/version>/" pom.test.xml
mvn -B -f pom.test.xml versions:set versions:commit -DnewVersion=${1}
mvn -B -f web-tester-fixture/pom.xml versions:set versions:commit -DnewVersion=${1}
