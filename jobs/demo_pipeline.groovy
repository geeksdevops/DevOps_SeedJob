def ENV_LIST = [
	'demo-pipeline-SeedJobs-1',
  	'demo-pipeline-SeedJobs-2'
]

def seedFolder = "Multi-Pipeline"

folder(seedFolder)
ENV_LIST.each {

  def NewPipelineJobNames= "${it}"
  def jobName = seedFolder +"/"+NewPipelineJobNames
  pipelineJob(jobName){
  	// --- general ---
    logRotator(2, 2, 1, -1)
    
    environmentVariables {
    
      env('PipelineJobNames', NewPipelineJobNames)
      env('CustomFullJobName', jobName)
    }
  }
}
