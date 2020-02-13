package kr.co.tipsvalley.sapsa.model.db;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
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
@Data
@Entity
@SequenceGenerator(
	name="ELEVATOR_OPR_MSG_SEQ_GEN", //시퀀스 제너레이터 이름
	sequenceName="ELEVATOR_OPR_MSG_SEQ", //시퀀스 이름
	initialValue=1, //시작값
	allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)
@Table(name="OPERATION_MSG")
public class ElevatorOperationMsgEntity {
	
	@NonNull
	@EmbeddedId
	private ElevatorOperationMsgIdEntity operationId;
	
	@NonNull
	@Column(name="STATUS_ID")
	private Integer statusId;
	
	@Column(name="CREATE_DT")
	@CreationTimestamp
	private Timestamp createDt;
	
}