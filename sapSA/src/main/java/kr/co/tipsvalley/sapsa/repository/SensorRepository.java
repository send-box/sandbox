package kr.co.tipsvalley.sapsa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.tipsvalley.sapsa.model.db.DeviceEntity;
import kr.co.tipsvalley.sapsa.model.db.SensorEntity;
import kr.co.tipsvalley.sapsa.util.CommonConst;

/*
 * JPA sensor repository.
 */
@Repository
public interface SensorRepository extends JpaRepository<SensorEntity, String> {
	
	@Query(value = "SELECT distinct(DEVICE_MAC_ADDR) AS DEVICE_MAC_ADDR FROM TIPS_SENSOR_TEMP2 group by device_mac_addr, device_time", nativeQuery = true)
	List<String> selectSensorDeviceList();
	
	@Query(value = "SELECT IFNULL(DEVICE_MAC_ADDR, ?1) AS DEVICE_MAC_ADDR"
			+"  , TO_CHAR(TO_DATE(t_date,'YYYYMMDD'),'YYYY.MM.DD') AS DEVICE_TIME "
			+"  , IFNULL(AVG( CASE WHEN avg_illuminace < 2 THEN avg_illuminace*100 ELSE avg_illuminace END ),0) AS AVG_ILLUMINACE  "
			+"  , IFNULL(MIN( CASE WHEN min_illuminace < 2 THEN min_illuminace*100 ELSE min_illuminace END ),0) AS MIN_ILLUMINACE  "
			+"  , IFNULL(MAX( CASE WHEN max_illuminace < 2 THEN max_illuminace*100 ELSE max_illuminace END ),0) AS MAX_ILLUMINACE  "
			+"  , IFNULL(AVG( CASE WHEN avg_temperature < 2 THEN avg_temperature*100 ELSE avg_temperature END ),0) AS AVG_temperature  "
			+"  , IFNULL(MIN( CASE WHEN min_temperature < 2 THEN min_temperature*100 ELSE min_temperature END ),0) AS MIN_temperature  "
			+"  , IFNULL(MAX( CASE WHEN max_temperature < 2 THEN max_temperature*100 ELSE max_temperature END ),0) AS MAX_temperature  "
			+"  , IFNULL(AVG( CASE WHEN avg_humidity < 2 THEN avg_humidity*100 ELSE avg_humidity END ),0) AS AVG_humidity  "
			+"  , IFNULL(MIN( CASE WHEN min_humidity < 2 THEN min_humidity*100 ELSE min_humidity END ),0) AS MIN_humidity  "
			+"  , IFNULL(MAX( CASE WHEN max_humidity < 2 THEN max_humidity*100 ELSE max_humidity END ),0) AS MAX_humidity  "
			+" FROM T_CALENDAR A LEFT OUTER JOIN TIPS_SENSOR_TEMP2 B "
			+" ON  A.T_DATE = TO_CHAR(B.DEVICE_TIME,'YYYYMMDD') "
			+" AND DEVICE_MAC_ADDR = ?1 "
			+" WHERE A.T_DATE BETWEEN REPLACE(?2,'.','') AND REPLACE(?3,'.','') "
			+" GROUP BY DEVICE_MAC_ADDR, TO_CHAR(device_time,'YYYY.MM.DD'), A.T_DATE order by 2 asc", nativeQuery = true)
	List<Object[]> selectSensorStatisticsListD(String device_id, String startDt, String endDt);
	
	@Query(value = "SELECT IFNULL(DEVICE_MAC_ADDR, ?1) AS DEVICE_MAC_ADDR"
			+"  , TO_CHAR(TO_DATE(t_date,'YYYYMMDD'),'YYYY.MM.DD') AS DEVICE_TIME "
			+"  , IFNULL(AVG( CASE WHEN avg_illuminace < 2 THEN avg_illuminace*100 ELSE avg_illuminace END ),0) AS AVG_ILLUMINACE  "
			+"  , IFNULL(MIN( CASE WHEN min_illuminace < 2 THEN min_illuminace*100 ELSE min_illuminace END ),0) AS MIN_ILLUMINACE  "
			+"  , IFNULL(MAX( CASE WHEN max_illuminace < 2 THEN max_illuminace*100 ELSE max_illuminace END ),0) AS MAX_ILLUMINACE  "
			+"  , IFNULL(AVG( CASE WHEN avg_temperature < 2 THEN avg_temperature*100 ELSE avg_temperature END ),0) AS AVG_temperature  "
			+"  , IFNULL(MIN( CASE WHEN min_temperature < 2 THEN min_temperature*100 ELSE min_temperature END ),0) AS MIN_temperature  "
			+"  , IFNULL(MAX( CASE WHEN max_temperature < 2 THEN max_temperature*100 ELSE max_temperature END ),0) AS MAX_temperature  "
			+"  , IFNULL(AVG( CASE WHEN avg_humidity < 2 THEN avg_humidity*100 ELSE avg_humidity END ),0) AS AVG_humidity  "
			+"  , IFNULL(MIN( CASE WHEN min_humidity < 2 THEN min_humidity*100 ELSE min_humidity END ),0) AS MIN_humidity  "
			+"  , IFNULL(MAX( CASE WHEN max_humidity < 2 THEN max_humidity*100 ELSE max_humidity END ),0) AS MAX_humidity  "
			+" FROM T_CALENDAR A LEFT OUTER JOIN TIPS_SENSOR_TEMP2 B "
			+" ON  A.T_DATE = TO_CHAR(B.DEVICE_TIME,'YYYYMMDD') "
			+" AND DEVICE_MAC_ADDR = ?1 "
			+" WHERE substring(A.T_DATE,0,6) = concat(?2,?3) "
			+" GROUP BY DEVICE_MAC_ADDR, TO_CHAR(device_time,'YYYY.MM.DD'), A.T_DATE order by 2 asc", nativeQuery = true)
	List<Object[]> selectSensorStatisticsListM(String device_id, String year, String month);
	
	@Query(value = "SELECT DEVICE_MAC_ADDR, DEVICE_TIME"
			+"  , SUM(AVG_ILLUMINACE) AS AVG_ILLUMINACE  "
			+"  , SUM(MIN_ILLUMINACE) AS MIN_ILLUMINACE  "
			+"  , SUM(MAX_ILLUMINACE) AS MAX_ILLUMINACE  "
			+"  , SUM(AVG_TEMPERATURE) AS AVG_TEMPERATURE  "
			+"  , SUM(MIN_TEMPERATURE) AS MIN_TEMPERATURE  "
			+"  , SUM(MAX_TEMPERATURE) AS MAX_TEMPERATURE  "
			+"  , SUM(AVG_HUMIDITY) AS AVG_HUMIDITY  "
			+"  , SUM(MIN_HUMIDITY) AS MIN_HUMIDITY  "
			+"  , SUM(MAX_HUMIDITY) AS MAX_HUMIDITY  "
			+" FROM (SELECT IFNULL(DEVICE_MAC_ADDR, ?1) AS DEVICE_MAC_ADDR"
			+"  , SUBSTRING(A.T_DATE,0,6) AS DEVICE_TIME "
			+"  , IFNULL(AVG( CASE WHEN avg_illuminace < 2 THEN avg_illuminace*100 ELSE avg_illuminace END ),0) AS AVG_ILLUMINACE  "
			+"  , IFNULL(MIN( CASE WHEN min_illuminace < 2 THEN min_illuminace*100 ELSE min_illuminace END ),0) AS MIN_ILLUMINACE  "
			+"  , IFNULL(MAX( CASE WHEN max_illuminace < 2 THEN max_illuminace*100 ELSE max_illuminace END ),0) AS MAX_ILLUMINACE  "
			+"  , IFNULL(AVG( CASE WHEN avg_temperature < 2 THEN avg_temperature*100 ELSE avg_temperature END ),0) AS AVG_temperature  "
			+"  , IFNULL(MIN( CASE WHEN min_temperature < 2 THEN min_temperature*100 ELSE min_temperature END ),0) AS MIN_temperature  "
			+"  , IFNULL(MAX( CASE WHEN max_temperature < 2 THEN max_temperature*100 ELSE max_temperature END ),0) AS MAX_temperature  "
			+"  , IFNULL(AVG( CASE WHEN avg_humidity < 2 THEN avg_humidity*100 ELSE avg_humidity END ),0) AS AVG_humidity  "
			+"  , IFNULL(MIN( CASE WHEN min_humidity < 2 THEN min_humidity*100 ELSE min_humidity END ),0) AS MIN_humidity  "
			+"  , IFNULL(MAX( CASE WHEN max_humidity < 2 THEN max_humidity*100 ELSE max_humidity END ),0) AS MAX_humidity  "
			+" FROM T_CALENDAR A LEFT OUTER JOIN TIPS_SENSOR_TEMP2 B "
			+" ON  A.T_DATE = TO_CHAR(B.DEVICE_TIME,'YYYYMMDD') "
			+" AND DEVICE_MAC_ADDR = ?1 "
			+" WHERE substring(A.T_DATE,0,4) = ?2 "
			+" GROUP BY DEVICE_MAC_ADDR, SUBSTRING(A.T_DATE,0,6) order by 2 asc)"
			+" GROUP BY DEVICE_MAC_ADDR, DEVICE_TIME", nativeQuery = true)
	List<Object[]> selectSensorStatisticsListY(String device_id, String year);
}