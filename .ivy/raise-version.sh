#!/bin/bash

mvn -f 'pom.test.xml' -B versions:set -DnewVersion=${1} -DprocessAllModules
mvn -f 'pom.test.xml' -B versions:commit -DprocessAllModules
