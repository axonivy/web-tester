pipeline {
  agent {
    docker {
      image 'maven:3.5.2-jdk-8'
    }
  }
  parameters {
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
        script {
    	  script { maven cmd: 'clean deploy --activate-profiles ${DEPLOY_PROFILES}' }
          archiveArtifacts '*/target/*.zip, */target/fop.log'
          analyzeFopLogs()
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

