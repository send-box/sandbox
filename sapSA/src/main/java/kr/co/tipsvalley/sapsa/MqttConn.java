package kr.co.tipsvalley.sapsa;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MqttConn {
	
	static final Logger logger = LoggerFactory.getLogger(MqttConn.class);

	@Bean
	public static String main() {
		final String MQTT_BROKER_IP = "tcp://192.168.1.111:1883";
		final String mqtt_id = MqttClient.generateClientId();
		try {
			final MqttClient client = new MqttClient(MQTT_BROKER_IP, mqtt_id, new MemoryPersistence());

			client.connect();
			client.setCallback(new MqttCallback() {

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					logger.info("[topic] {} : {}", topic, message.toString());

					Properties props = new Properties();
					props.put("bootstrap.servers", "kafka1:9092,kafka2:9092,kafka3:9092");
					props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
					props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
					props.put("acks", "1");

					KafkaProducer<String, String> producer = new KafkaProducer<>(props);

					producer.send(new ProducerRecord<String, String>("tips_demo_sensor1", message.toString()));

					producer.close();
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {

				}

				@Override
				public void connectionLost(Throwable cause) {
					logger.error("connectionLost");
					try {
						client.reconnect();
					}catch (Exception e) {
						logger.error("ReconnectionLost");
					}
				}
			});

			client.subscribe("mytopic", 1);
		} catch (MqttException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}
}