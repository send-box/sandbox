package kr.co.tipsvalley.sapsa.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/*
 * Json model of sensor API.
 */
@Getter @Setter
public class DeviceData {

	@JsonProperty("device_info")
	private List<DeviceInfo> sensorDeviceInfoList;
	
}