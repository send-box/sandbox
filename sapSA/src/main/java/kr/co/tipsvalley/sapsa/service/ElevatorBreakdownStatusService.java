package kr.co.tipsvalley.sapsa.service;

import java.util.List;

import kr.co.tipsvalley.sapsa.model.db.ElevatorOperationMsgEntity;
import kr.co.tipsvalley.sapsa.model.json.ElevatorComponentDownInfo;

/*
 * ElevatorBreakdownStatus service interface.
 */
public interface ElevatorBreakdownStatusService {

	public void mergeStatus(List<ElevatorOperationMsgEntity> list);
	
	/*
	 * 고장 부품 목록
	 */
	public List<ElevatorComponentDownInfo> getBreakComponentListByDeviceId(int deviceId);

}