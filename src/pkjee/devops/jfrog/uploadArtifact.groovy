/**************************************************************
***** Description :: This Package is used for jfrog uplaod *****
***** Author      :: Prmaod Vishwakarma                   *****
***** Date        :: 23/06/2019                           *****
***** Revision    :: 1.0                                  *****
***************************************************************/

package pkjee.devops.jfrog

/********************************************
** Function to jfrog uplaod Process      *****
*********************************************/
def artiFact()
{
  try {
    wrap([$class: 'AnsiColorBuildWrapper']) {
      println "\u001B[32mINFO => uplaoding ${JOB_NAME} to Jfrog artifact, please wait..."
      def Result = "${JOB_NAME} ${BUILD_ID}"
      sh "echo ${Result}"
    }
  }
   catch (Exception caughtException) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41mERROR => failed to upload ${BUILD_ID} jfrog artifcatry ..."
         currentuplaod.result = 'FAILED'
         throw caughtException
    }
  }
}

