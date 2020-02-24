package com.fullstack.batch.step.listener;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.fullstack.batch.model.entity.MeasureInfoReal;
import com.fullstack.batch.model.vo.MeasureInfoRealListVO;

public class ListenerHDFS extends JobExecutionListenerSupport
{    
    private static final Logger log       = LoggerFactory.getLogger(ListenerHDFS.class);
    //private static final String HEADER    = "id,data_time,sido_name,station_name,mang_name,so2_value,co_value,o3_value,no2_value,pm10_value,pm10_value_24h,pm25_value,pm25_value_24h,khai_value,so2_grade,co_grade,o3_grade,no2_grade,pm10_grade,pm10_grade_1h,pm25_grade,pm25_grade_1h,khai_grade";
    private static final String HEADER    = "id,data_time,sido_name,station_name,mang_name";  //,so2_value,co_value,o3_value,no2_value,pm10_value,pm10_value_24h,pm25_value,pm25_value_24h,khai_value,so2_grade,co_grade,o3_grade,no2_grade,pm10_grade,pm10_grade_1h,pm25_grade,pm25_grade_1h,khai_grade";
    private static final String LINE_DILM = ",";
   
    @Autowired
    private MeasureInfoRealListVO measureInfoRealListVO;

    @Override
    public void afterJob(JobExecution jobExecution)
    {
        log.info("[JobListener] afterJob() start : {}", jobExecution.getStatus());
        
        if (jobExecution.getStatus() == BatchStatus.COMPLETED)
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date             date             = new Date();
            
            String createTime = simpleDateFormat.format(date);
            
            String filename = "measure_info_real_" + createTime + ".csv";
            
            try 
            {
                Configuration conf = new Configuration();

                // 50010 포트에서 사용하는 IP에 hostname 을 사용하도록 설정
                // 도커와 같은 가상IP 환경일 경우 실제 IP 를 찾기 때문에 파일은 생성이 되어도 datanode 에 쓸 때 IP를 찾지 못한다.
                conf.set("dfs.client.use.datanode.hostname", "true");
                
                // 서버는 아래와 같이 설정
//            <configuration>
//                <property>
//                    <name>dfs.replication</name>
//                    <value>1</value>  // 단일 namenode, datanode
//                    <final>true</final>
//                </property>
//                <property>
//                    <name>dfs.namenode.name.dir</name>
//                    <value>/hadoop_home/namenode_home</value>
//                    <final>true</final>
//                </property>
//                <property>
//                    <name>dfs.datanode.data.dir</name>
//                    <value>/hadoop_home/datanode_home</value>
//                    <final>true</final>
//                </property>
//                <property>
//                        <name>dfs.client.use.datanode.hostname</name>  // client 와 맞춰워야 함
//                        <value>true</value>
//                </property>
//                <property>
//                        <name>dfs.permissions.enabled</name>
//                        <value>false</value>
//                </property>
//            </configuration>
            
                FileSystem fs = FileSystem.get(new URI("hdfs://master:9000"), conf);
                
                FSDataOutputStream out = fs.create(new Path(filename));
                
                out.writeBytes(HEADER);
                
                for (MeasureInfoReal record : measureInfoRealListVO.get())
                {
                    out.writeBytes(new StringBuilder().append(record.getColumnA1()).append(LINE_DILM)
                                                      .append(record.getColumnA3()).append(LINE_DILM)
                                                      .append(record.getColumnA5()).append(LINE_DILM)
                                                      .append(record.getColumnA6()).append(LINE_DILM)
                                                      .append(record.getColumnA7()).append(LINE_DILM)
                                                      .append(record.getColumnA8()).append(LINE_DILM)
                                                      .append(record.getColumnA9()).append(LINE_DILM)
                                                      .append(record.getColumnB1()).append(LINE_DILM)
                                                      .append(record.getColumnB2()).append(LINE_DILM)
                                                      .append(record.getColumnB3()).append(LINE_DILM)
                                                      .append(record.getColumnB4()).append(LINE_DILM)
                                                      .append(record.getColumnB5()).append(LINE_DILM)
                                                      .append(record.getColumnB6()).append(LINE_DILM)
                                                      .append(record.getColumnB7()).append(LINE_DILM)
                                                      .append(record.getColumnB8()).append(LINE_DILM)
                                                      .append(record.getColumnB9()).append(LINE_DILM)
                                                      .append(record.getColumnC1()).append(LINE_DILM)
                                                      .append(record.getColumnC2()).append(LINE_DILM)
                                                      .append(record.getColumnC3()).append(LINE_DILM)
                                                      .append(record.getColumnC4()).append(LINE_DILM)
                                                      .append(record.getColumnC5()).append(LINE_DILM)
                                                      .append(record.getColumnC6()).toString());
                    
                    out.writeBytes("\n");
                }
                
                out.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
