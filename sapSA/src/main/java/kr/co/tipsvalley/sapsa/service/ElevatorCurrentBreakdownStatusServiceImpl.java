package kr.co.tipsvalley.sapsa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.tipsvalley.sapsa.model.db.ElevatorCurrentBreakdownStatusEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorCurrentBreakdownStatusIdEntity;
import kr.co.tipsvalley.sapsa.model.db.ElevatorOperationMsgEntity;
import kr.co.tipsvalley.sapsa.repository.ElevatorCurrentBreakdownStatusRepository;
import kr.co.tipsvalley.sapsa.util.DateUtil;

/*
 * ElevatorCurrentBreakdownStatus service implementation.
 */
@Service
public class ElevatorCurrentBreakdownStatusServiceImpl implements ElevatorCurrentBreakdownStatusService {
	
	@Autowired
	private ElevatorCurrentBreakdownStatusRepository currentBreakdownStatusRepository;

	@Transactional
	public void mergeStatus(List<ElevatorOperationMsgEntity> list) {
		List<ElevatorCurrentBreakdownStatusEntity> statusList = new ArrayList<ElevatorCurrentBreakdownStatusEntity>();
		
		for (ElevatorOperationMsgEntity msgEntity : list) {
			int cnt = 1; // cnt 구하는 로직 구현 필요
			
			if(msgEntity.getStatusId()==1) {
				String date = msgEntity.getOperationId().getCheckDt().toString().substring(0, 10).replaceAll("-", "");
				ElevatorCurrentBreakdownStatusIdEntity idEntity = new ElevatorCurrentBreakdownStatusIdEntity(msgEntity.getOperationId().getDeviceId(), date);
				ElevatorCurrentBreakdownStatusEntity entitysss = currentBreakdownStatusRepository.findByBreakdownPK(idEntity);
				
				if(entitysss!=null) {
					cnt = entitysss.getBreakdownCnt()+1;
				}
				ElevatorCurrentBreakdownStatusEntity entity = new ElevatorCurrentBreakdownStatusEntity(idEntity, cnt);
				
				statusList.add(entity);
			}
		}
		
		currentBreakdownStatusRepository.saveAll(statusList);
	}
	
}