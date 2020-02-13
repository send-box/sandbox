package kr.co.tipsvalley.sapsa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.tipsvalley.sapsa.model.db.ElevatorStatusEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorStatusIdEntity;
import kr.co.tipsvalley.sapsa.model.json.DeviceData;
import kr.co.tipsvalley.sapsa.model.json.DeviceInfo;
import kr.co.tipsvalley.sapsa.model.json.ElevatorBreakDownData;
import kr.co.tipsvalley.sapsa.model.json.ElevatorBreakDownInfo;
import kr.co.tipsvalley.sapsa.model.json.ElevatorData;
import kr.co.tipsvalley.sapsa.model.json.ElevatorInfo;
import kr.co.tipsvalley.sapsa.model.json.ElevatorSearchInfo;
import kr.co.tipsvalley.sapsa.repository.ElevatorStatusRepository;
import kr.co.tipsvalley.sapsa.util.DateUtil;

/*
 * ElevatorStatusService service implementation.
 */
@Service
public class ElevatorStatusServiceImpl implements ElevatorStatusService {
	
	@Autowired
	private ElevatorStatusRepository elevatorStatusRepository;

	/**
	 * 엘리베이터 모니터링 테이블 에러 초기화
	 */
	@Transactional
	public void initSet() {
		List<ElevatorStatusEntity> list = new ArrayList<ElevatorStatusEntity>();

		for(int deviceId=1; deviceId<25;deviceId++) {
			for(int componentId=1; componentId <31 ; componentId++) {
				ElevatorStatusIdEntity idEntity = new ElevatorStatusIdEntity(deviceId, componentId);
				ElevatorStatusEntity entity = new ElevatorStatusEntity(idEntity, 0, null);
				
				
				list.add(entity);
			}
		}
		elevatorStatusRepository.saveAll(list);
	}
	
	/**
	 * 엘리베이터 실시간 고장현황 (고장발생한 엘리베이터만 추출)
	 */
	public ElevatorBreakDownData getElevatorMonitor() {

		ElevatorBreakDownData elevatorData = new ElevatorBreakDownData();
		List<ElevatorBreakDownInfo> infoList = new ArrayList<ElevatorBreakDownInfo>();

			List<Object[]> elevatorList = elevatorStatusRepository.selectElevatorInfo();

			for (Object[] data : elevatorList) {
				ElevatorBreakDownInfo sensorInfo = new ElevatorBreakDownInfo();
				sensorInfo.setBuildingNm(String.valueOf(data[0]));
				sensorInfo.setElevatorNo(Integer.parseInt(String.valueOf(data[1])));
				sensorInfo.setOperationCorp(String.valueOf(data[2]));
				sensorInfo.setAddress(String.valueOf(data[3]));
				sensorInfo.setOperationManager(String.valueOf(data[4]));
				sensorInfo.setOperationAdmin(String.valueOf(data[5]));
				sensorInfo.setBreakTime(DateUtil.getSecToString(Integer.parseInt(String.valueOf(data[8]))));
				sensorInfo.setComponentCnt(Integer.parseInt(String.valueOf(data[9])));
				sensorInfo.setDeviceId(Integer.parseInt(String.valueOf(data[10])));

				infoList.add(sensorInfo);
			}

			elevatorData.setElevatorBreakDownInfo(infoList);
		return elevatorData;
	}
	
	/**
	 * 엘리베이터 실시간 고장현황 차트용(전 엘리베이터 추출)
	 */
	public ElevatorBreakDownData getElevatorMonitor2() {
		
		ElevatorBreakDownData elevatorData = new ElevatorBreakDownData();
		List<ElevatorBreakDownInfo> infoList = new ArrayList<ElevatorBreakDownInfo>();
		
		List<Object[]> elevatorList = elevatorStatusRepository.selectElevatorInfo2();
		
		for (Object[] data : elevatorList) {
			ElevatorBreakDownInfo sensorInfo = new ElevatorBreakDownInfo();
			sensorInfo.setBuildingNm(String.valueOf(data[0]));
			sensorInfo.setElevatorNo(Integer.parseInt(String.valueOf(data[1])));
			sensorInfo.setOperationCorp(String.valueOf(data[2]));
			sensorInfo.setAddress(String.valueOf(data[3]));
			sensorInfo.setOperationManager(String.valueOf(data[4]));
			sensorInfo.setOperationAdmin(String.valueOf(data[5]));
			sensorInfo.setBreakTime(DateUtil.getSecToString(Integer.parseInt(String.valueOf(data[8]))));
			sensorInfo.setComponentCnt(Integer.parseInt(String.valueOf(data[9])));
			sensorInfo.setDeviceId(Integer.parseInt(String.valueOf(data[10])));
			
			infoList.add(sensorInfo);
		}
		
		elevatorData.setElevatorBreakDownInfo2(infoList);
		return elevatorData;
	}
	
	/**
	 * 엘리베이터 조회 구분 목록
	 */
	public DeviceData getElevatorDeviceData(String type) {
		
		DeviceData elevatorData = new DeviceData();
		List<DeviceInfo> infoList = new ArrayList<DeviceInfo>();
		if (type.equals("B")) {	//건물별
			List<Object[]> elevatorList = elevatorStatusRepository.selectDeviceType1();
			for (Object[] data : elevatorList) {
				DeviceInfo sensorInfo = new DeviceInfo();
				sensorInfo.setDeviceKey(String.valueOf(data[0]));
				sensorInfo.setDeviceValue(String.valueOf(data[1]));
				
				infoList.add(sensorInfo);
			}
			
			elevatorData.setSensorDeviceInfoList(infoList);
		}else if(type.equals("O")) {	//운영사별
			List<String> elevatorList = elevatorStatusRepository.selectDeviceType2();
			for (String data : elevatorList) {
				DeviceInfo sensorInfo = new DeviceInfo();
				sensorInfo.setDeviceKey(data);
				sensorInfo.setDeviceValue(data);
				
				infoList.add(sensorInfo);
			}
			
			elevatorData.setSensorDeviceInfoList(infoList);
			
		}else {	//부품별
			List<Object[]> elevatorList = elevatorStatusRepository.selectDeviceType3();
			for (Object[] data : elevatorList) {
				DeviceInfo sensorInfo = new DeviceInfo();
				sensorInfo.setDeviceKey(String.valueOf(data[0]));
				sensorInfo.setDeviceValue(String.valueOf(data[1]));
				
				infoList.add(sensorInfo);
			}
			
			elevatorData.setSensorDeviceInfoList(infoList);
		}
		return elevatorData;
	}

	/**
	 * 엘리베이터 통계 조회
	 */
	@Override
	public ElevatorData getElevatorStatisticsData(ElevatorSearchInfo elevatorSearchInfo) {
		
		ElevatorData elevatorData = new ElevatorData();
		
		List<ElevatorInfo> infoList = new ArrayList<ElevatorInfo>();
		List<HashMap<String, Object>> chartList = new ArrayList<HashMap<String, Object>>(); //고장횟수 차트
		List<HashMap<String, Object>> chartList2 = new ArrayList<HashMap<String, Object>>(); //고장시간 차트
		
		
		if (elevatorSearchInfo.getGubun().equals("B")) {	//건물별
			List<Object[]> elevatorList = new ArrayList<Object[]>();
			List<Object[]> elevatorChart = new ArrayList<Object[]>();
			if(elevatorSearchInfo.getType().equals("D")) {	//일별
				elevatorList = elevatorStatusRepository.selectEvStsBuildingOfDayList(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getStartDt(),elevatorSearchInfo.getEndDt());
				elevatorChart = elevatorStatusRepository.selectEvStsBuildingOfDayChart(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getStartDt(),elevatorSearchInfo.getEndDt());
			}else if(elevatorSearchInfo.getType().equals("M")) {	//월별
				elevatorList = elevatorStatusRepository.selectEvStsBuildingOfMonthList(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear(), elevatorSearchInfo.getMonth());
				elevatorChart = elevatorStatusRepository.selectEvStsBuildingOfMonthChart(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear(), elevatorSearchInfo.getMonth());
			}else if(elevatorSearchInfo.getType().equals("Y")) {	//년별
				elevatorList = elevatorStatusRepository.selectEvStsBuildingOfYearList(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear());
				elevatorChart = elevatorStatusRepository.selectEvStsBuildingOfYearChart(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear());
			}
			for (Object[] data : elevatorList) {
				ElevatorInfo sensorInfo = new ElevatorInfo();
				sensorInfo.setBuildingNm(String.valueOf(data[0]));
				sensorInfo.setElevatorNo(Integer.parseInt(String.valueOf(data[1])));
				sensorInfo.setBreakdownCnt(Integer.parseInt(String.valueOf(data[2])));
				sensorInfo.setBreakdownTime(DateUtil.getLongToString(Long.parseLong(String.valueOf(data[3]))));
				sensorInfo.setOperationManager(String.valueOf(data[4]));
				sensorInfo.setOperationAdmin(String.valueOf(data[5]));
				infoList.add(sensorInfo);
			}
			HashMap<String,Object> sensorInfo = new HashMap<String,Object>();	//고장횟수
			HashMap<String,Object> sensorInfo2 = new HashMap<String,Object>();	//고장시간
			String temp="";
			for (int i=0; i<elevatorChart.size();i++) {
				Object[] data = elevatorChart.get(i);
				if(!temp.equals(String.valueOf(data[3]))) {
					if(i!=0) {
						chartList.add(sensorInfo);
						chartList2.add(sensorInfo2);
					}
					sensorInfo = new HashMap<String,Object>();
					sensorInfo.put("date", String.valueOf(data[3]));
					sensorInfo.put(String.valueOf(data[0]), String.valueOf(data[1]));
					sensorInfo2 = new HashMap<String,Object>();
					sensorInfo2.put("date", String.valueOf(data[3]));
					sensorInfo2.put(String.valueOf(data[0]), Long.parseLong(String.valueOf(data[2]))/3600);
				}else {
					sensorInfo.put("date", String.valueOf(data[3]));
					sensorInfo.put(String.valueOf(data[0]), String.valueOf(data[1]));
					sensorInfo2.put("date", String.valueOf(data[3]));
					sensorInfo2.put(String.valueOf(data[0]), Long.parseLong(String.valueOf(data[2]))/3600);
				}
				if(i==elevatorChart.size()-1) {
					chartList.add(sensorInfo);
					chartList2.add(sensorInfo2);
				}
				temp = String.valueOf(data[3]);
			}
			
			elevatorData.setElevatorStatisticsInfoList(infoList);	//엘리베이터 통계 리스트
			elevatorData.setElevatorStatisticsChartList(chartList);	//고장횟수 차트 리스트
			elevatorData.setElevatorStatisticsChartList2(chartList2);	//고장시간 차트 리스트
		}else if(elevatorSearchInfo.getGubun().equals("O")) {	//운영사별 
			List<Object[]> elevatorList = new ArrayList<Object[]>();
			List<Object[]> elevatorChart = new ArrayList<Object[]>();
			if(elevatorSearchInfo.getType().equals("D")) {
				elevatorList = elevatorStatusRepository.selectEvStsOperationCorpOfDayList(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getStartDt(),elevatorSearchInfo.getEndDt());
				elevatorChart = elevatorStatusRepository.selectEvStsOperationCorpOfDayChart(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getStartDt(),elevatorSearchInfo.getEndDt());
			}else if(elevatorSearchInfo.getType().equals("M")) {
				elevatorList = elevatorStatusRepository.selectEvStsOperationCorpOfMonthList(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear(),elevatorSearchInfo.getMonth());
				elevatorChart = elevatorStatusRepository.selectEvStsOperationCorpOfMonthChart(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear(),elevatorSearchInfo.getMonth());
			}else if(elevatorSearchInfo.getType().equals("Y")) {
				elevatorList = elevatorStatusRepository.selectEvStsOperationCorpOfYearList(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear());
				elevatorChart = elevatorStatusRepository.selectEvStsOperationCorpOfYearChart(elevatorSearchInfo.getDeviceKey().equals("-전체-")?"%":elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear());
			}
			for (Object[] data : elevatorList) {
				ElevatorInfo sensorInfo = new ElevatorInfo();
				sensorInfo.setOperationCorp(String.valueOf(data[0]));
				sensorInfo.setElevatorNo(Integer.parseInt(String.valueOf(data[1])));
				sensorInfo.setBreakdownCnt(Integer.parseInt(String.valueOf(data[2])));
				sensorInfo.setBreakdownTime(DateUtil.getLongToString(Long.parseLong(String.valueOf(data[3]))));
				sensorInfo.setOperationManager(String.valueOf(data[4]));
				sensorInfo.setOperationAdmin(String.valueOf(data[5]));
				infoList.add(sensorInfo);
			}
			
			HashMap<String,Object> sensorInfo = new HashMap<String,Object>();
			HashMap<String,Object> sensorInfo2 = new HashMap<String,Object>();
			String temp="";
			for (int i=0; i<elevatorChart.size();i++) {
				Object[] data = elevatorChart.get(i);
				if(!temp.equals(String.valueOf(data[3]))) {
					if(i!=0) {
						chartList.add(sensorInfo);
						chartList2.add(sensorInfo2);
					}
					sensorInfo = new HashMap<String,Object>();
					sensorInfo.put("date", String.valueOf(data[3]));
					sensorInfo.put(String.valueOf(data[0]), String.valueOf(data[1]));
					sensorInfo2 = new HashMap<String,Object>();
					sensorInfo2.put("date", String.valueOf(data[3]));
					sensorInfo2.put(String.valueOf(data[0]), Long.parseLong(String.valueOf(data[2]))/3600);
				}else {
					sensorInfo.put("date", String.valueOf(data[3]));
					sensorInfo.put(String.valueOf(data[0]), String.valueOf(data[1]));
					sensorInfo2.put("date", String.valueOf(data[3]));
					sensorInfo2.put(String.valueOf(data[0]), Long.parseLong(String.valueOf(data[2]))/3600);
				}
				if(i==elevatorChart.size()-1) {
					chartList.add(sensorInfo);
					chartList2.add(sensorInfo2);
				}
				temp = String.valueOf(data[3]);
			}
			
			elevatorData.setElevatorStatisticsInfoList(infoList);
			elevatorData.setElevatorStatisticsChartList(chartList);
			elevatorData.setElevatorStatisticsChartList2(chartList2);
			
		}else {	//부품별
			List<Object[]> elevatorList = new ArrayList<Object[]>();
			List<Object[]> elevatorChart = new ArrayList<Object[]>();
			if(elevatorSearchInfo.getType().equals("D")) {
				elevatorList = elevatorStatusRepository.selectEvStsComponentOfDayList(elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getStartDt(),elevatorSearchInfo.getEndDt());
				elevatorChart = elevatorStatusRepository.selectEvStsComponentOfDayChart(elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getStartDt(),elevatorSearchInfo.getEndDt());
			}else if(elevatorSearchInfo.getType().equals("M")) {
				elevatorList = elevatorStatusRepository.selectEvStsComponentOfMonthList(elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear(),elevatorSearchInfo.getMonth());
				elevatorChart = elevatorStatusRepository.selectEvStsComponentOfMonthChart(elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear(),elevatorSearchInfo.getMonth());
			}else if(elevatorSearchInfo.getType().equals("Y")) {
				elevatorList = elevatorStatusRepository.selectEvStsComponentOfYearList(elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear());
				elevatorChart = elevatorStatusRepository.selectEvStsComponentOfYearChart(elevatorSearchInfo.getDeviceKey(),elevatorSearchInfo.getYear());
			}
			for (Object[] data : elevatorList) {
				ElevatorInfo sensorInfo = new ElevatorInfo();
				sensorInfo.setComponentNm(String.valueOf(data[0]));
				sensorInfo.setBreakdownCnt(Integer.parseInt(String.valueOf(data[1])));
				sensorInfo.setBreakdownTime(DateUtil.getLongToString(Long.parseLong(String.valueOf(data[2]))));
				sensorInfo.setManufacturer(String.valueOf(data[3]));
				sensorInfo.setSeller(String.valueOf(data[4]));
				infoList.add(sensorInfo);
			}

			HashMap<String,Object> sensorInfo = new HashMap<String,Object>();
			HashMap<String,Object> sensorInfo2 = new HashMap<String,Object>();
			String temp="";
			for (int i=0; i<elevatorChart.size();i++) {
				Object[] data = elevatorChart.get(i);
				if(!temp.equals(String.valueOf(data[3]))) {
					if(i!=0) {
						chartList.add(sensorInfo);
						chartList2.add(sensorInfo2);
					}
					sensorInfo = new HashMap<String,Object>();
					sensorInfo.put("date", String.valueOf(data[3]));
					sensorInfo.put(String.valueOf(data[0]), String.valueOf(data[1]));
					sensorInfo2 = new HashMap<String,Object>();
					sensorInfo2.put("date", String.valueOf(data[3]));
					sensorInfo2.put(String.valueOf(data[0]), Long.parseLong(String.valueOf(data[2]))/3600);
				}else {
					sensorInfo.put("date", String.valueOf(data[3]));
					sensorInfo.put(String.valueOf(data[0]), String.valueOf(data[1]));
					sensorInfo2.put("date", String.valueOf(data[3]));
					sensorInfo2.put(String.valueOf(data[0]), Long.parseLong(String.valueOf(data[2]))/3600);
				}
				if(i==elevatorChart.size()-1) {
					chartList.add(sensorInfo);
					chartList2.add(sensorInfo2);
				}
				temp = String.valueOf(data[3]);
			}
			
			elevatorData.setElevatorStatisticsInfoList(infoList);
			elevatorData.setElevatorStatisticsChartList(chartList);
			elevatorData.setElevatorStatisticsChartList2(chartList2);
		}
		
		return elevatorData;
	}
	
}