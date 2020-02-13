package kr.co.tipsvalley.sapsa.model.db;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.NonNull;

/*
 * DB Table Model Class using JPA.
 * 엘리베이터 부품별 상태 현황
 */
@Data
@Entity
@Table(name="DEVICE_MONITORING")
public class ElevatorStatusEntity {
	
	@NonNull
	@EmbeddedId
	private ElevatorStatusIdEntity statusPK;
	
	@NonNull
	@Column(name="STATUS_ID")
	private Integer statusId; // 상태값

	@Column(name="BREAK_TIME")
	private Timestamp breakTime; // 고장시간
	
	public ElevatorStatusEntity() {
	}
	
	public ElevatorStatusEntity(ElevatorStatusIdEntity statusPK, Integer statusId, Timestamp breakTime) {
		super();
		this.statusPK = statusPK;
		this.statusId = statusId;
		this.breakTime = breakTime;
	}
	
}