package com.fullstack.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fullstack.batch.BatchApplication;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
public class BatchApplication
{
    @Autowired
    public JobLauncher jobLauncher;
    
    @Autowired
    public Job job;
    
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(BatchApplication.class, args);
    }
}

