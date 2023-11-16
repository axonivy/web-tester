#!/bin/bash

mvn -f 'pom.test.xml' --batch-mode versions:set-property versions:commit -Dproperty=project.build.plugin.version -DnewVersion=${2} -DallowSnapshots=true
