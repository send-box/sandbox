package kr.co.tipsvalley.sapsa.model.json;

import java.io.Serializable;

import lombok.Data;

/*
 * ElevatorComponentDownInfo information json model
 */
@SuppressWarnings("serial")
@Data
public class ElevatorComponentDownInfo implements Serializable {

	private Integer componentId;
	private String componentNm;
	private String manufacturer;
	private String seller;
	private String breakTime;
	private String downTime;

}