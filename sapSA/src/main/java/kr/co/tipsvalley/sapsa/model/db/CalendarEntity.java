package kr.co.tipsvalley.sapsa.model.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/*
 * DB Table Model Class using JPA.
 */
@Getter @Setter
@Entity
@Table(name="T_CALENDAR")
public class CalendarEntity {
	
	@Id
	private String T_DATE;
	
	private String T_NAME;
	
}