package kr.co.tipsvalley.sapsa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.tipsvalley.sapsa.model.db.ElevatorCurrentBreakdownStatusEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorCurrentBreakdownStatusIdEntity;

/*
 * JPA elevator CurrentBreakdownStatus repository.
 */
@Repository
public interface ElevatorCurrentBreakdownStatusRepository extends JpaRepository<ElevatorCurrentBreakdownStatusEntity, Integer> {
	
	
	public ElevatorCurrentBreakdownStatusEntity findByBreakdownPK(ElevatorCurrentBreakdownStatusIdEntity idEntity);
}