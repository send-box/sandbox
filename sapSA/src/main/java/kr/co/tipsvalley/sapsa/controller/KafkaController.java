package kr.co.tipsvalley.sapsa.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.tipsvalley.sapsa.httpEntity.RestResponseEntity;
import kr.co.tipsvalley.sapsa.model.json.KafkaSensorInfo;

/*
 * A controller that manages the sensor APIs.
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {

	private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@GetMapping("/data")
	public RestResponseEntity<List<KafkaSensorInfo>> kafka(Model model) throws IOException {
		logger.info("/kafka/data start");
		RestResponseEntity<List<KafkaSensorInfo>> result = null;
		List<KafkaSensorInfo> data = new ArrayList<KafkaSensorInfo>();
		HashMap<String, KafkaSensorInfo> data2 = new HashMap<String, KafkaSensorInfo>();
		try {

			Properties props = new Properties();
			props.put("bootstrap.servers", "kafka1:9092,kafka2:9092,kafka3:9092");	//카프카 서버목록
			props.put("group.id", "perter-consumer2");	//카프카 컨슈머 그룹 아이디
			props.put("enable.auto.commit", "true");	//자동 커밋
			props.put("auto.offset.reset", "latest");	//옵셋 옵션 커밋 이후 데이터 요청
			props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
			props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

			KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

			//카프카 요청 토픽 목록 리스트
			consumer.subscribe(
				Arrays.asList("tips_demo_sensor2"));
			try {
				ConsumerRecords<String, String> records = consumer.poll(1000);	//폴링 타임 1000ms
				
				logger.info("/kafka/data records count: {}", records.count());

				//레코드로 부터 토픽별 처리
				for (ConsumerRecord<String, String> record : records) {
					switch (record.topic()) {
					case "tips_demo_sensor2":	//토픽명
						logger.info("kafka record: {}", record);
						String rowData[] = record.value().split(","); // split문자(,)로 문자 배열 생성
						KafkaSensorInfo kafkaSensorInfo = new KafkaSensorInfo();

						if(rowData[4].indexOf("T")>0) { //날짜 유형 문자열 처리
							kafkaSensorInfo.setTime(rowData[4].substring(rowData[4].indexOf("T")+1, rowData[4].indexOf("T")+9));
						}else {
							kafkaSensorInfo.setTime(rowData[4]);
						}
						kafkaSensorInfo.setIlluminance(Double.parseDouble(rowData[1]));	//조도센서값
						kafkaSensorInfo.setTemperature(Double.parseDouble(rowData[2]));	//온도센서값
						kafkaSensorInfo.setHumidity(Double.parseDouble(rowData[3]));	//습도센서값
						kafkaSensorInfo.setDeviceMacAddr(rowData[0]);	//센서 맥주소값

						data2.put(rowData[0], kafkaSensorInfo);
						break;
//					case "tips_demo_sensor2":
//						rowData = record.value().split(",");
//						kafkaSensorInfo = new KafkaSensorInfo();
//						
//						kafkaSensorInfo.setTime(rowData[10]);
//						kafkaSensorInfo.setTemperature(Double.parseDouble(rowData[8]));
//						kafkaSensorInfo.setIlluminance(Double.parseDouble(rowData[7]));
//						kafkaSensorInfo.setHumidity(Double.parseDouble(rowData[9]));
//						kafkaSensorInfo.setDeviceMacAddr(rowData[0]);
//						
//						data2.put(rowData[0], kafkaSensorInfo);
//						break;
					default:
						break;
					}
				}
				for (Entry<String, KafkaSensorInfo> entry : data2.entrySet()) {
					data.add(entry.getValue());
				}
				result = new RestResponseEntity<List<KafkaSensorInfo>>(data);
			} catch (Exception e) {
				result = new RestResponseEntity<List<KafkaSensorInfo>>(e);
			} finally {
				consumer.close();
			}
		} catch (Exception e) {
			result = new RestResponseEntity<List<KafkaSensorInfo>>(e);
		}
		
		return result;
	}
}