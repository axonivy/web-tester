pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '20'))
  }

  triggers {
    cron '@midnight'
  }

  parameters {
    choice(name: 'deployProfile',
      description: 'Choose where the built plugin should be deployed to',
      choices: ['sonatype.snapshots', 'maven.central.release'])

    string(name: 'revision',
      description: 'Revision for this release, e.g. newest version "8.0.0" revison should be "1" (-> 8.0.1). Note: This is only used for release target!',
      defaultValue: '0' )
  }

  stages {
    stage('build') {
      steps {
        script {
          def random = (new Random()).nextInt(10000000)
          def networkName = "build-" + random
          def seleniumName = "selenium-" + random
          sh "docker network create ${networkName}"
          try {
            docker.image("selenium/standalone-firefox:4").withRun("-e START_XVFB=false --shm-size=2g --name ${seleniumName} --network ${networkName}") {
              docker.build('maven-build').inside("--network ${networkName}") {
                withCredentials([string(credentialsId: 'gpg.password.axonivy', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.axonivy', variable: 'GPG_FILE')]) {
                  sh "gpg --batch --import ${env.GPG_FILE}"
                  def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
                  def mavenProps = ""
                  if (params.deployProfile == 'maven.central.release') {
                    mavenProps = "-Drevision=${params.revision}" 
                  }
                  maven cmd: "clean ${phase} " +
                    "-P ${params.deployProfile} " +
                    "-Dmaven.test.failure.ignore=true " +
                    "-Dgpg.passphrase='${env.GPG_PWD}' " +                    
                    "-Dselenide.remote=http://${seleniumName}:4444/wd/hub " +
                    "${mavenProps}"
                }                
              }
            }
          } finally {
            sh "docker network rm ${networkName}"
          }
        }
        archiveArtifacts '**/target/*.jar'
        junit '**/target/surefire-reports/**/*.xml'
        recordIssues tools: [mavenConsole(), eclipse(), javaDoc()], unstableTotalAll: 1, filters: [
          excludeMessage('.*JAR will be empty.*'), // for unit tester          
        ]
      }
    }
  }
}
