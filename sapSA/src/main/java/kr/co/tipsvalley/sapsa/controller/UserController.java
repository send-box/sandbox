package kr.co.tipsvalley.sapsa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.tipsvalley.sapsa.httpEntity.RestResponseEntityList;
import kr.co.tipsvalley.sapsa.model.json.UserMenu;
import kr.co.tipsvalley.sapsa.service.UserService;

/*
 * Controller that manages user information.
 */
@RestController
@RequestMapping("/user")
public class UserController {

	static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/menu/info")
	public RestResponseEntityList<UserMenu> getUserMenuInfo() {
		RestResponseEntityList<UserMenu> result;
		try {
			result = new RestResponseEntityList<UserMenu>(userService.getUserMenuList());
		} catch (Exception e) {
			result = new RestResponseEntityList<UserMenu>(e);
		}

		return result;
	}

}