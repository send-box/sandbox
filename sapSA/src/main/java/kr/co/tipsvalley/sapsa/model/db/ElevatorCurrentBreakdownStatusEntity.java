package kr.co.tipsvalley.sapsa.model.db;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/*
 * DB Table Model Class using JPA.
 * 현재 고장 현황
 */
@Data
@Entity
@Table(name="CURRENT_BREAKDOWN_STATUS")
public class ElevatorCurrentBreakdownStatusEntity {
	
	@NonNull
	@EmbeddedId
	private ElevatorCurrentBreakdownStatusIdEntity breakdownPK;
	
	@NonNull
	@Column(name="BREAKDOWN_CNT")
	private Integer breakdownCnt; // 고장 횟수
	
	@Column(name="UPDATE_DT")
	@UpdateTimestamp
	private Timestamp updateDt;

	public ElevatorCurrentBreakdownStatusEntity() {
	}
	
	public ElevatorCurrentBreakdownStatusEntity(ElevatorCurrentBreakdownStatusIdEntity breakdownPK, Integer breakdownCnt) {
		super();
		this.breakdownPK = breakdownPK;
		this.breakdownCnt = breakdownCnt;
	}
	
}