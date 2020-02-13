package kr.co.tipsvalley.sapsa.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.tipsvalley.sapsa.model.db.ElevatorBreakdownStatusEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorBreakdownStatusIdEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorOperationMsgEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorStatusEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorStatusIdEntity;
import kr.co.tipsvalley.sapsa.model.json.ElevatorComponentDownInfo;
import kr.co.tipsvalley.sapsa.repository.ElevatorBreakdownStatusRepository;
import kr.co.tipsvalley.sapsa.repository.ElevatorStatusRepository;
import kr.co.tipsvalley.sapsa.util.DateUtil;

/*
 * ElevatorBreakdownStatus service implementation.
 */
@Service
public class ElevatorBreakdownStatusServiceImpl implements ElevatorBreakdownStatusService {

	@Autowired
	private ElevatorBreakdownStatusRepository breakdownStatusRepository;

	@Autowired
	private ElevatorStatusRepository statusRepository;

	@Transactional
	public void mergeStatus(List<ElevatorOperationMsgEntity> list) {
		List<ElevatorBreakdownStatusEntity> statusList = new ArrayList<ElevatorBreakdownStatusEntity>();

		for (ElevatorOperationMsgEntity msgEntity : list) {
			int cnt = 0; // cnt 구하는 로직 구현 필요
			int downTime = 0; // downTime 구하는 로직 구현 필요

			/*
			 * msgEntity.getCheckDt() => 날짜까지만 추가 필요 (협의)
			 */
			if (msgEntity.getStatusId() == 1) {
				ElevatorBreakdownStatusIdEntity breakdownStatusIdEntity = new ElevatorBreakdownStatusIdEntity(
						msgEntity.getOperationId().getComponentId(), msgEntity.getOperationId().getDeviceId(),
						msgEntity.getOperationId().getCheckDt());
				ElevatorBreakdownStatusEntity breakdownStatusEntity = new ElevatorBreakdownStatusEntity(
						breakdownStatusIdEntity, cnt, downTime);

				statusList.add(breakdownStatusEntity);
				statusRepository.save(new ElevatorStatusEntity(
						new ElevatorStatusIdEntity(msgEntity.getOperationId().getDeviceId(),
								msgEntity.getOperationId().getComponentId()),
						msgEntity.getStatusId(), msgEntity.getOperationId().getCheckDt()));
			} else {
				List<Object[]> data = breakdownStatusRepository.findBreakLastData(
						msgEntity.getOperationId().getDeviceId(), msgEntity.getOperationId().getComponentId());

				if (data.size() > 0) {

					long startMillis = ((Timestamp) data.get(0)[0]).getTime();
					long endMillis = msgEntity.getOperationId().getCheckDt().getTime();
					long term = (endMillis - startMillis);

					long day = term / (1000);

					ElevatorBreakdownStatusIdEntity breakdownStatusIdEntity = new ElevatorBreakdownStatusIdEntity(
							(Integer) data.get(0)[2], (Integer) data.get(0)[1], (Timestamp) data.get(0)[0]);
					ElevatorBreakdownStatusEntity breakdownStatusEntity = new ElevatorBreakdownStatusEntity(
							breakdownStatusIdEntity, 1, Integer.parseInt(String.valueOf(day)));

					statusList.add(breakdownStatusEntity);
					statusRepository
							.save(new ElevatorStatusEntity(
									new ElevatorStatusIdEntity(msgEntity.getOperationId().getDeviceId(),
											msgEntity.getOperationId().getComponentId()),
									msgEntity.getStatusId(), null));
				}

			}

			breakdownStatusRepository.saveAll(statusList);
		}
	}
	
	/*
	 * 고장 부품 목록
	 */
	public List<ElevatorComponentDownInfo> getBreakComponentListByDeviceId(int deviceId) {
		List<ElevatorComponentDownInfo> list = new ArrayList<ElevatorComponentDownInfo>();
		List<Object[]> data = breakdownStatusRepository.findBreakComponentListByDeviceId(deviceId);
		
		for (Object[] obj : data) {
			ElevatorComponentDownInfo componentDownInfo = new ElevatorComponentDownInfo();
			componentDownInfo.setComponentId((Integer) obj[0]);
			componentDownInfo.setComponentNm((String) obj[1]);
			componentDownInfo.setManufacturer((String) obj[2]);
			componentDownInfo.setSeller((String) obj[3]);
			componentDownInfo.setBreakTime((String) obj[4]);
			componentDownInfo.setDownTime(DateUtil.getSecToString(Integer.parseInt(String.valueOf(obj[5]))));
			
			list.add(componentDownInfo);
		}
		
		return list;
	}
}