package com.fullstack.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fullstack.back.model.MeasureInfoReal;
import com.fullstack.back.model.SO2ValueInfoReal;

@Repository
public interface MeasureInfoRealEntityRepository extends JpaRepository<MeasureInfoReal, Long>
{
	@Query(name="measureInfoRealEntity.findSO2ValueInfoRealBySidonameAndMangname", nativeQuery=true)
	List<SO2ValueInfoReal> findSO2ValueInfoRealBySidonameAndMangname(String sidoName, String mangName);
}
