package kr.co.tipsvalley.sapsa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.tipsvalley.sapsa.model.db.SensorEntity;
import kr.co.tipsvalley.sapsa.model.json.DeviceData;
import kr.co.tipsvalley.sapsa.model.json.DeviceInfo;
import kr.co.tipsvalley.sapsa.model.json.SensorData;
import kr.co.tipsvalley.sapsa.model.json.SensorInfo;
import kr.co.tipsvalley.sapsa.repository.SensorRepository;

/*
 * Sensor service implementation.
 */
@Service
public class SensorServiceImpl implements SensorService {

	@Autowired
	SensorRepository sensorRepository;

	public SensorData getSensorData() {
		SensorData sensorData = new SensorData();
		List<SensorInfo> sensorInfoList = new ArrayList<SensorInfo>();
		List<SensorEntity> sensorDataEnitityList = sensorRepository.findAll();

		for (SensorEntity sensorEntity : sensorDataEnitityList) {
			SensorInfo sensorInfo = new SensorInfo();
			sensorInfo.setDeviceMacAddr(sensorEntity.getSensorPK().getDeviceMacAddr());
			sensorInfo.setAvgIlluminace(sensorEntity.getAvgIlluminace() < 2 ? sensorEntity.getAvgIlluminace()*100 : sensorEntity.getAvgIlluminace());
			sensorInfo.setMinIlluminace(sensorEntity.getMinIlluminace() < 2 ? sensorEntity.getMinIlluminace()*100 : sensorEntity.getMinIlluminace());
			sensorInfo.setMaxIlluminace(sensorEntity.getMaxIlluminace() < 2 ? sensorEntity.getMaxIlluminace()*100 : sensorEntity.getMaxIlluminace());
			sensorInfo.setAvgTemperature(sensorEntity.getAvgTemperature() < 1 ? sensorEntity.getAvgIlluminace()*100 : sensorEntity.getAvgIlluminace());
			sensorInfo.setMinTemperature(sensorEntity.getMinTemperature() < 1 ? sensorEntity.getMinTemperature()*100 : sensorEntity.getMinTemperature());
			sensorInfo.setMaxTemperature(sensorEntity.getMaxTemperature() < 1 ? sensorEntity.getMaxTemperature()*100 : sensorEntity.getMaxTemperature());
			sensorInfo.setAvgHumidity(sensorEntity.getAvgHumidity() < 1 ? sensorEntity.getAvgHumidity()*100 : sensorEntity.getAvgHumidity());
			sensorInfo.setMinHumidity(sensorEntity.getMinHumidity() < 1 ? sensorEntity.getMinHumidity()*100 : sensorEntity.getMinHumidity());
			sensorInfo.setMaxHumidity(sensorEntity.getMaxHumidity() < 1 ? sensorEntity.getMaxHumidity()*100 : sensorEntity.getMaxHumidity());

			sensorInfoList.add(sensorInfo);
		}

		sensorData.setSensorInfoList(sensorInfoList);

		return sensorData;
	}

	public DeviceData getSensorDeviceData() {
		DeviceData deviceData = new DeviceData();
		List<DeviceInfo> deviceInfoList = new ArrayList<DeviceInfo>();
		List<String> deviceDataEnitityList = sensorRepository.selectSensorDeviceList();

		for (String sensorEntity : deviceDataEnitityList) {
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setDeviceMacAddr(sensorEntity);

			deviceInfoList.add(deviceInfo);
		}

		deviceData.setSensorDeviceInfoList(deviceInfoList);

		return deviceData;
	}

	public SensorData getSensorStatisticsData(String type, String device, String startDt, String endDt, String year,
			String month) {

		SensorData sensorData = new SensorData();
		List<SensorInfo> sensorInfoList = new ArrayList<SensorInfo>();

		if (type.equals("D")) {
			List<Object[]> sensorList = sensorRepository.selectSensorStatisticsListD(device, startDt, endDt);

			for (Object[] data : sensorList) {
				SensorInfo sensorInfo = new SensorInfo();
				sensorInfo.setDeviceMacAddr(String.valueOf(data[0]));
				sensorInfo.setDeviceTime(String.valueOf(data[1]));
				sensorInfo.setAvgIlluminace(Double.parseDouble(String.valueOf(data[2])));
				sensorInfo.setMinIlluminace(Double.parseDouble(String.valueOf(data[3])));
				sensorInfo.setMaxIlluminace(Double.parseDouble(String.valueOf(data[4])));
				sensorInfo.setAvgTemperature(Double.parseDouble(String.valueOf(data[5])));
				sensorInfo.setMinTemperature(Double.parseDouble(String.valueOf(data[6])));
				sensorInfo.setMaxTemperature(Double.parseDouble(String.valueOf(data[7])));
				sensorInfo.setAvgHumidity(Double.parseDouble(String.valueOf(data[8])));
				sensorInfo.setMinHumidity(Double.parseDouble(String.valueOf(data[9])));
				sensorInfo.setMaxHumidity(Double.parseDouble(String.valueOf(data[10])));
				
				sensorInfo.setAvgIlluminace(sensorInfo.getAvgIlluminace() < 2 ? sensorInfo.getAvgIlluminace()*100 : sensorInfo.getAvgIlluminace());
				sensorInfo.setMinIlluminace(sensorInfo.getMinIlluminace() < 2 ? sensorInfo.getMinIlluminace()*100 : sensorInfo.getMinIlluminace());
				sensorInfo.setMaxIlluminace(sensorInfo.getMaxIlluminace() < 2 ? sensorInfo.getMaxIlluminace()*100 : sensorInfo.getMaxIlluminace());
				sensorInfo.setAvgTemperature(sensorInfo.getAvgTemperature() < 2 ? sensorInfo.getAvgTemperature()*100 : sensorInfo.getAvgTemperature());
				sensorInfo.setMinTemperature(sensorInfo.getAvgTemperature() < 2 ? sensorInfo.getAvgTemperature()*100 : sensorInfo.getAvgTemperature());
				sensorInfo.setMaxTemperature(sensorInfo.getAvgTemperature() < 2 ? sensorInfo.getAvgTemperature()*100 : sensorInfo.getAvgTemperature());
				sensorInfo.setAvgHumidity(sensorInfo.getAvgHumidity() < 2 ? sensorInfo.getAvgHumidity()*100 : sensorInfo.getAvgHumidity());
				sensorInfo.setMinHumidity(sensorInfo.getAvgHumidity() < 2 ? sensorInfo.getAvgHumidity()*100 : sensorInfo.getAvgHumidity());
				sensorInfo.setMaxHumidity(sensorInfo.getAvgHumidity() < 2 ? sensorInfo.getAvgHumidity()*100 : sensorInfo.getAvgHumidity());

				sensorInfoList.add(sensorInfo);
			}

			sensorData.setSensorInfoList(sensorInfoList);
		} else if (type.equals("M")) {
			List<Object[]> sensorList = sensorRepository.selectSensorStatisticsListM(device, year, month);

			for (Object[] data : sensorList) {
				SensorInfo sensorInfo = new SensorInfo();
				sensorInfo.setDeviceMacAddr(String.valueOf(data[0]));
				sensorInfo.setDeviceTime(String.valueOf(data[1]));
				sensorInfo.setAvgIlluminace(Double.parseDouble(String.valueOf(data[2])));
				sensorInfo.setMinIlluminace(Double.parseDouble(String.valueOf(data[3])));
				sensorInfo.setMaxIlluminace(Double.parseDouble(String.valueOf(data[4])));
				sensorInfo.setAvgTemperature(Double.parseDouble(String.valueOf(data[5])));
				sensorInfo.setMinTemperature(Double.parseDouble(String.valueOf(data[6])));
				sensorInfo.setMaxTemperature(Double.parseDouble(String.valueOf(data[7])));
				sensorInfo.setAvgHumidity(Double.parseDouble(String.valueOf(data[8])));
				sensorInfo.setMinHumidity(Double.parseDouble(String.valueOf(data[9])));
				sensorInfo.setMaxHumidity(Double.parseDouble(String.valueOf(data[10])));

				sensorInfoList.add(sensorInfo);
			}

			sensorData.setSensorInfoList(sensorInfoList);
		} else if (type.equals("Y")) {
			List<Object[]> sensorList = sensorRepository.selectSensorStatisticsListY(device, year);
			
			for (Object[] data : sensorList) {
				SensorInfo sensorInfo = new SensorInfo();
				sensorInfo.setDeviceMacAddr(String.valueOf(data[0]));
				sensorInfo.setDeviceTime(String.valueOf(data[1]));
				sensorInfo.setAvgIlluminace(Double.parseDouble(String.valueOf(data[2])));
				sensorInfo.setMinIlluminace(Double.parseDouble(String.valueOf(data[3])));
				sensorInfo.setMaxIlluminace(Double.parseDouble(String.valueOf(data[4])));
				sensorInfo.setAvgTemperature(Double.parseDouble(String.valueOf(data[5])));
				sensorInfo.setMinTemperature(Double.parseDouble(String.valueOf(data[6])));
				sensorInfo.setMaxTemperature(Double.parseDouble(String.valueOf(data[7])));
				sensorInfo.setAvgHumidity(Double.parseDouble(String.valueOf(data[8])));
				sensorInfo.setMinHumidity(Double.parseDouble(String.valueOf(data[9])));
				sensorInfo.setMaxHumidity(Double.parseDouble(String.valueOf(data[10])));
				
				sensorInfoList.add(sensorInfo);
			}
			
			sensorData.setSensorInfoList(sensorInfoList);
		}

		return sensorData;
	}

}