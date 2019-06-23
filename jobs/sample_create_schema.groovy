/*
 * create Jenkins job to Visible the VRApp Database Schema creation and DB refresh Trigger
 *
 */
def LicenseFolder = "EPMC-VRAPP-FEATURE"
def currentEnv = "epmc-dev-1"
boolean disabledMail = true
def jobName = LicenseFolder+"/Database-Create-"+currentEnv
def lockResourceName = currentEnv+"-EPMC-VRAPP-FEATURE"
//def automationPodLabel = "vulnerability-remediation-uat-"+currentEnv
//def authToken1 = "b04ae1df4846b68"
def suiteName = 'VRApp DB schema create or refresh'
def SQL_SCRIPT = "DB_SCHEMA.SQL"

folder(LicenseFolder)
freeStyleJob(jobName){
    //-- General ---
    logRotator(2, 20, 1, -1)
    lockableResources(lockResourceName)
    //-- Choose CA Customer Data File and GroovyScript to execute the table update
    parameters {
        fileParam('DB_SCHEMA.SQL','Please upload databse.sql to create/Refresh datbase schema VRAPP.SQL -https://stash.ges.symantec.com/projects/EPMC/repos/vulnerability-remediation-database/browse/vulnerability-remediation-database/vrapp.sql or CATALOG.SQL - https://stash.ges.symantec.com/projects/EPMC/repos/vulnerability-catalog-service/browse/vulnerability-catalog-service-database/catalog.sql')
        stringParam('ENDPOINT_HOST_URL','localhost','RDS ENDPOINT URL-WRITER')
	stringParam('DB_PASSWORD','abc@xyz','DB PASSWORD')
    }
//    label(automationPodLabel)

    //-- Source Code Management --
//    authenticationToken(authToken1)
    
    //-- Build Environment --
    wrappers {
        preBuildCleanup {
            deleteDirectories(true)
        }
    }

    // -- Build --

    steps{
        shell( """
		mysql -V
		if [ \$? -eq 0 ]; then
			echo "MYSQL Package is already installed..!"
		else
			echo "MYSQL MARIADB installation started, please wait..."
			yum -y install mariadb
		fi
		if [ -f ${SQL_SCRIPT} ]
                then
                        mysql -h\$ENDPOINT_HOST_URL -uroot -p\$DB_PASSWORD < ${SQL_SCRIPT}
		else
			echo "${SQL_SCRIPT} upload error & databse creations/refresh failed"; exit 1
		fi
		""")
    }

}//freeStyleJob
