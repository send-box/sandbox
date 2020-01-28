    package com.fullstack.batch.config;

import java.util.List;

import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.fullstack.batch.bean.DummyClass;
import com.fullstack.batch.bean.listener.ListenerDBExt;
import com.fullstack.batch.bean.listener.ListenerFlatFileExt;
import com.fullstack.batch.bean.processor.ProcessorImpl;
import com.fullstack.batch.bean.reader.ReaderDummyImpl;
import com.fullstack.batch.bean.reader.ReaderFlatFileExt;
import com.fullstack.batch.bean.reader.ReaderRestApiImpl;
import com.fullstack.batch.bean.writer.WriterDBImpl;
import com.fullstack.batch.bean.writer.WriterDTOImpl;
import com.fullstack.batch.model.ProcessorReceiveDTO;
import com.fullstack.batch.model.ReaderReturnDTO;
import com.fullstack.batch.model.entity.MeasureInfoRealStage;
import com.fullstack.batch.model.vo.BizVO;
import com.fullstack.batch.model.vo.MeasureInfoVO;

@Configuration
@EnableBatchProcessing
@Import({QuartzConfiguration.class})
public class BatchConfiguration
{
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // VO --------------------------------------------------------------------
    @Bean
    public BizVO bizVO()
    {
        return new BizVO();
    }

    @Bean
    public MeasureInfoVO measureInfoVO()
    {
        return new MeasureInfoVO();
    }
    
    // Reader ----------------------------------------------------------------
    @Bean
    public ReaderRestApiImpl readerRestApiImpl()
    {
        return new ReaderRestApiImpl();
    }
    
    @Bean
    public ReaderFlatFileExt readerFlatFileExt()
    {
        return new ReaderFlatFileExt();
    }

    @Bean
    public ReaderDummyImpl readerDummyImpl()
    {
        return new ReaderDummyImpl();
    }
    
    // Processor -------------------------------------------------------------
    @Bean
    public ProcessorImpl processorImpl()
    {
        return new ProcessorImpl();
    }
    
    // Writer--- -------------------------------------------------------------    
    @Bean
    public WriterDBImpl writerDBImpl()
    {
        return new WriterDBImpl();
    }
   
    @Bean
    public WriterDTOImpl writerDTOImpl()
    {
        return new WriterDTOImpl();
    }

    // Listener --------------------------------------------------------------
    @Bean
    public ListenerFlatFileExt listenerFlatFileExt()
    {
        return new ListenerFlatFileExt();
    }

    @Bean
    public ListenerDBExt listenerDBExt()
    {
        return new ListenerDBExt();
    }
    
    // RunIncreamenter -------------------------------------------------------
    @Bean
    public RunIdIncrementer runIdIncrementer()
    {
        return new RunIdIncrementer();
    }
    
    // Job Step Configuration ------------------------------------------------
    // Configure job step
    @Bean
    public Job jobBean()
    {
        return jobBuilderFactory.get("ETLJob")                       // Share Quartz Configuration
                                .incrementer(runIdIncrementer   ())  // Automatically parameter increase
                              //.listener   (listenerFlatFileExt())  // Must be Bean
                                .listener   (listenerDBExt      ())
                                .flow       (stepBean())
                                .end()
                                .build();
    }

    @Bean
    public Step stepBean()
    {
        return stepBuilderFactory.get("ETLStep")
                                 .allowStartIfComplete(true)                                      // allows step re-runnig although there is job that success
                               //.<     ReaderReturnDTO,       ProcessorReceiveDTO>  chunk(1000)  // First:Reader return type. Second:Writer receive type
                                 .<List<ReaderReturnDTO>, List<ProcessorReceiveDTO>> chunk(1000)  // First:Reader return type. Second:Writer receive type
                               //.reader   (readerFlatFileExt())
                               //.reader   (readerDummyImpl  ())
                                 .reader   (readerRestApiImpl())
                                 .processor(processorImpl    ())
                               //.writer   (writerDTOImpl    ())
                                 .writer   (writerDBImpl     ())
                                 .build();
    }
}
