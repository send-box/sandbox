package com.fullstack.batch.service.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.batch.model.ProcessorReceiveDTO;
import com.fullstack.batch.model.entity.MeasureInfoReal;
import com.fullstack.batch.model.entity.MeasureInfoRealStage;
import com.fullstack.batch.repository.MeasureInfoRealRepository;
import com.fullstack.batch.service.MeasureInfoRealService;



@Transactional
@Service
public class MeasureInfoRealImpl implements MeasureInfoRealService
{
	@Autowired
	MeasureInfoRealRepository measureInfoRealRepository;

	@Override
	public void save(MeasureInfoReal measureInfoReal) throws InvalidParameterException
	{
		measureInfoRealRepository.save(measureInfoReal);	
	}
}
