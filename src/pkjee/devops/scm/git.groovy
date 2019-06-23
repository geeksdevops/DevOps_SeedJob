/*************************************************************************
**** Description :: this groovy code is used to clone the git code    ****
**** Created By  :: Pramod Vishwakarma                                ****
**** Created On  :: 23/06/2019                                        ****
**** version     :: 1.0                                               ****
**************************************************************************/
package pkjee.devops.scm

/*****************************************************
***** function to checkout code from Git repository
******************************************************/
def Checkout(String GIT_URL, String BRANCH, String GIT_CREDENTIALS)
{
   try {
        wrap([$class: 'AnsiColorBuildWrapper']) {
          println "\u001B[32mINFO => Checking out ${GIT_URL} from branch ${BRANCH}, please wait..."
          checkout([$class: 'GitSCM', branches: [[name: "${BRANCH}"]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 30]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIALS}", url: "${GIT_URL}"]]])
          env.GIT_BRANCH = "${BRANCH}"
          env.GIT_URL = "${GIT_URL}"
          env.GIT_COMMIT = getGitCommitHash()
          env.GIT_AUTHOR_EMAIL = getCommitAuthorEmail()
        }
   }
   catch (Exception caughtError) {
       wrap([$class: 'AnsiColorBuildWrapper']) {
          print "\u001B[41m[ERROR]: clone for repository ${env.GIT_URL} failed, please check the logs..."
          currentBuild.result = "FAILURE"
          throw caughtError
       }
   }
}

/**********************************************
***** function to get the Git commit hash *****
***********************************************/
def getGitCommitHash()
{
   try {
     gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
     return gitCommit
   }
   catch (Exception error)
   {
     wrap([$class: 'AnsiColorBuildWrapper']) {
          print "\u001B[41m[ERROR] failed to get last Git commit ID....."
          throw error
     }
   }
}

/*************************************************
***** Function to get the committer email id *****
**************************************************/
def getCommitAuthorEmail()
{
   try {
     def COMMIT = getGitCommitHash()
     sh "git log --format='%ae' $COMMIT > author"
     def author = readFile('author').trim()
     return author
   }
   catch (Exception error)
   {
     wrap([$class: 'AnsiColorBuildWrapper']) {
          print "\u001B[41m[ERROR] failed to get the last Git commit author email ID....."
          throw error
     }
   }
}


