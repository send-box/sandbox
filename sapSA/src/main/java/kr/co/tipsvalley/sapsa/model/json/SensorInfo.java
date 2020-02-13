package kr.co.tipsvalley.sapsa.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/*
 * Sensor information json model
 */
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorInfo {

	@JsonProperty("device_mac_addr")
	private String deviceMacAddr;
	private String deviceTime;
	private double avgIlluminace;
	private double minIlluminace;
	private double maxIlluminace;
	private double avgTemperature;
	private double minTemperature;
	private double maxTemperature;
	private double avgHumidity;
	private double minHumidity;
	private double maxHumidity;

}