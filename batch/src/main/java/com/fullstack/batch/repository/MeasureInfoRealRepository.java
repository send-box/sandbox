package com.fullstack.batch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fullstack.batch.model.ProcessorReceiveDTO;
import com.fullstack.batch.model.entity.MeasureInfoReal;
import com.fullstack.batch.model.entity.MeasureInfoRealStage;


@Repository
public interface MeasureInfoRealRepository extends JpaRepository<MeasureInfoReal, String>
{
}
