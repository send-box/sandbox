package com.fullstack.batch.step.listener;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.fullstack.batch.model.FileWriteDTO;
import com.fullstack.batch.model.entity.MeasureInfoReal;
import com.fullstack.batch.model.entity.MeasureInfoRealStage;
import com.fullstack.batch.model.vo.MeasureInfoRealListVO;
import com.fullstack.batch.model.vo.MeasureInfoRealMapVO;
import com.fullstack.batch.repository.MeasureInfoRealRepository;
import com.fullstack.batch.service.MeasureInfoRealService;
import com.fullstack.batch.step.reader.ReaderRestApi;

public class ListenerDB extends JobExecutionListenerSupport
{
    private static final Logger log = LoggerFactory.getLogger(ListenerDB.class);
    
    @Autowired
    MeasureInfoRealService measureInfoRealService;
    
    @Autowired
    MeasureInfoRealMapVO measureInfoRealMapVO;
    
    @Autowired
    MeasureInfoRealListVO measureInfoRealListVO;
    
    @Override
    public void afterJob(JobExecution jobExecution)
    {
    	log.info("[JobListener] afterJob() jobExecution.getStatus() : {}, measureInfoRealListVO.size() : {}", jobExecution.getStatus(), measureInfoRealListVO.size());
    	
        if (jobExecution.getStatus() == BatchStatus.COMPLETED)
        {
          //for (MeasureInfoReal measureInfoReal : measureInfoVO.values())
            for (MeasureInfoReal measureInfoReal : measureInfoRealListVO.get())
            {
        	    //log.info("[JobListener] afterJob() measureInfoReal write to mart");
        	    
        		measureInfoRealService.save(measureInfoReal);
        	}
        }
    }
}
