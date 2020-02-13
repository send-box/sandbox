package kr.co.tipsvalley.sapsa.model.db;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/*
 * DB Table PK Model Class using JPA.(TIPS_SENSOR_TEMP2)
 */
@SuppressWarnings("serial")
@Data
@Embeddable
public class SensorIdEntity implements Serializable {

	@Column(name="DEVICE_MAC_ADDR")
	private String deviceMacAddr;
	
	@Column(name="DEVICE_TIME")
	private Timestamp deviceTime;	
}