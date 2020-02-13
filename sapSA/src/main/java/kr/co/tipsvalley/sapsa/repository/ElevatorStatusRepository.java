package kr.co.tipsvalley.sapsa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.tipsvalley.sapsa.model.db.ElevatorStatusEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorStatusIdEntity;

/*
 * JPA elevator ElevatorMonitoring repository.
 */
@Repository
public interface ElevatorStatusRepository extends JpaRepository<ElevatorStatusEntity, ElevatorStatusIdEntity> {

	/**
	 * 실시간 엘리베이터 고장 목록
	 * @return
	 */
	@Query(value = "SELECT\n" + 
			"        BUILDING_NM,\n" + 
			"        ELEVATOR_NO,\n" + 
			"        OPERATION_CORP,\n" + 
			"        ADDRESS,\n" + 
			"        OPERATION_MANAGER,\n" + 
			"        OPERATION_ADMIN,\n" + 
			"        BREAK_TIME,\n" + 
			"        ADD_SECONDS (CURRENT_TIMESTAMP,\n" + 
			"        90) AS CURRENT,\n" + 
			"        SECONDS_BETWEEN(BREAK_TIME,\n" + 
			"        ADD_SECONDS (CURRENT_TIMESTAMP,\n" + 
			"        90)) BT,\n" + 
			"        CNT,\n" + 
			"        A.DEVICE_ID  \n" + 
			"    FROM\n" + 
			"        DEVICE_INFO A,\n" + 
			"        ( SELECT\n" + 
			"            DEVICE_ID,\n" + 
			"            MIN(BREAK_TIME) AS BREAK_TIME,\n" + 
			"            COUNT(*) AS CNT \n" + 
			"        FROM\n" + 
			"            DEVICE_MONITORING \n" + 
			"        WHERE\n" + 
			"            BREAK_TIME IS NOT NULL \n" + 
			"        GROUP BY\n" + 
			"            DEVICE_ID  ) B \n" + 
			"    WHERE\n" + 
			"        A.DEVICE_ID = B.DEVICE_ID \n" + 
			"    ORDER BY\n" + 
			"        BREAK_TIME DESC", nativeQuery = true)
	List<Object[]> selectElevatorInfo();

	/**
	 * 실시간 엘리베이터 고장 목록 (차트용)
	 * @return
	 */
	@Query(value = "SELECT\n" + 
			"        BUILDING_NM||' '||ELEVATOR_NO||' 호기' AS BUILDING_NM,\n" + 
			"        ELEVATOR_NO,\n" + 
			"        OPERATION_CORP,\n" + 
			"        ADDRESS,\n" + 
			"        OPERATION_MANAGER,\n" + 
			"        OPERATION_ADMIN,\n" + 
			"        BREAK_TIME,\n" + 
			"        ADD_SECONDS (CURRENT_TIMESTAMP,\n" + 
			"        90) AS CURRENT,\n" + 
			"        ifnull(SECONDS_BETWEEN(BREAK_TIME,\n" + 
			"        ADD_SECONDS (CURRENT_TIMESTAMP,\n" + 
			"        90)),\n" + 
			"        0) BT,\n" + 
			"        ifnull(CNT,\n" + 
			"        0) AS CNT,\n" + 
			"        A.DEVICE_ID  \n" + 
			"    FROM\n" + 
			"        DEVICE_INFO A \n" + 
			"    LEFT OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                MIN(BREAK_TIME) AS BREAK_TIME,\n" + 
			"                COUNT(*) AS CNT \n" + 
			"            FROM\n" + 
			"                DEVICE_MONITORING \n" + 
			"            WHERE\n" + 
			"                BREAK_TIME IS NOT NULL \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID  \n" + 
			"        ) B \n" + 
			"            ON A.DEVICE_ID = B.DEVICE_ID \n" + 
			"    ORDER BY\n" + 
			"        A.DEVICE_ID ASC", nativeQuery = true)
	List<Object[]> selectElevatorInfo2();

	/**
	 * 엘리베이터 건물별 목록
	 * @return
	 */
	@Query(value = "    select\n" + 
			"        '%' as key,\n" + 
			"        '-전체-' as value \n" + 
			"    from\n" + 
			"        dummy \n" + 
			"    union\n" + 
			"    select\n" + 
			"        building_cd as key,\n" + 
			"        building_nm as value \n" + 
			"    from\n" + 
			"        device_info \n" + 
			"    order by\n" + 
			"        2", nativeQuery = true)
	List<Object[]> selectDeviceType1();

	/**
	 * 엘리베이터 운영사별 목록
	 * @return
	 */
	@Query(value = " SELECT\n" + 
			"        '-전체-' AS VALUE \n" + 
			"    FROM\n" + 
			"        DUMMY \n" + 
			"    UNION\n" + 
			"    SELECT\n" + 
			"        DISTINCT(OPERATION_CORP) AS DEVICE_VALUE \n" + 
			"    FROM\n" + 
			"        DEVICE_INFO \n" + 
			"    ORDER BY\n" + 
			"        1", nativeQuery = true)
	List<String> selectDeviceType2();
	
	/**
	 * 엘리베이터 부품별 목록
	 * @return
	 */
	@Query(value = "    select\n" + 
			"        '%' as key,\n" + 
			"        '-전체-' as value \n" + 
			"    from\n" + 
			"        dummy \n" + 
			"    union\n" + 
			"    select\n" + 
			"        to_char(component_id) as key,\n" + 
			"        component_nm as value \n" + 
			"    from\n" + 
			"        component_info \n" + 
			"    order by\n" + 
			"        1", nativeQuery = true)
	List<Object[]> selectDeviceType3();

	/**
	 * 엘리베이터 건물별 & 일별 통계 리스트
	 * @param deviceId	
	 * @param startDt
	 * @param endDt
	 * @return
	 */
	@Query(value = "SELECT BUILDING_NM, ELEVATOR_NO, IFNULL(SUM(BREAKDOWN_CNT),0) AS BC,\n" + "\n"
			+ "	IFNULL(SUM(BREAKDOWN_TIME),0) AS BT, OPERATION_MANAGER, OPERATION_ADMIN\n"
			+ "	FROM DEVICE_INFO A LEFT\n" + "\n" + "	OUTER JOIN\n" + "	(\n"
			+ "	SELECT DEVICE_ID, SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT FROM CURRENT_BREAKDOWN_STATUS \n" + "\n"
			+ "	WHERE BREAKDOWN_DT BETWEEN REPLACE(?2,'.','') AND REPLACE(?3,'.','')\n" + "	GROUP BY DEVICE_ID \n"
			+ "	) B\n" + "	ON A.DEVICE_ID = B.DEVICE_ID\n" + "	LEFT OUTER JOIN \n"
			+ "	(SELECT DEVICE_ID, SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME FROM BREAKDOWN_STATUS\n" + "\n"
			+ "	WHERE TO_CHAR(DEVICE_TIME,'YYYYMMDD') BETWEEN REPLACE(?2,'.','') AND REPLACE(?3,'.','')\n"
			+ "	GROUP BY DEVICE_ID) C\n" + "	ON A.DEVICE_ID = C.DEVICE_ID\n" + "   WHERE contains(building_cd, ?1)"
			+ "	GROUP BY BUILDING_NM,ELEVATOR_NO, OPERATION_MANAGER, OPERATION_ADMIN", nativeQuery = true)
	List<Object[]> selectEvStsBuildingOfDayList(String deviceId, String startDt, String endDt);
	
	/**
	 * 엘리베이터 건물별 & 일별 통계 차트 리스트
	 * @param deviceId
	 * @param startDt
	 * @param endDt
	 * @return
	 */
	@Query(value = " SELECT\n" + 
			"        A.BUILDING_NM,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        A.T_DATE\n" + 
			"    FROM\n" + 
			"        (\n" + 
			"        SELECT TC.T_DATE, DI.*\n" + 
			"        FROM T_CALENDAR TC LEFT OUTER JOIN DEVICE_INFO DI\n" + 
			"        on 1=1\n" + 
			"        WHERE  TC.T_DATE BETWEEN REPLACE(?2,'.','') AND REPLACE(?3,'.','')\n" + 
			"        ) A \n" + 
			"    LEFT   OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT, \n" + 
			"                BREAKDOWN_DT\n" + 
			"            FROM\n" + 
			"                CURRENT_BREAKDOWN_STATUS    \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID,BREAKDOWN_DT   \n" + 
			"        ) B  \n" + 
			"            ON A.DEVICE_ID = B.DEVICE_ID \n" + 
			"            AND A.T_DATE = B.BREAKDOWN_DT \n" + 
			"    LEFT OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,\n" + 
			"                0)) AS BREAKDOWN_TIME,\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMMDD') BREAKDOWN_DT \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID, TO_CHAR(DEVICE_TIME,'YYYYMMDD')\n" + 
			"        ) C  \n" + 
			"            ON A.DEVICE_ID = C.DEVICE_ID  \n" + 
			"             AND A.T_DATE = C.BREAKDOWN_DT  \n" + 
			"    WHERE\n" + 
			"        A.building_cd like ?1 \n" + 
			"    GROUP BY\n" + 
			"        A.BUILDING_NM, A.T_DATE\n" + 
			"    ORDER BY\n" + 
			"        A.T_DATE\n" + 
			"", nativeQuery = true)
	List<Object[]> selectEvStsBuildingOfDayChart(String deviceId, String startDt, String endDt);

	/**
	 * 엘리베이터 건물별 & 월별 통계 리스트
	 * @param device_id
	 * @param year
	 * @param month
	 * @return
	 */
	@Query(value = "SELECT BUILDING_NM, ELEVATOR_NO, IFNULL(SUM(BREAKDOWN_CNT),0) AS BC,\n" + "\n"
			+ "	IFNULL(SUM(BREAKDOWN_TIME),0) AS BT, OPERATION_MANAGER, OPERATION_ADMIN\n"
			+ "	FROM DEVICE_INFO A LEFT\n" + "\n" + "	OUTER JOIN\n" + "	(\n"
			+ "	SELECT DEVICE_ID, SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT FROM CURRENT_BREAKDOWN_STATUS \n" + "\n"
			+ "	WHERE SUBSTR(BREAKDOWN_DT,0,6) = concat(?2,?3)\n" + "	GROUP BY DEVICE_ID \n" + "	) B\n"
			+ "	ON A.DEVICE_ID = B.DEVICE_ID\n" + "	LEFT OUTER JOIN \n"
			+ "	(SELECT DEVICE_ID, SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME FROM BREAKDOWN_STATUS\n" + "\n"
			+ "	WHERE TO_CHAR(DEVICE_TIME,'YYYYMM') = concat(?2,?3) \n" + "	GROUP BY DEVICE_ID) C\n"
			+ "	ON A.DEVICE_ID = C.DEVICE_ID\n" + "   WHERE contains(building_cd, ?1)"
			+ "	GROUP BY BUILDING_NM,ELEVATOR_NO, OPERATION_MANAGER, OPERATION_ADMIN", nativeQuery = true)
	List<Object[]> selectEvStsBuildingOfMonthList(String device_id, String year, String month);
	
	/**
	 * 엘리베이터 건물별 & 월별 통계 차트 리스트
	 * @param deviceId
	 * @param year
	 * @param month
	 * @return
	 */
	@Query(value = " SELECT\n" + 
			"        A.BUILDING_NM,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        A.T_DATE\n" + 
			"    FROM\n" + 
			"        (\n" + 
			"        SELECT TC.T_DATE, DI.*\n" + 
			"        FROM T_CALENDAR TC LEFT OUTER JOIN DEVICE_INFO DI\n" + 
			"        on 1=1\n" + 
			"        WHERE  SUBSTR(TC.T_DATE,0,6) = CONCAT(?2,?3)\n" + 
			"        ) A \n" + 
			"    LEFT   OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT, \n" + 
			"                BREAKDOWN_DT\n" + 
			"            FROM\n" + 
			"                CURRENT_BREAKDOWN_STATUS    \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID,BREAKDOWN_DT   \n" + 
			"        ) B  \n" + 
			"            ON A.DEVICE_ID = B.DEVICE_ID \n" + 
			"            AND A.T_DATE = B.BREAKDOWN_DT \n" + 
			"    LEFT OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,\n" + 
			"                0)) AS BREAKDOWN_TIME,\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMMDD') BREAKDOWN_DT \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID, TO_CHAR(DEVICE_TIME,'YYYYMMDD')\n" + 
			"        ) C  \n" + 
			"            ON A.DEVICE_ID = C.DEVICE_ID  \n" + 
			"             AND A.T_DATE = C.BREAKDOWN_DT  \n" + 
			"    WHERE\n" + 
			"        A.building_cd like ?1 \n" + 
			"    GROUP BY\n" + 
			"        A.BUILDING_NM, A.T_DATE\n" + 
			"    ORDER BY\n" + 
			"        A.T_DATE\n" + 
			"", nativeQuery = true)
	List<Object[]> selectEvStsBuildingOfMonthChart(String deviceId, String year, String month);

	/**
	 * 엘리베이터 건물별 & 년별 통계 리스트
	 * @param device_id
	 * @param year
	 * @return
	 */
	@Query(value = "SELECT BUILDING_NM, ELEVATOR_NO, IFNULL(SUM(BREAKDOWN_CNT),0) AS BC,\n" + "\n"
			+ "	IFNULL(SUM(BREAKDOWN_TIME),0) AS BT, OPERATION_MANAGER, OPERATION_ADMIN\n"
			+ "	FROM DEVICE_INFO A LEFT\n" + "\n" + "	OUTER JOIN\n" + "	(\n"
			+ "	SELECT DEVICE_ID, SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT FROM CURRENT_BREAKDOWN_STATUS \n" + "\n"
			+ "	WHERE SUBSTR(BREAKDOWN_DT,0,4) = ?2\n" + "	GROUP BY DEVICE_ID \n" + "	) B\n"
			+ "	ON A.DEVICE_ID = B.DEVICE_ID\n" + "	LEFT OUTER JOIN \n"
			+ "	(SELECT DEVICE_ID, SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME FROM BREAKDOWN_STATUS\n" + "\n"
			+ "	WHERE TO_CHAR(DEVICE_TIME,'YYYY') = ?2\n" + "	GROUP BY DEVICE_ID) C\n"
			+ "	ON A.DEVICE_ID = C.DEVICE_ID\n" + "   WHERE contains(building_cd, ?1)"
			+ "	GROUP BY BUILDING_NM,ELEVATOR_NO, OPERATION_MANAGER, OPERATION_ADMIN", nativeQuery = true)
	List<Object[]> selectEvStsBuildingOfYearList(String device_id, String year);
	
	/**
	 * 엘리베이터 건물별 & 년별 통계 차트 리스트
	 * @param device_id
	 * @param year
	 * @return
	 */
	@Query(value = " SELECT\n" + 
			"        A.BUILDING_NM,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        A.T_DATE\n" + 
			"    FROM\n" + 
			"        (\n" + 
			"        SELECT SUBSTR(TC.T_DATE,0,6) AS T_DATE,  DI.BUILDING_NM, DI.BUILDING_CD, DI.DEVICE_ID  \n" + 
			"        FROM T_CALENDAR TC LEFT OUTER JOIN DEVICE_INFO DI\n" + 
			"        on 1=1\n" + 
			"        WHERE  SUBSTR(TC.T_DATE,0,4) = ?2\n" + 
			"        GROUP BY SUBSTR(TC.T_DATE,0,6),  DI.BUILDING_NM, DI.BUILDING_CD, DI.DEVICE_ID   \n" + 
			"        ) A \n" + 
			"    LEFT   OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT, \n" + 
			"                SUBSTR(BREAKDOWN_DT,0,6) AS BREAKDOWN_DT\n" + 
			"            FROM\n" + 
			"                CURRENT_BREAKDOWN_STATUS    \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID, SUBSTR(BREAKDOWN_DT,0,6)   \n" + 
			"        ) B  \n" + 
			"            ON A.DEVICE_ID = B.DEVICE_ID \n" + 
			"            AND A.T_DATE = B.BREAKDOWN_DT \n" + 
			"    LEFT OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,\n" + 
			"                0)) AS BREAKDOWN_TIME,\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMM') BREAKDOWN_DT \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID, TO_CHAR(DEVICE_TIME,'YYYYMM')\n" + 
			"        ) C  \n" + 
			"            ON A.DEVICE_ID = C.DEVICE_ID  \n" + 
			"             AND A.T_DATE = C.BREAKDOWN_DT  \n" + 
			"    WHERE\n" + 
			"        A.building_cd like ?1 \n" + 
			"    GROUP BY\n" + 
			"        A.BUILDING_NM, A.T_DATE\n" + 
			"    ORDER BY\n" + 
			"        A.T_DATE\n" + 
			"", nativeQuery = true)
	List<Object[]> selectEvStsBuildingOfYearChart(String device_id, String year);

	/**
	 * 엘리베이터 운영사별 & 일별 통계 리스트
	 * @param deviceId
	 * @param startDt
	 * @param endDt
	 * @return
	 */
	@Query(value = "SELECT OPERATION_CORP, ELEVATOR_NO, IFNULL(SUM(BREAKDOWN_CNT),0) AS BC,\n" + "\n"
			+ "	IFNULL(SUM(BREAKDOWN_TIME),0) AS BT, OPERATION_MANAGER, OPERATION_ADMIN\n"
			+ "	FROM DEVICE_INFO A LEFT\n" + "\n" + "	OUTER JOIN\n" + "	(\n"
			+ "	SELECT DEVICE_ID, SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT FROM CURRENT_BREAKDOWN_STATUS \n" + "\n"
			+ "	WHERE BREAKDOWN_DT BETWEEN REPLACE(?2,'.','') AND REPLACE(?3,'.','')\n" + "	GROUP BY DEVICE_ID \n"
			+ "	) B\n" + "	ON A.DEVICE_ID = B.DEVICE_ID\n" + "	LEFT OUTER JOIN \n"
			+ "	(SELECT DEVICE_ID, SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME FROM BREAKDOWN_STATUS\n" + "\n"
			+ "	WHERE TO_CHAR(DEVICE_TIME,'YYYYMMDD') BETWEEN REPLACE(?2,'.','') AND REPLACE(?3,'.','')\n"
			+ "	GROUP BY DEVICE_ID) C\n" + "	ON A.DEVICE_ID = C.DEVICE_ID\n" + "   WHERE contains(OPERATION_CORP, ?1)"
			+ "	GROUP BY OPERATION_CORP,ELEVATOR_NO, OPERATION_MANAGER, OPERATION_ADMIN", nativeQuery = true)
	List<Object[]> selectEvStsOperationCorpOfDayList(String deviceId, String startDt, String endDt);
	

	/**
	 * 엘리베이터 운영사별 & 일별 통계 차트 리스트
	 * @param deviceId
	 * @param startDt
	 * @param endDt
	 * @return
	 */
	@Query(value = " SELECT\n" + 
			"        A.OPERATION_CORP,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        A.T_DATE\n" + 
			"    FROM\n" + 
			"        (\n" + 
			"        SELECT TC.T_DATE, DI.*\n" + 
			"        FROM T_CALENDAR TC LEFT OUTER JOIN DEVICE_INFO DI\n" + 
			"        on 1=1\n" + 
			"        WHERE  TC.T_DATE BETWEEN REPLACE(?2,'.','') AND REPLACE(?3,'.','')\n" + 
			"        ) A \n" + 
			"    LEFT   OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT, \n" + 
			"                BREAKDOWN_DT\n" + 
			"            FROM\n" + 
			"                CURRENT_BREAKDOWN_STATUS    \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID,BREAKDOWN_DT   \n" + 
			"        ) B  \n" + 
			"            ON A.DEVICE_ID = B.DEVICE_ID \n" + 
			"            AND A.T_DATE = B.BREAKDOWN_DT \n" + 
			"    LEFT OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,\n" + 
			"                0)) AS BREAKDOWN_TIME,\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMMDD') BREAKDOWN_DT \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID, TO_CHAR(DEVICE_TIME,'YYYYMMDD')\n" + 
			"        ) C  \n" + 
			"            ON A.DEVICE_ID = C.DEVICE_ID  \n" + 
			"             AND A.T_DATE = C.BREAKDOWN_DT  \n" + 
			"    WHERE\n" + 
			"       OPERATION_CORP like  ?1 \n" + 
			"    GROUP BY\n" + 
			"        A.OPERATION_CORP, A.T_DATE\n" + 
			"    ORDER BY\n" + 
			"        A.T_DATE\n" + 
			"", nativeQuery = true)
	List<Object[]> selectEvStsOperationCorpOfDayChart(String deviceId, String startDt, String endDt);

	/**
	 * 엘리베이터 운영사별 & 월별 통계 리스트
	 * @param device_id
	 * @param year
	 * @param month
	 * @return
	 */
	@Query(value = "SELECT OPERATION_CORP, ELEVATOR_NO, IFNULL(SUM(BREAKDOWN_CNT),0) AS BC,\n" + "\n"
			+ "	IFNULL(SUM(BREAKDOWN_TIME),0) AS BT, OPERATION_MANAGER, OPERATION_ADMIN\n"
			+ "	FROM DEVICE_INFO A LEFT\n" + "\n" + "	OUTER JOIN\n" + "	(\n"
			+ "	SELECT DEVICE_ID, SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT FROM CURRENT_BREAKDOWN_STATUS \n" + "\n"
			+ "	WHERE SUBSTR(BREAKDOWN_DT,0,6) = concat(?2,?3)\n" + "	GROUP BY DEVICE_ID \n" + "	) B\n"
			+ "	ON A.DEVICE_ID = B.DEVICE_ID\n" + "	LEFT OUTER JOIN \n"
			+ "	(SELECT DEVICE_ID, SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME FROM BREAKDOWN_STATUS\n" + "\n"
			+ "	WHERE TO_CHAR(DEVICE_TIME,'YYYYMM') = concat(?2,?3) \n" + "	GROUP BY DEVICE_ID) C\n"
			+ "	ON A.DEVICE_ID = C.DEVICE_ID\n" + "   WHERE contains(building_cd, ?1)"
			+ "	GROUP BY OPERATION_CORP,ELEVATOR_NO, OPERATION_MANAGER, OPERATION_ADMIN", nativeQuery = true)
	List<Object[]> selectEvStsOperationCorpOfMonthList(String device_id, String year, String month);
	

	/**
	 * 엘리베이터 운영사별 & 월별 통계 차트 리스트
	 * @param deviceId
	 * @param startDt
	 * @param endDt
	 * @return
	 */
	@Query(value = " SELECT\n" + 
			"        A.OPERATION_CORP,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        A.T_DATE\n" + 
			"    FROM\n" + 
			"        (\n" + 
			"        SELECT TC.T_DATE, DI.*\n" + 
			"        FROM T_CALENDAR TC LEFT OUTER JOIN DEVICE_INFO DI\n" + 
			"        on 1=1\n" + 
			"        WHERE  SUBSTR(TC.T_DATE,0,6) = CONCAT(?2,?3)\n" + 
			"        ) A \n" + 
			"    LEFT   OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT, \n" + 
			"                BREAKDOWN_DT\n" + 
			"            FROM\n" + 
			"                CURRENT_BREAKDOWN_STATUS    \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID,BREAKDOWN_DT   \n" + 
			"        ) B  \n" + 
			"            ON A.DEVICE_ID = B.DEVICE_ID \n" + 
			"            AND A.T_DATE = B.BREAKDOWN_DT \n" + 
			"    LEFT OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,\n" + 
			"                0)) AS BREAKDOWN_TIME,\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMMDD') BREAKDOWN_DT \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID, TO_CHAR(DEVICE_TIME,'YYYYMMDD')\n" + 
			"        ) C  \n" + 
			"            ON A.DEVICE_ID = C.DEVICE_ID  \n" + 
			"             AND A.T_DATE = C.BREAKDOWN_DT  \n" + 
			"    WHERE\n" + 
			"        A.OPERATION_CORP like ?1 \n" + 
			"    GROUP BY\n" + 
			"        A.OPERATION_CORP, A.T_DATE\n" + 
			"    ORDER BY\n" + 
			"        A.T_DATE\n" + 
			"", nativeQuery = true)
	List<Object[]> selectEvStsOperationCorpOfMonthChart(String deviceId, String startDt, String endDt);

	/**
	 * 엘리베이터 운영사별 & 년별 통계 리스트
	 * @param device_id
	 * @param year
	 * @return
	 */
	@Query(value = "SELECT OPERATION_CORP, ELEVATOR_NO, IFNULL(SUM(BREAKDOWN_CNT),0) AS BC,\n" + "\n"
			+ "	IFNULL(SUM(BREAKDOWN_TIME),0) AS BT, OPERATION_MANAGER, OPERATION_ADMIN\n"
			+ "	FROM DEVICE_INFO A LEFT\n" + "\n" + "	OUTER JOIN\n" + "	(\n"
			+ "	SELECT DEVICE_ID, SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT FROM CURRENT_BREAKDOWN_STATUS \n" + "\n"
			+ "	WHERE SUBSTR(BREAKDOWN_DT,0,4) = ?2\n" + "	GROUP BY DEVICE_ID \n" + "	) B\n"
			+ "	ON A.DEVICE_ID = B.DEVICE_ID\n" + "	LEFT OUTER JOIN \n"
			+ "	(SELECT DEVICE_ID, SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME FROM BREAKDOWN_STATUS\n" + "\n"
			+ "	WHERE TO_CHAR(DEVICE_TIME,'YYYY') = ?2\n" + "	GROUP BY DEVICE_ID) C\n"
			+ "	ON A.DEVICE_ID = C.DEVICE_ID\n" + "   WHERE contains(building_cd, ?1)"
			+ "	GROUP BY OPERATION_CORP,ELEVATOR_NO, OPERATION_MANAGER, OPERATION_ADMIN", nativeQuery = true)
	List<Object[]> selectEvStsOperationCorpOfYearList(String device_id, String year);

	/**
	 * 엘리베이터 운영사별 & 년별 통계 차트 리스트
	 * @param deviceId
	 * @param year
	 * @return
	 */
	@Query(value = " SELECT\n" + 
			"        A.OPERATION_CORP,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        A.T_DATE\n" + 
			"    FROM\n" + 
			"        (\n" + 
			"        SELECT SUBSTR(TC.T_DATE,0,6) AS T_DATE, DI.OPERATION_CORP, DI.DEVICE_ID \n" + 
			"        FROM T_CALENDAR TC LEFT OUTER JOIN DEVICE_INFO DI\n" + 
			"        on 1=1\n" + 
			"        WHERE  SUBSTR(TC.T_DATE,0,4) = ?2\n" + 
			"        GROUP BY SUBSTR(TC.T_DATE,0,6), DI.OPERATION_CORP, DI.DEVICE_ID \n" + 
			"        ) A \n" + 
			"    LEFT   OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(BREAKDOWN_CNT) AS BREAKDOWN_CNT, \n" + 
			"                SUBSTR(BREAKDOWN_DT,0,6) AS BREAKDOWN_DT\n" + 
			"            FROM\n" + 
			"                CURRENT_BREAKDOWN_STATUS    \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID,SUBSTR(BREAKDOWN_DT,0,6)   \n" + 
			"        ) B  \n" + 
			"            ON A.DEVICE_ID = B.DEVICE_ID \n" + 
			"            AND A.T_DATE = B.BREAKDOWN_DT \n" + 
			"    LEFT OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                DEVICE_ID,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,\n" + 
			"                0)) AS BREAKDOWN_TIME,\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMM') BREAKDOWN_DT \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            GROUP BY\n" + 
			"                DEVICE_ID, TO_CHAR(DEVICE_TIME,'YYYYMM')\n" + 
			"        ) C  \n" + 
			"            ON A.DEVICE_ID = C.DEVICE_ID  \n" + 
			"             AND A.T_DATE = C.BREAKDOWN_DT  \n" + 
			"    WHERE\n" + 
			"        A.OPERATION_CORP like ?1 \n" + 
			"    GROUP BY\n" + 
			"        A.OPERATION_CORP, A.T_DATE\n" + 
			"    ORDER BY\n" + 
			"        A.T_DATE\n" + 
			"", nativeQuery = true)
	List<Object[]> selectEvStsOperationCorpOfYearChart(String deviceId, String year);
	
	/**
	 * 엘리베이터 부품별 & 일별 통계 리스트
	 * @param deviceId
	 * @param startDt
	 * @param endDt
	 * @return
	 */
	@Query(value = "SELECT\n" + 
			"        A.COMPONENT_ID||COMPONENT_NM,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        MANUFACTURER,\n" + 
			"        SELLER  \n" + 
			"    FROM\n" + 
			"        COMPONENT_INFO A \n" + 
			"    LEFT OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                COMPONENT_ID,\n" + 
			"                COUNT(*) AS BREAKDOWN_CNT,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            WHERE\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMMDD') BETWEEN REPLACE(?2,'.','') AND REPLACE(?3,'.','')  \n" + 
			"            GROUP BY\n" + 
			"                COMPONENT_ID\n" + 
			"        ) C  \n" + 
			"            ON A.COMPONENT_ID = C.COMPONENT_ID    \n" + 
			"    WHERE\n" + 
			"        TO_CHAR(A.COMPONENT_ID) like ?1   \n" + 
			"    GROUP BY\n" + 
			"        A.COMPONENT_ID||COMPONENT_NM,\n" + 
			"        MANUFACTURER,\n" + 
			"        SELLER", nativeQuery = true)
	List<Object[]> selectEvStsComponentOfDayList(String deviceId, String startDt, String endDt);
	

	/**
	 * 엘리베이터 부품별 & 일별 차트 통계 리스트
	 * @param deviceId
	 * @param startDt
	 * @param endDt
	 * @return
	 */
	@Query(value = " SELECT\n" + 
			"        A.COMPONENT_ID||A.COMPONENT_NM,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        A.T_DATE,\n" + 
			"        A.COMPONENT_ID\n" + 
			"    FROM\n" + 
			"        (\n" + 
			"        SELECT TC.T_DATE, DI.*\n" + 
			"        FROM T_CALENDAR TC LEFT OUTER JOIN COMPONENT_INFO DI\n" + 
			"        on 1=1\n" + 
			"        WHERE  TC.T_DATE BETWEEN REPLACE(?2,'.','') AND REPLACE(?3,'.','')\n" + 
			"        ) A \n" + 
			"    LEFT   OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                COMPONENT_ID,\n" + 
			"                COUNT(*) AS BREAKDOWN_CNT,\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMMDD') AS BREAKDOWN_DT,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            WHERE\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMMDD') BETWEEN REPLACE(?2,'.','') AND REPLACE(?3,'.','')  \n" + 
			"            GROUP BY\n" + 
			"                COMPONENT_ID, TO_CHAR(DEVICE_TIME,'YYYYMMDD')\n" + 
			"        ) C  \n" + 
			"            ON A.COMPONENT_ID = C.COMPONENT_ID    \n" + 
			"             AND A.T_DATE = C.BREAKDOWN_DT  \n" + 
			"    WHERE\n" + 
			"       TO_CHAR(A.COMPONENT_ID) like ?1 \n" + 
			"    GROUP BY\n" + 
			"        A.COMPONENT_ID||A.COMPONENT_NM, A.T_DATE, A.COMPONENT_ID\n" + 
			"    ORDER BY\n" + 
			"        A.T_DATE,A.COMPONENT_ID\n" + 
			"", nativeQuery = true)
	List<Object[]> selectEvStsComponentOfDayChart(String deviceId, String startDt, String endDt);

	/**
	 * 엘리베이터 부품별 & 월별 통계 리스트
	 * @param device_id
	 * @param year
	 * @param month
	 * @return
	 */
	@Query(value = "SELECT\n" + 
			"        A.COMPONENT_ID||COMPONENT_NM,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        MANUFACTURER,\n" + 
			"        SELLER  \n" + 
			"    FROM\n" + 
			"        COMPONENT_INFO A \n" + 
			"    LEFT OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                COMPONENT_ID,\n" + 
			"                COUNT(*) AS BREAKDOWN_CNT,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            WHERE\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMM') = CONCAT(?2,?3) \n" + 
			"            GROUP BY\n" + 
			"                COMPONENT_ID\n" + 
			"        ) C  \n" + 
			"            ON A.COMPONENT_ID = C.COMPONENT_ID    \n" + 
			"    WHERE\n" + 
			"        TO_CHAR(A.COMPONENT_ID) like ?1   \n" + 
			"    GROUP BY\n" + 
			"        A.COMPONENT_ID||COMPONENT_NM,\n" + 
			"        MANUFACTURER,\n" + 
			"        SELLER", nativeQuery = true)
	List<Object[]> selectEvStsComponentOfMonthList(String device_id, String year, String month);

	/**
	 * 엘리베이터 부품별 & 월별 차트 통계 리스트
	 * @param deviceId
	 * @param year
	 * @param month
	 * @return
	 */
	@Query(value = " SELECT\n" + 
			"        A.COMPONENT_ID||A.COMPONENT_NM,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        A.T_DATE,\n" + 
			"        A.COMPONENT_ID\n" + 
			"    FROM\n" + 
			"        (\n" + 
			"        SELECT TC.T_DATE, DI.*\n" + 
			"        FROM T_CALENDAR TC LEFT OUTER JOIN COMPONENT_INFO DI\n" + 
			"        on 1=1\n" + 
			"        WHERE  TO_CHAR(TC.T_DATE,'YYYYMM') = CONCAT(?2,?3)\n" + 
			"        ) A \n" + 
			"    LEFT   OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                COMPONENT_ID,\n" + 
			"                COUNT(*) AS BREAKDOWN_CNT,\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMMDD') AS BREAKDOWN_DT,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            WHERE\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMM') = CONCAT(?2,?3)  \n" + 
			"            GROUP BY\n" + 
			"                COMPONENT_ID, TO_CHAR(DEVICE_TIME,'YYYYMMDD')\n" + 
			"        ) C  \n" + 
			"            ON A.COMPONENT_ID = C.COMPONENT_ID    \n" + 
			"             AND A.T_DATE = C.BREAKDOWN_DT  \n" + 
			"    WHERE\n" + 
			"       TO_CHAR(A.COMPONENT_ID) like ?1 \n" + 
			"    GROUP BY\n" + 
			"        A.COMPONENT_ID||A.COMPONENT_NM, A.T_DATE, A.COMPONENT_ID\n" + 
			"    ORDER BY\n" + 
			"        A.T_DATE,A.COMPONENT_ID\n" + 
			"", nativeQuery = true)
	List<Object[]> selectEvStsComponentOfMonthChart(String deviceId, String year, String month);
	
	/**
	 * 엘리베이터 부품별 & 년별 통계 리스트
	 * @param device_id
	 * @param year
	 * @return
	 */
	@Query(value = "SELECT\n" + 
			"        A.COMPONENT_ID||COMPONENT_NM,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        MANUFACTURER,\n" + 
			"        SELLER  \n" + 
			"    FROM\n" + 
			"        COMPONENT_INFO A \n" + 
			"    LEFT OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                COMPONENT_ID,\n" + 
			"                COUNT(*) AS BREAKDOWN_CNT,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            WHERE\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYY') = ?2 \n" + 
			"            GROUP BY\n" + 
			"                COMPONENT_ID\n" + 
			"        ) C  \n" + 
			"            ON A.COMPONENT_ID = C.COMPONENT_ID    \n" + 
			"    WHERE\n" + 
			"        TO_CHAR(A.COMPONENT_ID) like ?1   \n" + 
			"    GROUP BY\n" + 
			"        A.COMPONENT_ID||COMPONENT_NM,\n" + 
			"        MANUFACTURER,\n" + 
			"        SELLER", nativeQuery = true)
	List<Object[]> selectEvStsComponentOfYearList(String device_id, String year);

	/**
	 * 엘리베이터 부품별 & 년별 차트 통계 리스트
	 * @param deviceId
	 * @param year
	 * @return
	 */
	@Query(value = " SELECT\n" + 
			"        A.COMPONENT_ID||A.COMPONENT_NM,\n" + 
			"        IFNULL(SUM(BREAKDOWN_CNT),\n" + 
			"        0) AS BC,\n" + 
			"        IFNULL(SUM(BREAKDOWN_TIME),\n" + 
			"        0) AS BT,\n" + 
			"        A.T_DATE,\n" + 
			"        A.COMPONENT_ID\n" + 
			"    FROM\n" + 
			"        (\n" + 
			"        SELECT TO_CHAR(TC.T_DATE,'YYYYMM') T_DATE, DI.COMPONENT_ID,DI.COMPONENT_NM\n" + 
			"        FROM T_CALENDAR TC LEFT OUTER JOIN COMPONENT_INFO DI\n" + 
			"        on 1=1\n" + 
			"        WHERE  TO_CHAR(TC.T_DATE,'YYYY') = ?2\n" + 
			"         GROUP BY  TO_CHAR(TC.T_DATE,\n" + 
			"            'YYYYMM'), DI.COMPONENT_ID,\n" + 
			"            DI.COMPONENT_NM \n" + 
			"        ) A \n" + 
			"    LEFT   OUTER JOIN\n" + 
			"        (\n" + 
			"            SELECT\n" + 
			"                COMPONENT_ID,\n" + 
			"                COUNT(*) AS BREAKDOWN_CNT,\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYYMM') AS BREAKDOWN_DT,\n" + 
			"                SUM(IFNULL(BREAKDOWN_TIME,0)) AS BREAKDOWN_TIME \n" + 
			"            FROM\n" + 
			"                BREAKDOWN_STATUS   \n" + 
			"            WHERE\n" + 
			"                TO_CHAR(DEVICE_TIME,'YYYY') = ?2  \n" + 
			"            GROUP BY\n" + 
			"                COMPONENT_ID, TO_CHAR(DEVICE_TIME,'YYYYMM')\n" + 
			"        ) C  \n" + 
			"            ON A.COMPONENT_ID = C.COMPONENT_ID    \n" + 
			"             AND A.T_DATE = C.BREAKDOWN_DT  \n" + 
			"    WHERE\n" + 
			"       TO_CHAR(A.COMPONENT_ID) like ?1 \n" + 
			"    GROUP BY\n" + 
			"        A.COMPONENT_ID||A.COMPONENT_NM, A.T_DATE, A.COMPONENT_ID\n" + 
			"    ORDER BY\n" + 
			"        A.T_DATE,A.COMPONENT_ID\n" + 
			"", nativeQuery = true)
	List<Object[]> selectEvStsComponentOfYearChart(String deviceId, String year);
}
