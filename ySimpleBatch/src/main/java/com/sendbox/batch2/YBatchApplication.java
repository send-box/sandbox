package com.sendbox.batch2;

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
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class YBatchApplication {

//	@Autowired
//	JobLauncher jobLauncher;
//	
//	@Autowired
//	Job processJob;
	
	public static void main(String[] args) {
		SpringApplication.run(YBatchApplication.class, args);
	}

//	@Override
//	public void run(String... strings) throws Exception {
//		System.out.println("---------------------- SpringBatch Start!!! ----------------------");
//
//		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
//		
//		jobLauncher.run(processJob, jobParameters);
//	}
}

