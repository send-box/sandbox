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
public class SearchInfo {

	@JsonProperty("device_mac_addr")
	private String deviceMacAddr;
	private String type;
	private String year;
	private String month;
	@JsonProperty("start_dt")
	private String startDt;
	@JsonProperty("end_dt")
	private String endDt;

}