pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '20'))
  }

  triggers {
    cron '@midnight'
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
              docker.build('maven').inside("--network ${networkName}") {
                withCredentials([string(credentialsId: 'gpg.password.axonivy', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.axonivy', variable: 'GPG_FILE')]) {
                  sh "gpg --batch --import ${env.GPG_FILE}"
                  def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'                  
                  maven cmd: "clean ${phase} " +                   
                    "-Dmaven.test.failure.ignore=true " +
                    "-Dskip.gpg=false " +
                    "-Dgpg.passphrase='${env.GPG_PWD}' " +                    
                    "-Dselenide.remote=http://${seleniumName}:4444/wd/hub "
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
