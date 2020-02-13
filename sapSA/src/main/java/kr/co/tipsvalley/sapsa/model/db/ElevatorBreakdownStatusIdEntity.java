package kr.co.tipsvalley.sapsa.model.db;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.NonNull;

/*
 * DB Table PK Model Class using JPA.(BREAKDOWN_STATUS)
 */
@SuppressWarnings("serial")
@Data
@Embeddable
public class ElevatorBreakdownStatusIdEntity implements Serializable {
	
	@NonNull
	@Column(name="COMPONENT_ID")
	private Integer componentId; // 부품 ID

	@NonNull
	@Column(name="DEVICE_ID")
	private Integer deviceId; // 기기 ID
	
	@NonNull
	@Column(name="DEVICE_TIME")
	private Timestamp deviceTime;
	
	public ElevatorBreakdownStatusIdEntity() {
	}
	
	public ElevatorBreakdownStatusIdEntity(Integer componentId, Integer deviceId, Timestamp deviceTime) {
		super();
		this.componentId = componentId;
		this.deviceId = deviceId;
		this.deviceTime = deviceTime;
	}
}