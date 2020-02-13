package kr.co.tipsvalley.sapsa.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.NonNull;

/*
 * DB Table Model Class using JPA.
 * 엘리베이터 부품 상태 현황
 */
@SuppressWarnings("serial")
@Data
@Embeddable
public class ElevatorStatusIdEntity implements Serializable {
	
	@NonNull
	@Column(name="DEVICE_ID")
	private Integer deviceId; // 기기 ID

	@NonNull
	@Column(name="COMPONENT_ID")
	private Integer componentId; // 부품 ID
	
	public ElevatorStatusIdEntity() {
	}
	
	public ElevatorStatusIdEntity(Integer deviceId, Integer componentId) {
		super();
		this.deviceId = deviceId;
		this.componentId = componentId;
	}
	
}