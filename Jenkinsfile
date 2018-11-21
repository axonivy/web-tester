pipeline {
  agent {
    dockerfile true
  }
  parameters {
    booleanParam(defaultValue: false, description: 'If checked the plugin does not sign the plugin', name: 'skipGPGSign')
    choice(
      name: 'engineSource',
      description: 'Engine to use for build',
      choices: 'Linux_Trunk_DesignerAndServer\nTrunk_DesignerAndServer\nTrunk_All'
    )
    choice(
      name: 'DEPLOY_PROFILES',
      description: 'Choose where the built plugin should be deployed to',
      choices: 'build\ncentral'
    )
  }
  options {
    buildDiscarder(logRotator(artifactNumToKeepStr: '20'))
  }
  triggers {
    pollSCM 'H/30 * * * *'
    cron '@midnight'
  }
  stages {    
    stage('build') {
      steps {
        withCredentials([string(credentialsId: 'gpg.password.supplements', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.supplements', variable: 'GPG_FILE')]) {
          script {
            script {
              sh "gpg --batch --import ${env.GPG_FILE}"
              maven cmd: 'clean deploy -f primeui-tester/pom.xml --activate-profiles ${DEPLOY_PROFILES} -Dgpg.passphrase='${env.GPG_PWD}' -Dgpg.skip=${params.skipGPGSign}'
            }
            archiveArtifacts '*/target/*.zip, */target/fop.log'
            analyzeFopLogs()
          }
        }
      }
    }
  }
}

def analyzeFopLogs() {
    analyzeFopLog('primeui-tester/target/fop.log')
}

def analyzeFopLog(logFile) {
  println "Analyze ${logFile}"

  def content = readFile(logFile)

  def errors = content.split("\\n").findAll { line -> 
    line.contains('ERROR') && !line.contains('Couldn\'t find hyphenation pattern')
  }

  if (errors.size() > 0) {
    println "==========================================="
    println "FOUND ${errors.size()} ERRORS in ${logFile}"
    println "BUILD WILL BE MAKRED AS UNSTABLE"
    println errors
    println "==========================================="
    currentBuild.result = 'UNSTABLE'
  }
}

