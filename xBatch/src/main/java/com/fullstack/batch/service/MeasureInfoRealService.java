package com.fullstack.batch.service;

import java.security.InvalidParameterException;
import java.util.List;

import com.fullstack.batch.model.entity.MeasureInfoReal;
import com.fullstack.batch.model.entity.MeasureInfoRealStage;

public interface MeasureInfoRealService
{
	void save(MeasureInfoReal measureInfoReal) throws InvalidParameterException;
}
