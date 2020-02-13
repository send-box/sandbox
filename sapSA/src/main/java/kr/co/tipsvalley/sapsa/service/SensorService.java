package kr.co.tipsvalley.sapsa.service;

import kr.co.tipsvalley.sapsa.model.json.DeviceData;
import kr.co.tipsvalley.sapsa.model.json.SensorData;

/*
 * Sensor service interface.
 */
public interface SensorService {

	public SensorData getSensorData();

	public DeviceData getSensorDeviceData();
	
	public SensorData getSensorStatisticsData(String type, String device, String startDt, String endDt, String year, String month);
}