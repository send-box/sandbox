package kr.co.tipsvalley.sapsa.service;

import java.util.List;

import kr.co.tipsvalley.sapsa.model.json.UserMenu;

/*
 * User management service interface.
 */
public interface UserService {
	
	public List<UserMenu> getUserMenuList();
	
}