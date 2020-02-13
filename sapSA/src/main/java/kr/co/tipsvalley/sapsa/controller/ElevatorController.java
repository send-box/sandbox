package kr.co.tipsvalley.sapsa.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.tipsvalley.sapsa.httpEntity.RestBaseResponse;
import kr.co.tipsvalley.sapsa.httpEntity.RestResponseEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorOperationMsgEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorOperationMsgIdEntity;
import kr.co.tipsvalley.sapsa.model.json.DeviceInfo;
import kr.co.tipsvalley.sapsa.model.json.ElevatorBreakDownInfo;
import kr.co.tipsvalley.sapsa.model.json.ElevatorComponentDownInfo;
import kr.co.tipsvalley.sapsa.model.json.ElevatorData;
import kr.co.tipsvalley.sapsa.model.json.ElevatorSearchInfo;
import kr.co.tipsvalley.sapsa.model.json.SensorInfo;
import kr.co.tipsvalley.sapsa.repository.ElevatorOperationMsgRepository;
import kr.co.tipsvalley.sapsa.repository.ElevatorStatusRepository;
import kr.co.tipsvalley.sapsa.service.ElevatorBreakdownStatusService;
import kr.co.tipsvalley.sapsa.service.ElevatorCurrentBreakdownStatusService;
import kr.co.tipsvalley.sapsa.service.ElevatorStatusService;
import kr.co.tipsvalley.sapsa.util.DateUtil;

/*
 * A controller that manages the sensor APIs.
 */
@RestController
@RequestMapping("/elevator")
public class ElevatorController {

	static final Logger logger = LoggerFactory.getLogger(ElevatorController.class);

	@Autowired private ElevatorOperationMsgRepository operationMsgRepository;
	@Autowired private ElevatorStatusRepository elevatorStatusRepository;
	@Autowired private ElevatorBreakdownStatusService breakdownStatusService;
	@Autowired private ElevatorCurrentBreakdownStatusService currentBreakdownStatusService;
	@Autowired private ElevatorStatusService statusService;
	
	@GetMapping("/msg/insert/rnd")
	public RestBaseResponse insertMsgRandom() {
		RestBaseResponse result = null;
		
		Arrays.asList();
		
		try {
			Timestamp currTimestamp = DateUtil.stringToTimestamp(DateUtil.getCurrentDateTime(DateUtil.DATE_TIME_PATTERN), DateUtil.DATE_TIME_PATTERN);
//			operationMsgRepository.save(new ElevatorOperationMsgEntity(currTimestamp, 1, 1, 1));
			
			result = new RestBaseResponse(RestBaseResponse.SuccessCode);
		} catch (Exception e) {
			result = new RestResponseEntity<List<SensorInfo>>(e);
		}
		
		return result;
	}
	
	/**
	 * 엘리베이터 정보 실시간 생성
	 * 서버 실행 후 실행해줘야 실시간 데이터를 생성해준다. 정지는 없음
	 * @return
	 */
	@GetMapping("/msg/insert")
	public RestBaseResponse insertMsg() {
		int execCnt = 30000; // 실행 횟수 설정
		boolean flag = true;
		RestBaseResponse result = null;
		
		try {
			while (flag) {
				Timestamp currTimestamp = DateUtil.stringToTimestamp(DateUtil.getCurrentDateTime(DateUtil.DATE_TIME_PATTERN), DateUtil.DATE_TIME_PATTERN);
				
				List<ElevatorOperationMsgEntity> list = new ArrayList<ElevatorOperationMsgEntity>();
				int[] devId01N24 = ThreadLocalRandom.current().ints(1, 25).limit(5).toArray();
				
				for(int devId : devId01N24) {
					int[] compId01N30 = ThreadLocalRandom.current().ints(1, 31).limit(5).toArray();
					
					for(int compId : compId01N30) {
						int[] status = ThreadLocalRandom.current().ints(0, 100).limit(1).toArray();
						ElevatorOperationMsgIdEntity operationMsgIdEntity = new ElevatorOperationMsgIdEntity(devId, compId, currTimestamp);
						list.add(new ElevatorOperationMsgEntity(operationMsgIdEntity, status[0]<95?0:1));
						Properties props = new Properties();
						props.put("bootstrap.servers", "kafka1:9092,kafka2:9092,kafka3:9092");
						props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
						props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
						props.put("acks", "1");

						KafkaProducer<String, String> producer = new KafkaProducer<>(props);
						producer.send(new ProducerRecord<String, String>("tips_demo_sensor3", currTimestamp.toString().substring(0, 10)+"T"+currTimestamp.toString().substring(11, currTimestamp.toString().length())+"z"+","+devId+","+compId+","+(status[0]<95?0:1)));
						producer.close();
					}
				}
//				operationMsgRepository.saveAll(list);
				breakdownStatusService.mergeStatus(list);
				currentBreakdownStatusService.mergeStatus(list);
				
				Thread.sleep(1000);
				
				if (--execCnt == 0) break;
			}

			
			result = new RestBaseResponse(RestBaseResponse.SuccessCode);
		} catch (Exception e) {
			result = new RestBaseResponse(e);
		}
		
		return result;
	}
	
	/**
	 * 고장 부품 목록
	 * @return
	 */
	@GetMapping("/breakComponent/{dId}")
	public RestResponseEntity<List<ElevatorComponentDownInfo>> getBreakComponentListByDeviceId(@PathVariable("dId") int deviceId) {
		logger.info("getBreakComponentListByDeviceId start");
		logger.info("getBreakComponentListByDeviceId deviceId:{}", deviceId);
		System.out.println("deviceId : " + deviceId);
		
		RestResponseEntity<List<ElevatorComponentDownInfo>> result = null;
		
		try {
			result = new RestResponseEntity<List<ElevatorComponentDownInfo>>(breakdownStatusService.getBreakComponentListByDeviceId(deviceId));
		} catch (Exception e) {
			e.printStackTrace();
			result = new RestResponseEntity<List<ElevatorComponentDownInfo>>(e);
		}
		
		return result;
	}
	
	/**
	 * 엘리베이터 센서별 모니터링 정보 초기화
	 * 엘리베이터 모니터링 테이블의 데이터 값 비고장으로 일괄 처리함
	 * @return
	 */
	@GetMapping("/msg/initSet")
	public RestBaseResponse insertDeviceMonitoringSet() {
		
		try {
			statusService.initSet();
				
		} catch (Exception e) {
		}
		
		return null;
	}
	
//	@GetMapping("/msg/data/{msgId}")
//	public RestResponseEntity<ElevatorOperationMsgEntity> getMsg(@PathVariable Integer msgId) {
//		RestResponseEntity<ElevatorOperationMsgEntity> result = null;
//		
//		try {
//			result = new RestResponseEntity<ElevatorOperationMsgEntity>(operationMsgRepository.findById(msgId).get());
//		} catch (Exception e) {
//			result = new RestResponseEntity<ElevatorOperationMsgEntity>(e);
//		}
//		
//		return result;
//	}
	
//	@GetMapping("/msg/list")
//	public RestResponseEntity<List<ElevatorOperationMsgEntity>> getMsgList() {
//		RestResponseEntity<List<ElevatorOperationMsgEntity>> result = null;
//		
//		try {
//			result = new RestResponseEntity<List<ElevatorOperationMsgEntity>>(operationMsgRepository.findAll());
//		} catch (Exception e) {
//			result = new RestResponseEntity<List<ElevatorOperationMsgEntity>>(e);
//		}
//		
//		return result;
//	}
	
	/**
	 * 엘리베이터 고장현황
	 * @return
	 */
	@GetMapping("/liveData")
	public RestResponseEntity<List<ElevatorBreakDownInfo>> getElevatorRealData() {
		RestResponseEntity<List<ElevatorBreakDownInfo>> result = null;
		
		try {
			
			result = new RestResponseEntity<List<ElevatorBreakDownInfo>>(statusService.getElevatorMonitor().getElevatorBreakDownInfo());
		} catch (Exception e) {
			result = new RestResponseEntity<List<ElevatorBreakDownInfo>>(e);
		}
		
		return result;
	}
	
	/**
	 * 엘리베이터 고장현황 (전체)
	 * @return
	 */
	@GetMapping("/liveData2")
	public RestResponseEntity<List<ElevatorBreakDownInfo>> getElevatorRealData2() {
		RestResponseEntity<List<ElevatorBreakDownInfo>> result = null;
		
		try {
			
			result = new RestResponseEntity<List<ElevatorBreakDownInfo>>(statusService.getElevatorMonitor2().getElevatorBreakDownInfo2());
		} catch (Exception e) {
			result = new RestResponseEntity<List<ElevatorBreakDownInfo>>(e);
		}
		
		return result;
	}
	
	/**
	 * 엘리베이터 조회조건
	 * @return
	 */
	@GetMapping("/device")
	public RestResponseEntity<List<DeviceInfo>> getElevatorDeviceData(ElevatorSearchInfo elevatorSearchInfo) {
		RestResponseEntity<List<DeviceInfo>> result = null;
		
		try {
			result = new RestResponseEntity<List<DeviceInfo>>(statusService.getElevatorDeviceData(elevatorSearchInfo.getGubun()).getSensorDeviceInfoList());
		} catch (Exception e) {
			result = new RestResponseEntity<List<DeviceInfo>>(e);
		}
		
		return result;
	}
	
	/**
	 * 엘리베이터 조회조건
	 * @return
	 */
	@GetMapping("/statistics")
	public RestResponseEntity<ElevatorData> getElevatorStatisticsData(ElevatorSearchInfo elevatorSearchInfo) {
		RestResponseEntity<ElevatorData> result = null;
		
		try {
			result = new RestResponseEntity<ElevatorData>(statusService.getElevatorStatisticsData(elevatorSearchInfo));
		} catch (Exception e) {
			result = new RestResponseEntity<ElevatorData>(e);
		}
		
		return result;
	}
	
}