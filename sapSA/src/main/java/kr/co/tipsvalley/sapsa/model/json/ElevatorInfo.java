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
public class ElevatorInfo {

	private String buildingNm;
	private int elevatorNo;
	private int breakdownCnt;
	private String breakdownTime;
	private String operationCorp;
	private String operationManager;
	private String operationAdmin;
	
	private int componentId;
	private String componentNm;
	private String manufacturer;
	private String seller;

}