package kr.co.tipsvalley.sapsa.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.tipsvalley.sapsa.httpEntity.RestResponseEntity;
import kr.co.tipsvalley.sapsa.model.db.CalendarEntity;
import kr.co.tipsvalley.sapsa.model.json.DeviceInfo;
import kr.co.tipsvalley.sapsa.model.json.SearchInfo;
import kr.co.tipsvalley.sapsa.model.json.SensorInfo;
import kr.co.tipsvalley.sapsa.repository.CalendarRepository;
import kr.co.tipsvalley.sapsa.service.SensorService;

/*
 * A controller that manages the sensor APIs.
 */
@RestController
@RequestMapping("/sensor")
public class SensorController {

	static final Logger logger = LoggerFactory.getLogger(SensorController.class);

	@Autowired
	private SensorService sensorService;

	@GetMapping("/data")
	public RestResponseEntity<List<SensorInfo>> getSensorData() {
		RestResponseEntity<List<SensorInfo>> result = null;
		
		try {
			result = new RestResponseEntity<List<SensorInfo>>(sensorService.getSensorData().getSensorInfoList());
		} catch (Exception e) {
			result = new RestResponseEntity<List<SensorInfo>>(e);
		}
		
		return result;
	}	
	
	@GetMapping("/device")
	public RestResponseEntity<List<DeviceInfo>> getSensorDeviceData() {
		RestResponseEntity<List<DeviceInfo>> result = null;
		
		try {
			result = new RestResponseEntity<List<DeviceInfo>>(sensorService.getSensorDeviceData().getSensorDeviceInfoList());
		} catch (Exception e) {
			result = new RestResponseEntity<List<DeviceInfo>>(e);
		}
		
		return result;
	}
	
	@GetMapping("/statistics")
	public RestResponseEntity<List<SensorInfo>> getSensorStatisticsData(SearchInfo searchInfo) {
		RestResponseEntity<List<SensorInfo>> result = null;
		
		try {
			
			result = new RestResponseEntity<List<SensorInfo>>(sensorService.getSensorStatisticsData(searchInfo.getType(), searchInfo.getDeviceMacAddr(), searchInfo.getStartDt(), searchInfo.getEndDt(), searchInfo.getYear(), searchInfo.getMonth()).getSensorInfoList());
		} catch (Exception e) {
			result = new RestResponseEntity<List<SensorInfo>>(e);
		}
		
		return result;
	}	
	@Autowired
	private CalendarRepository cr;
	
	/**
	 * 날짜 테이블 날짜 목록 자동 생성
	 * @return
	 */
	@GetMapping("/calendar")
	public RestResponseEntity<List<SensorInfo>> setCalendarData() {
		
		CalendarEntity ce = new CalendarEntity();
		
		Calendar cal = Calendar.getInstance();
		
		Date d = new Date();
		
		cal.set(2018,0,1);
		
		while(true) {
			if(cal.get(Calendar.YEAR)==2020) break;
			ce.setT_DATE(""+cal.get(Calendar.YEAR)+(cal.get(Calendar.MONTH)+1<10?"0"+(cal.get(Calendar.MONTH)+1):(cal.get(Calendar.MONTH)+1))+(cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE):cal.get(Calendar.DATE)));
			ce.setT_NAME("");
			
			System.out.println(""+cal.get(Calendar.YEAR)+(cal.get(Calendar.MONTH)+1<10?"0"+(cal.get(Calendar.MONTH)+1):(cal.get(Calendar.MONTH)+1))+(cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE):cal.get(Calendar.DATE)));
			cal.add(Calendar.DATE, 1);
			
			cr.save(ce);
		}
		
		
		
		return null;
	}	
}