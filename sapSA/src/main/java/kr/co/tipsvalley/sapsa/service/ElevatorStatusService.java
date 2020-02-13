package kr.co.tipsvalley.sapsa.service;

import kr.co.tipsvalley.sapsa.model.json.DeviceData;
import kr.co.tipsvalley.sapsa.model.json.ElevatorBreakDownData;
import kr.co.tipsvalley.sapsa.model.json.ElevatorData;
import kr.co.tipsvalley.sapsa.model.json.ElevatorSearchInfo;

/*
 * CurrentBreakdownStatus service interface.
 */
public interface ElevatorStatusService {

	public void initSet();
	
	public ElevatorBreakDownData getElevatorMonitor();
	public ElevatorBreakDownData getElevatorMonitor2();

	public DeviceData getElevatorDeviceData(String type);
	
	public ElevatorData getElevatorStatisticsData(ElevatorSearchInfo elevatorSearchInfo);
}