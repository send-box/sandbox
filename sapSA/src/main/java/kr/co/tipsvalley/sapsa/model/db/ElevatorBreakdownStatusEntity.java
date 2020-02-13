package kr.co.tipsvalley.sapsa.model.db;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/*
 * DB Table Model Class using JPA.
 * 고장 현황
 */
@Data
@Entity
@Table(name="BREAKDOWN_STATUS")
public class ElevatorBreakdownStatusEntity {
	
	@NonNull
	@EmbeddedId
	private ElevatorBreakdownStatusIdEntity breakdownPK;
	
	@NonNull
	@Column(name="BREAKDOWN_CNT")
	private Integer breakdownCnt; // 고장 횟수
	
	@NonNull
	@Column(name="BREAKDOWN_TIME")
	private Integer breakdownTime; // 고장 시간 (초 단위 or ms 단위로 입력)
	
	@Column(name="CREATE_DT")
	@CreationTimestamp
	private Timestamp createDt;
	
	@Column(name="UPDATE_DT")
	@UpdateTimestamp
	private Timestamp updateDt;
	
	public ElevatorBreakdownStatusEntity() {
	}
	public ElevatorBreakdownStatusEntity(ElevatorBreakdownStatusIdEntity breakdownPK, Integer breakdownCnt,
			Integer breakdownTime) {
		super();
		this.breakdownPK = breakdownPK;
		this.breakdownCnt = breakdownCnt;
		this.breakdownTime = breakdownTime;
	}
	
}