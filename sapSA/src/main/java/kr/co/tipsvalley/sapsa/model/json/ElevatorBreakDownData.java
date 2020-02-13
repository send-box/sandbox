package kr.co.tipsvalley.sapsa.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/*
 * Json model of sensor API.
 */
@Getter @Setter
public class ElevatorBreakDownData {

	@JsonProperty("break_info")
	private List<ElevatorBreakDownInfo> elevatorBreakDownInfo;
	
	@JsonProperty("break_info2")
	private List<ElevatorBreakDownInfo> elevatorBreakDownInfo2;
	
}