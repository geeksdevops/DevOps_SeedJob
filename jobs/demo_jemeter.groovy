/*
 * create Jenkins Job to execute the JMETER SCRIPTS 
 *
 */
def JmeterFolder = "DEMO-JMETER"
def currentEnv = "DemoJmeterJobs"
boolean disabledMail = true
def jobName = JmeterFolder+"/"+currentEnv
def lockResourceName = currentEnv+"-DEMO-JMETER"
//def automationPodLabel = "SlaveMachineName-"+currentEnv
//def authToken1 = "SlaveMachineToken"
def slackChannel = 'TBD'
def suiteName = 'JMETER_SCRIPT'
def createDevicesFile = "customersCreateDevices.csv"
def DummyDevices = "CreateDummyDevices_Final.jmx"
def Jmeter = "/app/apache-jmeter-5.1/bin"

folder(JmeterFolder)
freeStyleJob(jobName){
	 	//-- General ---
		logRotator(2, -1, 1, -1)
		lockableResources(lockResourceName)
		//-- Choose Data File and GroovyScript to execute the table update
		parameters {
			fileParam('customersCreateDevices.csv', 'Please upload customersCreateDevices .CSV file to execute Jmeter Scripts')
                        fileParam('CreateDummyDevices_Final.jmx', 'Please uplod CreateDummyDevices_Final.jmx file to be used')
		    }
//		label(automationPodLabel)

		//-- Source Code Management --
//		authenticationToken(authToken1)

		//-- Build Environment --
		wrappers {
			preBuildCleanup {
				deleteDirectories(true)
			}
		}

		// -- Build --

		steps{

			environmentVariables {
				env('JMETER_SCRIPT', suiteName)
			}

            shell("""
                if [ -f "$createDevicesFile" ]
                then
                    sh $Jmeter/jmeter -n -t $DummyDevices -Jdevicecount=10
		else
		    echo "Please verify the $createDevicesFile and $DummyDevices are uploaded correctly"
		    exit 1
                fi
		""")
        }
	// - Post Build Action 
            publishers{
            	archiveArtifacts('jmeter.log,output.csv')

            slackNotifier {
                startNotification(false)
                notifyAborted(false)
                notifyFailure(true)
                notifySuccess(true)
                notifyUnstable(true)
                notifyBackToNormal(false)
                notifyNotBuilt(false)
                notifyRepeatedFailure(true)
                includeTestSummary(false)
                includeCustomMessage(true)
                customMessage('*Test Type:* `$SUITE`   *Environment:*'+" `${currentEnv}`")
                teamDomain('symc-enterprise-cloud')
                authToken('')
                room(slackChannel)
                sendAs(null)
                commitInfoChoice('NONE')
            }//slackNotifier
       }//publishers
}//freeStyleJob
