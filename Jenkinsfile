#!groovy
@Library('Global_Libarary@master') _
node {
properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '2', numToKeepStr: '2'))])
demo_pipeline {
GIT_URL 		               = 'GitHobURL'
BRANCH                     = 'Git Branch'
GIT_CREDENTIALS 	         = 'GIT_CREDENTIALS'
MAVEN_HOME                 ='/app/maven-3.5.4'
//MAVEN_GOAL 		           = 'clean install'
MAVEN_GOAL                 = 'versions:set -DgroupId=org.apache.maven.* -DartifactId=* -DoldVersion=2.0.* -DnewVersion=2.0.1'
    }
}
