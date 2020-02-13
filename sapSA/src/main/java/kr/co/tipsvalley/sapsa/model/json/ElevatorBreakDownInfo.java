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
public class ElevatorBreakDownInfo {

	private String buildingNm;
	private int elevatorNo;
	private String operationCorp;
	private String address;
	private String operationManager;
	private String operationAdmin;
	private String breakTime;
	private int componentCnt;
	private int deviceId;

}