package kr.co.tipsvalley.sapsa.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

/*
 * DB Table Model Class using JPA.
 */
@Data
@Entity
@Table(name="TIPS_SENSOR_TEMP2")
public class DeviceEntity {
	
	@EmbeddedId
	private String deviceMacAddr;

}