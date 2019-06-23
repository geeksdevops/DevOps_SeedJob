/**************************************************************
**** Description :: This Package is used for Junit Report *****
***** Author      :: Prmaod Vishwakarma                   *****
***** Date        :: 23/06/2019                           *****
***** Revision    :: 1.0                                  *****
***************************************************************/

package pkjee.devops.report

/********************************************
** Function to Junit Report Publish	*****
*********************************************/

def htmlPublish()
{
  try {
    wrap([$class: 'AnsiColorBuildWrapper']) {
      println "\u001B[32mINFO => Publishing Junit Report, please wait..."
	 publishHTML (target: [
         allowMissing: false,
         alwaysLinkToLastBuild: false,
         keepAll: true,
         reportDir: 'vulnerability-assessment-service/target/surefire-reports',
         reportFiles: 'index.html',
         reportName: "Coverage Report"
         ])
    }
  }
   catch (Exception caughtException) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41mERROR => failed to publish Junit report, please check logs..."
         currentBuild.result = 'FAILED'
         throw caughtException
    }
  }
}


