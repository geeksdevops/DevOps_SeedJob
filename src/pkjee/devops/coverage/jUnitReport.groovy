/**************************************************************
**** Description :: This Package is used for Junit Report *****
***** Author      :: Prmaod Vishwakarma                   *****
***** Date        :: 23/06/2019                           *****
***** Revision    :: 1.0                                  *****
***************************************************************/

package pkjee.devops.coverage

/********************************************
** Function to Junit Report Publish	*****
*********************************************/

def jUnitReport()
{
  try {
    wrap([$class: 'AnsiColorBuildWrapper']) {
      println "\u001B[32mINFO => Publishing Junit Report, please wait..."
      step([$class: 'JUnitResultArchiver', testResults: '**/surefire-reports/*.xml', healthScaleFactor: 1.0])
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


