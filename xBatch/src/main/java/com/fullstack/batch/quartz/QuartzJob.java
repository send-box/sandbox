package com.fullstack.batch.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.Data;

/**
 * The Class QuartzJobLauncher.
 *
 * @author ashraf
 */
@Data
//public class QuartzJob extends QuartzJobBean
public class QuartzJob
{
//    private static final Logger log = LoggerFactory.getLogger(QuartzJob.class);
//
//    private String      jobName;
//    private JobLauncher jobLauncher;
//    private JobLocator  jobLocator;
//    
//    @Override
//    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
//    {
//        try
//        {
//            Job job = jobLocator.getJob(jobName);
//            
//            JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
//            
//            log.info("[QuartzJob] executeInternal() : {}(JobId:{}) was completed successfully", job.getName(), jobExecution.getId());
//        } 
//        catch (Exception e) 
//        {
//            log.info("[QuartzJob] executeInternal() Encountered job execution exception!" + e.toString());
//        }
//    }
}
