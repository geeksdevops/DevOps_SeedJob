/***********************************************************************************
**** Description :: This groovy code is used Build JAVA Code and Deploye Docker  ****
**** Created By  :: Pramod Vishwakarma                                           ****
**** Created in  :: 23/June/2019                                                   ****
**** version     :: 1.0                                                          ****
************************************************************************************/

import pkjee.devops.scm.*
import pkjee.devops.build.*
import pkjee.devops.jfrog.*
import pkjee.devops.coverage.*
import pkjee.devops.report.*

def call(body)
{
	def config = [:]
	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = config
	body()
	timestamps {
  try {
        def g = new git()
        def m = new mavenBuild()
        def u = new uploadArtifact()
        def j = new jUnitReport()
	def h = new htmlReport()
	currentBuild.result = "SUCCESS"
	NEXT_STAGE = "none"

        stage ('\u2780 Code_Checkout') {
                g.Checkout("${config.GIT_URL}","${config.BRANCH}","${config.GIT_CREDENTIALS}")
                NEXT_STAGE="maven_build"
                }
        stage ('\u2781 Maven_Build') {
                while (NEXT_STAGE != "maven_build") {
                continue
                }
                m.MavenBuild("${config.MAVEN_HOME}","${config.MAVEN_GOAL}")
                NEXT_STAGE="Junit_Report"
                }
	stage ('\u2782 JUnit_Coverage') {
                while (NEXT_STAGE != "Junit_Report") {
                continue
                }
		j.jUnitReport()
                NEXT_STAGE="jfrog_Artifact"
                }
        stage ('\u2783 Artifact') {
	  parallel (
                "\u278A Upload Artifact" : {
                while (NEXT_STAGE != "jfrog_Artifact") {
                continue
                }
                u.artiFact()
                NEXT_STAGE='Html_publish'
                },
                "\u278B HTML_Report" : {
                while (NEXT_STAGE != "Html_publish") {
                continue
                }
                h.htmlPublish()
                },
                failFast: true
                  )
		}
	}
        catch (Exception caughtError) {
                wrap([$class: 'AnsiColorBuildWrapper']) {
                print "\u001B[41mERROR => Build pipeline failed, check detailed logs..."
                currentBuild.result = "FAILURE"
                throw caughtError
                }
        }
    }
}
