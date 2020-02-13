package kr.co.tipsvalley.sapsa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.tipsvalley.sapsa.model.db.CalendarEntity;

/*
 * JPA Calendar repository.
 */
@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity, String> {
	
}