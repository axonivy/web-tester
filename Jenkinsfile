pipeline {
  agent { label 'fast' }

  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '20'))
  }

  triggers {
    cron '@midnight'
  }

  parameters {
    string(name: 'engineSource', 
      defaultValue: 'https://product.ivyteam.io', 
      description: 'Engine page url')
    string(name: 'sprintQualifier',
      description: "Optional sprint qualifier (e.g. m7 for Sprint 7). Empty keeps the original project version.",
      defaultValue: '')
  }

  stages {
    stage('build') {
      steps {
        script {
          def random = (new Random()).nextInt(10000000)
          def networkName = "build-" + random
          def seleniumName = "selenium-" + random
          def showcaseName = "showcase-" + random
          sh "docker network create ${networkName}"
          try {
            docker.image("ghcr.io/primefaces/primefaces-showcase:14.X-latest").withRun("--name ${showcaseName} --network ${networkName}") {
              docker.image("selenium/standalone-firefox:4").withRun("-e START_XVFB=false --shm-size=2g --name ${seleniumName} --network ${networkName}") {
                docker.build('maven').inside("--network ${networkName}") {
                  withCredentials([string(credentialsId: 'gpg.password.axonivy', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.axonivy', variable: 'GPG_FILE')]) {
                    sh "gpg --batch --import ${env.GPG_FILE}"
                    def args = "" +
                      "-Dmaven.test.failure.ignore=true " +
                      "-Dengine.page.url=${params.engineSource} " +
                      "-Divy.engine.version.latest.minor=true " +
                      "-Dskip.gpg=false " +
                      "-Dgpg.passphraseEnvName=GPG_PWD " +
                      "-Dselenide.remote=http://${seleniumName}:4444/wd/hub " + 
                      "-Dshowcase.url=http://${showcaseName}:8080/ "
                    def qualifier = params.sprintQualifier?.trim()
                    if (qualifier) {
                      applyVersionQualifier(qualifier)
                    } else {
                      args += "-f pom.test.xml "
                    }
                    def phase = isReleasingBranch() ? 'deploy' : 'verify'
                    maven cmd: "clean ${phase} ${args}"
                  }
                }
              }
            }
          } finally {
            sh "docker network rm ${networkName}"
          }
        }
        archiveArtifacts '**/target/*.jar'
        archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
        junit testDataPublishers: [[$class: 'StabilityTestDataPublisher']], testResults: '**/target/*-reports/**/*.xml'
        recordIssues tools: [mavenConsole(), eclipse(), javaDoc()], qualityGates: [[threshold: 1, type: 'TOTAL']], filters: [
          excludeMessage('.*JAR will be empty.*'), // for unit tester          
        ]
      }
    }
  }
}

def applyVersionQualifier(String qualifier) {
  def currentVersion = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
  def qualified  = currentVersion.replaceFirst(/-SNAPSHOT$/, "-${qualifier}")
  echo "Using version '${qualified}' for this build."
  maven cmd: "versions:set -DnewVersion=${qualified} -DprocessAllModules=true -DgenerateBackupPoms=false"
}
