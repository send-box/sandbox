package kr.co.tipsvalley.sapsa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.tipsvalley.sapsa.model.json.UserMenu;

/*
 * User management service.
 */
@Service
public class UserServiceImpl implements UserService {

	public List<UserMenu> getUserMenuList() {
		List<UserMenu> userMenuList = new ArrayList<UserMenu>();
		
		UserMenu userMenu5 = new UserMenu();
		userMenu5.setMenuId("ElevatorLiveChart");
		userMenu5.setMenuName("Elevator\nSA (Live Chart)");
		userMenuList.add(userMenu5);
		
		UserMenu userMenu4 = new UserMenu();
		userMenu4.setMenuId("ElevatorMonitoring");
		userMenu4.setMenuName("Elevator\nSA (Monitoring)");
		userMenuList.add(userMenu4);
		
		UserMenu userMenu6 = new UserMenu();
		userMenu6.setMenuId("ElevatorStatisticsData");
		userMenu6.setMenuName("Elevator\nSA (Statistics)");
		userMenuList.add(userMenu6);
		
		UserMenu userMenu = new UserMenu();
		userMenu.setMenuId("RealtimeSensor");
		userMenu.setMenuName("IoT\nSA (Live Chart)");
		userMenuList.add(userMenu);

		UserMenu userMenu2 = new UserMenu();
		userMenu2.setMenuId("SensorData");
		userMenu2.setMenuName("IoT\nSA (Monitoring)");
		userMenuList.add(userMenu2);

		UserMenu userMenu3 = new UserMenu();
		userMenu3.setMenuId("StatisticsData");
		userMenu3.setMenuName("IoT\nSA (Statistics)");
		userMenuList.add(userMenu3);
		
		UserMenu userMenu7 = new UserMenu();
		userMenu7.setMenuId("SparkRealtimeSensor");
		userMenu7.setMenuName("IoT\nSpark (Live Chart)");
		userMenuList.add(userMenu7);
		
//		UserMenu userMenu8 = new UserMenu();
//		userMenu2.setMenuId("SparkSensorData");
//		userMenu2.setMenuName("IoT\nSpark (Monitoring)");
//		userMenuList.add(userMenu8);
//
//		UserMenu userMenu9 = new UserMenu();
//		userMenu3.setMenuId("SparkStatisticsData");
//		userMenu3.setMenuName("IoT\nSpark (Statistics)");
//		userMenuList.add(userMenu9);
		
		return userMenuList;
	}
	
}