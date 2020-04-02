pipeline {
  agent {
    dockerfile true
  }

  options {
    buildDiscarder(logRotator(artifactNumToKeepStr: '20'))
  }

  triggers {
    pollSCM '@hourly'
    cron '@midnight'
  }

  parameters {
    choice(name: 'deployProfile',
      description: 'Choose where the built plugin should be deployed to',
      choices: ['sonatype.snapshots', 'maven.central.release'])

    choice(name: 'deployArtifact',
      description: "Choose which project should be released (onyl if you choose 'maven.central.release' as deployProfile)",
      choices: ['web-tester', 'primeui-tester'])

    string(name: 'nextDevVersion',
      description: "Next development version used after release, e.g. '7.3.0' (no '-SNAPSHOT').\nNote: This is only used for release target; if not set next patch version will be raised by one",
      defaultValue: '' )
  }

  stages {
    stage('snapshot build') {
      when {
        expression { params.deployProfile != 'maven.central.release' }
      }
      steps {
        script {
          withCredentials([string(credentialsId: 'gpg.password.supplements', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.supplements', variable: 'GPG_FILE')]) {

            def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
            maven cmd: "clean ${phase} " +
              "-P ${params.deployProfile} " +
              "-Dgpg.passphrase='${env.GPG_PWD}' " +
              "-Dgpg.skip=false " +
              "-Divy.engine.list.url=${params.engineListUrl} " +
              "-Dmaven.test.failure.ignore=true"

          }
        }
        archiveArtifacts '**/target/*.jar'
        junit '**/target/surefire-reports/**/*.xml'
      }
    }

    stage('release build') {
      when {
        //branch 'master'
        expression { params.deployProfile == 'maven.central.release' }
      }
      steps {

        script {
          def nextDevVersionParam = createNextDevVersionJVMParam()
          sh "git config --global user.name 'ivy-team'"
          sh "git config --global user.email 'nobody@axonivy.com'"
          
          withCredentials([string(credentialsId: 'gpg.password.supplements', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.supplements', variable: 'GPG_FILE')]) {

            withEnv(['GIT_SSH_COMMAND=ssh -o StrictHostKeyChecking=no']) {
              sshagent(credentials: ['github-axonivy']) {
                dir("${params.deployArtifact}"){
                  maven cmd: "clean verify -DdryRun=true release:prepare release:perform " +
                    "-P ${params.deployProfile} " +
                    "${nextDevVersionParam} " +
                    "-Dgpg.passphrase='${env.GPG_PWD}' " +
                    "-Dgpg.skip=false " +
                    "-DignoreSnapshots=true " +
                    "-Darguments=\\"-Dmaven.compiler.source=11 -Dmaven.compiler.target=11\\""
                }
              }
            }
          }
        }
        archiveArtifacts '**/target/*.jar'
        junit '**/target/surefire-reports/**/*.xml'
      }
    }
  }
}

def createNextDevVersionJVMParam() {
  def nextDevelopmentVersion = '' 
  if (params.nextDevVersion.trim() =~ /\d+\.\d+\.\d+/) {
    echo "nextDevVersion is set to ${params.nextDevVersion.trim()}"
    nextDevelopmentVersion = "-DdevelopmentVersion=${params.nextDevVersion.trim()}-SNAPSHOT"
  } else {
    echo "nextDevVersion is NOT set or does not match version pattern - using default"
  }
  return nextDevelopmentVersion
}
