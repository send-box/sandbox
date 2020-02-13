package kr.co.tipsvalley.sapsa.model.db;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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
@SuppressWarnings("serial")
@Data
@Embeddable
public class ElevatorCurrentBreakdownStatusIdEntity implements Serializable {
	
	@NonNull
	@Column(name="DEVICE_ID")
	private Integer deviceId; // 기기 ID

	@NonNull
	@Column(name="BREAKDOWN_DT")
	private String breakdownDt; // 고장일
	
	public ElevatorCurrentBreakdownStatusIdEntity() {
	}
	
	public ElevatorCurrentBreakdownStatusIdEntity(Integer deviceId, String breakdownDt) {
		super();
		this.deviceId = deviceId;
		this.breakdownDt = breakdownDt;
	}
	
}