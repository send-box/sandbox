package kr.co.tipsvalley.sapsa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.tipsvalley.sapsa.model.db.ElevatorBreakdownStatusEntity;

/*
 * JPA elevator BreakdownStatus repository.
 */
@Repository
public interface ElevatorBreakdownStatusRepository extends JpaRepository<ElevatorBreakdownStatusEntity, Integer> {
	
	
	@Query(value = "SELECT MAX(DEVICE_TIME) AS DEVICE_TIME, DEVICE_ID, COMPONENT_ID  FROM BREAKDOWN_STATUS WHERE DEVICE_ID = ?1 AND COMPONENT_ID = ?2 AND BREAKDOWN_TIME = 0 GROUP BY DEVICE_ID, COMPONENT_ID", nativeQuery = true)
	List<Object[]> findBreakLastData(int deviceId, int component_id);
	
	@Query(value = "SELECT B.COMPONENT_ID, B.COMPONENT_NM, B.MANUFACTURER, B.SELLER, TO_CHAR(DEVICE_TIME, 'YYYY-MM-DD HH:MI:SS') AS BREAK_TIME, IFNULL(SECONDS_BETWEEN(DEVICE_TIME, ADD_SECONDS (CURRENT_TIMESTAMP, 90)),0) AS DOWN_TIME FROM BREAKDOWN_STATUS A, COMPONENT_INFO B\n" + 
			"	WHERE A.COMPONENT_ID = B.COMPONENT_ID\n" + 
			"	AND BREAKDOWN_TIME = 0\n" + 
			"	AND A.DEVICE_ID = ?1\n" + 
			"	AND A.BREAKDOWN_CNT = 0\n" + 
			"	ORDER BY 4 DESC", nativeQuery = true)
	List<Object[]> findBreakComponentListByDeviceId(int deviceId);
}