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
public class KafkaSensorInfo {

	@JsonProperty("device_mac_addr")
	private String deviceMacAddr;
	private double temperature;
	private double illuminance;
	private double humidity;
	private String time;

}