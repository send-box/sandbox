package kr.co.tipsvalley.sapsa.model.json;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/*
 * Json model of sensor API.
 */
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElevatorData {

	@JsonProperty("statistics_info")
	private List<ElevatorInfo> elevatorStatisticsInfoList;
	
	@JsonProperty("chart_info")
	private List<HashMap<String, Object>> elevatorStatisticsChartList;
	
	@JsonProperty("chart_info2")
	private List<HashMap<String, Object>> elevatorStatisticsChartList2;
	
}