package kr.co.tipsvalley.sapsa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.tipsvalley.sapsa.model.db.ElevatorOperationMsgEntity;

/*
 * JPA elevator OperationMsg repository.
 */
@Repository
public interface ElevatorOperationMsgRepository extends JpaRepository<ElevatorOperationMsgEntity, Integer> {
	
}