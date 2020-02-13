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
public class ElevatorSearchInfo {

	private String deviceKey;
	private String gubun;
	private String type;
	private String year;
	private String month;
	private String startDt;
	private String endDt;

}