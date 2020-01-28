package com.fullstack.back.service;

import java.security.InvalidParameterException;

import org.springframework.data.domain.Pageable;

import com.fullstack.back.model.json.MeasureInfoRealJsonList;

public interface MeasureInfoRealEntityService
{
    public MeasureInfoRealJsonList findMeasureInfoRealEntity() throws InvalidParameterException;
    
    public MeasureInfoRealJsonList findMeasureInfoRealEntity(Pageable pageable) throws InvalidParameterException;
    
    public MeasureInfoRealJsonList findSO2ValueInfoRealEntity(String sidoName, String mangName) throws InvalidParameterException;
}
