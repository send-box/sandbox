package com.fullstack.batch.step.reader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.batch.mapper.ColumnMap;
import com.fullstack.batch.model.ReaderReturnDTO;
import com.fullstack.batch.model.entity.MeasureInfoRealStage;

import lombok.Data;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public class ReaderRestApi implements ItemReader<List<ReaderReturnDTO>>
{
    private static final Logger                log                 = LoggerFactory.getLogger(ReaderRestApi.class);
    private static final String[]              runningKey          = {"서울", "경기", "인천" };
    public               List<ReaderReturnDTO> readerReturnDTOList = new ArrayList<ReaderReturnDTO>();
    private              boolean               checkRestCall       = false;
    
    @Value("#{jobParameters['StartTime']}")
    public String startTime;
    
    @Override
    public List<ReaderReturnDTO> read()
    {
        log.info("[ReaderRestApi] read() startTime : {}", startTime);
        
        readerReturnDTOList = null;

        if (checkRestCall == false)
        {
          //readerReturnDTOList = this.getResource();
            readerReturnDTOList = this.getResourceMock();
                
            log.info("[ReaderRestApi] read() readerReturnDTOList.size() : {}", readerReturnDTOList.size());

            checkRestCall = true;
        }
        
        return readerReturnDTOList;
    }
    
    // Mock Data
    public List<ReaderReturnDTO> getResourceMock()
    {
        List<ReaderReturnDTO> readerReturnDTOListTmp = new ArrayList<ReaderReturnDTO>();
        
        for (int i = 0; i < runningKey.length; i++)
        {
            ReaderReturnDTO readerReturnDTO = new ReaderReturnDTO();
        
            readerReturnDTO.setColumn1(startTime            );
            readerReturnDTO.setColumn2("Long"               );
            readerReturnDTO.setColumn3(runningKey[i]        );
            readerReturnDTO.setColumn4("ABC123"             );
            readerReturnDTO.setColumn5("GasanDigitalComplex");
            readerReturnDTO.setColumn6("Net"                );
     
            readerReturnDTOListTmp.add(readerReturnDTO);
        }
        
        return readerReturnDTOListTmp;
    }
    
    // Get Rest Api Data
    public List<ReaderReturnDTO> getResource()
    {
        List<ReaderReturnDTO> readerReturnDTOListTmp = new ArrayList<ReaderReturnDTO>();
        
        for (int i = 0; i < runningKey.length; i++)
        {
            log.info("[ReaderRestApiImpl] getResource() runningKey : " + runningKey[i]);
            
            try
            {
                // 미세먼지 공공 REST API ------------------------------------------------------------------------------------------------------
                String url           = "http://openapi.airkorea.or.kr/openapi/services/rest/";
                String serviceId     = "ArpltnInforInqireSvc"                                ;  // 대기오염정보 조회 서비스
                String operationName = "getCtprvnRltmMesureDnsty"                            ;  // 시도별 실시간 측정정보 조회
              //String serviceKey    = "bg9choiwFZX5JYcIIF76jFiVYe0VwiWdxdpCUldbALWxzJLNZA4Ipq2Z1SVqkZyWSW88og%2Bt8EiOCX9J%2BB3ZUw%3D%3D"  ;
                String serviceKey    = "2%2Bxy%2FDG9FLV3s9hUtwRXX1%2F4KjR92LJqXblaoGqWPzs2u4s4ZxqgXnYQiEUNIAaoXjy66zBIafygmX8ayFFgRw%3D%3D";
                String numOfRows     = "100"                                  ;
                String pageNo        = "1"                                    ;
                String sidoName      = URLEncoder.encode(runningKey[i], "UTF-8");
                String version       = "1.3"                                  ;
                String returnType    = "json"                                 ;
                
                String uri = url
                           + serviceId
                           + "/"
                           + operationName
                           + "?" + "serviceKey="   + serviceKey
                           + "&" + "numOfRows="    + numOfRows
                           + "&" + "pageNo="       + pageNo
                           + "&" + "sidoName="     + sidoName
                           + "&" + "ver="          + version
                           + "&" +  "_returnType=" + returnType;
                
                log.info("[ReaderRestApiImpl] getResource() uri : " + uri);
                
                URL urlObj = new URL(uri.toString());
                
                HttpURLConnection urlconnection = (HttpURLConnection) urlObj.openConnection();
                
                urlconnection.setRequestMethod("GET");
                urlconnection.setRequestProperty("Content-type", "application/json");
                
                BufferedReader response;
                
                if (urlconnection.getResponseCode() >= 200 && urlconnection.getResponseCode() <= 300)
                {
                    response = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
                }
                else
                {
                    response = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
                };
                
                // ------------------------------------------------------------------------------------------------------------------------
                
                ObjectMapper mapper = new ObjectMapper();
                
                JsonNode list = mapper.readTree(response).get("list");
                
                for (int j = 0; j < list.size(); j++)
                {
                    ReaderReturnDTO readerReturnObj = new ReaderReturnDTO();
    
                    readerReturnObj.setColumn1 (list.get(j).get(ColumnMap.json[ 0]).textValue());
                    readerReturnObj.setColumn2 (list.get(j).get(ColumnMap.json[ 1]).textValue());
                    readerReturnObj.setColumn3 (runningKey[i]                                  );
                    readerReturnObj.setColumn4 (list.get(j).get(ColumnMap.json[ 3]).textValue());
                    readerReturnObj.setColumn5 (list.get(j).get(ColumnMap.json[ 4]).textValue());
                    readerReturnObj.setColumn6 (list.get(j).get(ColumnMap.json[ 5]).textValue());
                    readerReturnObj.setColumn7 (list.get(j).get(ColumnMap.json[ 6]).textValue());
                    readerReturnObj.setColumn8 (list.get(j).get(ColumnMap.json[ 7]).textValue());
                    readerReturnObj.setColumn9 (list.get(j).get(ColumnMap.json[ 8]).textValue());
                    readerReturnObj.setColumn10(list.get(j).get(ColumnMap.json[ 9]).textValue());
                    readerReturnObj.setColumn11(list.get(j).get(ColumnMap.json[10]).textValue());
                    readerReturnObj.setColumn12(list.get(j).get(ColumnMap.json[11]).textValue());
                    readerReturnObj.setColumn13(list.get(j).get(ColumnMap.json[12]).textValue());
                    readerReturnObj.setColumn14(list.get(j).get(ColumnMap.json[13]).textValue());
                    readerReturnObj.setColumn15(list.get(j).get(ColumnMap.json[14]).textValue());
                    readerReturnObj.setColumn16(list.get(j).get(ColumnMap.json[15]).textValue());
                    readerReturnObj.setColumn17(list.get(j).get(ColumnMap.json[16]).textValue());
                    readerReturnObj.setColumn18(list.get(j).get(ColumnMap.json[17]).textValue());
                    readerReturnObj.setColumn19(list.get(j).get(ColumnMap.json[18]).textValue());
                    readerReturnObj.setColumn20(list.get(j).get(ColumnMap.json[19]).textValue());
                    readerReturnObj.setColumn21(list.get(j).get(ColumnMap.json[20]).textValue());
                    readerReturnObj.setColumn22(list.get(j).get(ColumnMap.json[21]).textValue());
                    readerReturnObj.setColumn23(list.get(j).get(ColumnMap.json[22]).textValue());
                    readerReturnObj.setColumn24(list.get(j).get(ColumnMap.json[23]).textValue());
                    readerReturnObj.setColumn25(serviceId                                      );
                    readerReturnObj.setColumn26(list.get(j).get(ColumnMap.json[25]).textValue());
                    readerReturnObj.setColumn27(list.get(j).get(ColumnMap.json[26]).textValue());
                    readerReturnObj.setColumn28(list.get(j).get(ColumnMap.json[27]).textValue());
                    readerReturnObj.setColumn29(list.get(j).get(ColumnMap.json[28]).textValue());
                    readerReturnObj.setColumn30(list.get(j).get(ColumnMap.json[29]).textValue());
                    readerReturnObj.setColumn31(list.get(j).get(ColumnMap.json[30]).textValue());
                    readerReturnObj.setColumn32(version);
                    
                    readerReturnDTOListTmp.add(readerReturnObj);
                }
    
                response.close();
                
                urlconnection.disconnect();
            }
            catch(Exception e)
            {
                log.info("[ReaderRestApiImpl]");
                
                e.printStackTrace();
    
                return null;
            }
        }
        
        return readerReturnDTOListTmp;
    }
}
