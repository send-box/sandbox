package com.tips.yBatch.schedule;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler
{
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    Job processJob;
    
    @Bean
    @Scheduled(cron = "*/5 * * * * *")
    public void batchRun() throws Exception
    {
        System.out.println("------------------------------- SpringBatch Start!!! -------------------------------");

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();

        jobLauncher.run(processJob, jobParameters);
    }
}
