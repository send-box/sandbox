package kr.co.tipsvalley.sapsa.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Json model with user menu.
 */
@Getter @Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMenu {

	@JsonProperty("menu_id")
	private String menuId;
	
	@JsonProperty("menu_name")
	private String menuName;
	
	@JsonProperty("sub_menu")
	private List<UserMenu> subMenuList;

	public UserMenu(String menuId, String menuName)	{
		this.menuId = menuId;
		this.menuName = menuName;
	}
	
}