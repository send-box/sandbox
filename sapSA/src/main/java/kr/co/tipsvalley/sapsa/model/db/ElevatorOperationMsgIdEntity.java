package kr.co.tipsvalley.sapsa.model.db;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NonNull;

/*
 * DB Table Model Class using JPA.
 */
@SuppressWarnings("serial")
@Data
@Embeddable
public class ElevatorOperationMsgIdEntity implements Serializable {
	
	@NonNull
	@Column(name="DEVICE_ID")
	private Integer deviceId;
	
	@NonNull
	@Column(name="COMPONENT_ID")
	private Integer componentId;
	
	@NonNull
	@Column(name="CHECK_DT")
	private Timestamp checkDt;
	
}