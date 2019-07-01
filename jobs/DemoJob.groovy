/*
 * Seed Job to create Sample Demo Jobs
 *
 */
def SeedFolder = "SEED-JOBS"
def currentEnv = "Demo-CustomerJobs"
boolean disabledMail = true
def jobName = SeedFolder+"/"+currentEnv
def lockResourceName = currentEnv+"-SEED-JOBS"
//def automationPodLabel = "SlaveMachineName-"+currentEnv
//def authToken1 = "SlaveMachineToken"
def suiteName = 'ACTIVATED'
def caCustomerFile = "CA_Customers.json"
def groovyScript = "TableUpdate.py"

folder(SeedFolder)
freeStyleJob(jobName){
	 	//-- General ---
		logRotator(2, 2, 1, -1)
		lockableResources(lockResourceName)
		//-- Choose  Data File and GroovyScript to execute the table update
		parameters {
			fileParam('CA_Customers.json', 'Please upload CA customer file to do remediation')
			fileParam('TableUpdate.py', 'Please upload groovy script to update licensing table')
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
				env('ACTIVATED', suiteName)
			}
            shell("""
                if [ -f "$caCustomerFile" ]
                then
                	python $groovyScript
		else 
			echo "Please check $caCustomerFile $groovyScript are missing"
			exit 1
                fi
		""")
        }

}//freeStyleJob
