package com.fullstack.batch.step.writer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.fullstack.batch.mapper.ColumnMap;
import com.fullstack.batch.model.FileWriteDTO;
import com.fullstack.batch.model.ProcessorReceiveDTO;
import com.fullstack.batch.model.entity.MeasureInfoReal;
import com.fullstack.batch.model.entity.MeasureInfoRealStage;
import com.fullstack.batch.model.vo.BizVO;
import com.fullstack.batch.model.vo.MeasureInfoVO;


@StepScope
public class WriterDBImpl implements ItemWriter<List<ProcessorReceiveDTO>>
{
    private static final Logger log = LoggerFactory.getLogger(WriterDBImpl.class);
    
    @Autowired
    MeasureInfoVO measureInfoVO;
    
    private JdbcBatchItemWriter<MeasureInfoRealStage> batchTargetWriter;
    
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

    private static final String sql = "insert into public.measure_info_real_stage "
                                    + "(                                          "
                                    + "    " + ColumnMap.column[ 0] + ",          "   
                                    + "    " + ColumnMap.column[ 1] + ",          " 
                                    + "    " + ColumnMap.column[ 2] + ",          " 
                                    + "    " + ColumnMap.column[ 3] + ",          " 
                                    + "    " + ColumnMap.column[ 4] + ",          " 
                                    + "    " + ColumnMap.column[ 5] + ",          " 
                                    + "    " + ColumnMap.column[ 6] + ",          " 
                                    + "    " + ColumnMap.column[ 7] + ",          " 
                                    + "    " + ColumnMap.column[ 8] + ",          " 
                                    + "    " + ColumnMap.column[ 9] + ",          " 
                                    + "    " + ColumnMap.column[10] + ",          " 
                                    + "    " + ColumnMap.column[11] + ",          " 
                                    + "    " + ColumnMap.column[12] + ",          " 
                                    + "    " + ColumnMap.column[13] + ",          " 
                                    + "    " + ColumnMap.column[14] + ",          " 
                                    + "    " + ColumnMap.column[15] + ",          " 
                                    + "    " + ColumnMap.column[16] + ",          " 
                                    + "    " + ColumnMap.column[17] + ",          " 
                                    + "    " + ColumnMap.column[18] + ",          " 
                                    + "    " + ColumnMap.column[19] + ",          " 
                                    + "    " + ColumnMap.column[20] + ",          " 
                                    + "    " + ColumnMap.column[21] + ",          " 
                                    + "    " + ColumnMap.column[22] + ",          " 
                                    + "    " + ColumnMap.column[23] + ",          " 
                                    + "    " + ColumnMap.column[24] + ",          " 
                                    + "    " + ColumnMap.column[25] + ",          " 
                                    + "    " + ColumnMap.column[26] + ",          " 
                                    + "    " + ColumnMap.column[27] + ",          " 
                                    + "    " + ColumnMap.column[28] + ",          " 
                                    + "    " + ColumnMap.column[29] + ",          " 
                                    + "    " + ColumnMap.column[30] + ",          " 
                                    + "    " + ColumnMap.column[31] 
                                    + ")                                                                                                             "
                                    + "values                                                                                                        "
                                    + "(                                                                                                             "
                                    + "    :columnA1 , :columnA2 , :columnA3 , :columnA4 , :columnA5 , :columnA6 , :columnA7 , :columnA8 , :columnA9 "
                                    + "  , :columnB1 , :columnB2 , :columnB3 , :columnB4 , :columnB5 , :columnB6 , :columnB7 , :columnB8 , :columnB9 "
                                    + "  , :columnC1 , :columnC2 , :columnC3 , :columnC4 , :columnC5 , :columnC6 , :columnC7 , :columnC8 , :columnC9 "
                                    + "  , :columnD1 , :columnD2 , :columnD3 , :columnD4 , :columnD5                                                 "
                                    + ")                                                                                                             ";
    
    public WriterDBImpl()
    {
        dataSource.setDriver  (new org.postgresql.Driver());
        dataSource.setUrl     ("jdbc:postgresql://localhost:5432/tipsdb");
        dataSource.setUsername("tipsuser");
        dataSource.setPassword("tipsuser");
    }

    @BeforeStep
    public void prepareForWriter()
    {
        this.batchTargetWriter = new JdbcBatchItemWriter<MeasureInfoRealStage>();
        
        this.batchTargetWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<MeasureInfoRealStage>());
        this.batchTargetWriter.setDataSource(dataSource);
        this.batchTargetWriter.setJdbcTemplate(new NamedParameterJdbcTemplate(dataSource));
        this.batchTargetWriter.setSql(sql);
        this.batchTargetWriter.afterPropertiesSet();
    }
    
    @Override
    public void write(List<? extends List<ProcessorReceiveDTO>> items) throws Exception
    {
        log.info("[WriterImplJpa] write() items : " + items.toString());
        
        ArrayList <MeasureInfoRealStage> batchTargetList = new ArrayList<MeasureInfoRealStage>();
        
        for (List<ProcessorReceiveDTO> list : items)
        {
            //log.info("[WriterImplJpa] write() list : " + list.toString());
            
            list.forEach(record ->
            {
                //log.info("[WriterImplJpa] write() record : " + record.toString());
                
                MeasureInfoRealStage batchTarget = new MeasureInfoRealStage();
                
                batchTarget.setColumnA1(record.getColumn1 ());
                batchTarget.setColumnA2(record.getColumn2 ());
                batchTarget.setColumnA3(record.getColumn3 ());
                batchTarget.setColumnA4(record.getColumn4 ());
                batchTarget.setColumnA5(record.getColumn5 ());
                batchTarget.setColumnA6(record.getColumn6 ());
                batchTarget.setColumnA7(record.getColumn7 ());
                batchTarget.setColumnA8(record.getColumn8 ());
                batchTarget.setColumnA9(record.getColumn9 ());
                batchTarget.setColumnB1(record.getColumn10());
                batchTarget.setColumnB2(record.getColumn11());
                batchTarget.setColumnB3(record.getColumn12());
                batchTarget.setColumnB4(record.getColumn13());
                batchTarget.setColumnB5(record.getColumn14());
                batchTarget.setColumnB6(record.getColumn15());
                batchTarget.setColumnB7(record.getColumn16());
                batchTarget.setColumnB8(record.getColumn17());
                batchTarget.setColumnB9(record.getColumn18());
                batchTarget.setColumnC1(record.getColumn19());
                batchTarget.setColumnC2(record.getColumn20());
                batchTarget.setColumnC3(record.getColumn21());
                batchTarget.setColumnC4(record.getColumn22());
                batchTarget.setColumnC5(record.getColumn23());
                batchTarget.setColumnC6(record.getColumn24());
                batchTarget.setColumnC7(record.getColumn25());
                batchTarget.setColumnC8(record.getColumn26());
                batchTarget.setColumnC9(record.getColumn27());
                batchTarget.setColumnD1(record.getColumn28());
                batchTarget.setColumnD2(record.getColumn29());
                batchTarget.setColumnD3(record.getColumn30());
                batchTarget.setColumnD4(record.getColumn31());
                batchTarget.setColumnD5(record.getColumn32());
                
                batchTargetList.add(batchTarget);

                // To Listener
                MeasureInfoReal measureInfoReal = new MeasureInfoReal();
                
                measureInfoReal.setColumnA1(record.getColumn1 ());
                measureInfoReal.setColumnA3(record.getColumn3 ());
                measureInfoReal.setColumnA5(record.getColumn5 ());
                measureInfoReal.setColumnA6(record.getColumn6 ());
                measureInfoReal.setColumnA7(record.getColumn7 ());
                measureInfoReal.setColumnA8(record.getColumn8 ());
                measureInfoReal.setColumnA9(record.getColumn9 ());
                measureInfoReal.setColumnB1(record.getColumn10());
                measureInfoReal.setColumnB2(record.getColumn11());
                measureInfoReal.setColumnB3(record.getColumn12());
                measureInfoReal.setColumnB4(record.getColumn13());
                measureInfoReal.setColumnB5(record.getColumn14());
                measureInfoReal.setColumnB6(record.getColumn15());
                measureInfoReal.setColumnB7(record.getColumn16());
                measureInfoReal.setColumnB8(record.getColumn17());
                measureInfoReal.setColumnB9(record.getColumn18());
                measureInfoReal.setColumnC1(record.getColumn19());
                measureInfoReal.setColumnC2(record.getColumn20());
                measureInfoReal.setColumnC3(record.getColumn21());
                measureInfoReal.setColumnC4(record.getColumn22());
                measureInfoReal.setColumnC5(record.getColumn23());
                measureInfoReal.setColumnC6(record.getColumn24());
                
                measureInfoVO.put(record.getColumn5(), measureInfoReal);
            });
            
            this.batchTargetWriter.write(batchTargetList);
        }
    }
}