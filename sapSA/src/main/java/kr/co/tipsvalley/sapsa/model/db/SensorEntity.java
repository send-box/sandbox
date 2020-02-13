package kr.co.tipsvalley.sapsa.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/*
 * DB Table Model Class using JPA.
 */
@Getter @Setter
@Entity
@Table(name="TIPS_SENSOR_TEMP2")
public class SensorEntity {
	
	@EmbeddedId
	private SensorIdEntity sensorPK;

	@Column(name="AVG_ILLUMINACE")
	private double avgIlluminace;
	
	@Column(name="MIN_ILLUMINACE")
	private double minIlluminace;
	
	@Column(name="MAX_ILLUMINACE")
	private double maxIlluminace;
	
	@Column(name="AVG_TEMPERATURE")
	private double avgTemperature;
	
	@Column(name="MIN_TEMPERATURE")
	private double minTemperature;
	
	@Column(name="MAX_TEMPERATURE")
	private double maxTemperature;
	
	@Column(name="AVG_HUMIDITY")
	private double avgHumidity;
	
	@Column(name="MIN_HUMIDITY")
	private double minHumidity;
	
	@Column(name="MAX_HUMIDITY")
	private double maxHumidity;
	
}