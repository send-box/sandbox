package com.fullstack.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.back.common.RestResponseEntity;
import com.fullstack.back.model.json.MeasureInfoRealJsonList;
import com.fullstack.back.service.MeasureInfoRealEntityService;

@RestController
@RequestMapping("/")
public class BackController
{
    @Autowired
    private MeasureInfoRealEntityService measureInfoRealEntityService;
    
    @GetMapping("/list")
    public RestResponseEntity<MeasureInfoRealJsonList> findMeasureInfoRealAll()
    {
        RestResponseEntity<MeasureInfoRealJsonList> result = null;
        
        result = new RestResponseEntity<MeasureInfoRealJsonList>(this.measureInfoRealEntityService.findMeasureInfoRealEntity());
        
        return result;
    }
    
    /*
     * URL : /list/page?page=<page>&size=<size>&sort=<property>,<asc|desc>
     * Parameter
     * - page : sequence of page that start from 0
     * - size : num of data
     * - property
     * 		# id : pk
     * 		# datatime
     * 		# ...
     */   
    @GetMapping("/list/page")
    public RestResponseEntity<MeasureInfoRealJsonList> findMeasureInfoRealPage(Pageable pageable)
    {
    	RestResponseEntity<MeasureInfoRealJsonList> result = null;
    	
    	result = new RestResponseEntity<MeasureInfoRealJsonList>(this.measureInfoRealEntityService.findMeasureInfoRealEntity(pageable));
    	
    	return result;
    }
    
    /*
     * URL : /list/so2?sido_name=<sido_name>&mang_name=<mang_name>
     * Parameter
     * - sindo_name
     * 		# 서울
     * 		# 경기
     * 		# 인천
     * - mang_name
     * 		# 도시대기
     * 		# 도로변대기
     */
    @GetMapping("/list/so2")
    public RestResponseEntity<MeasureInfoRealJsonList> findSO2ValueInfoReal(
    		@RequestParam("sido_name") String sidoName, @RequestParam("mang_name") String mangName)
    {
    	RestResponseEntity<MeasureInfoRealJsonList> result = null;
    	
    	result = new RestResponseEntity<MeasureInfoRealJsonList>(this.measureInfoRealEntityService.findSO2ValueInfoRealEntity(sidoName, mangName));
    	
    	return result;
    }
}