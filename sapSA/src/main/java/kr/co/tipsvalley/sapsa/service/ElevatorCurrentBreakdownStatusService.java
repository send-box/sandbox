package kr.co.tipsvalley.sapsa.service;

import java.util.List;

import kr.co.tipsvalley.sapsa.model.db.ElevatorOperationMsgEntity;

/*
 * CurrentBreakdownStatus service interface.
 */
public interface ElevatorCurrentBreakdownStatusService {

	public void mergeStatus(List<ElevatorOperationMsgEntity> list);
}