sap.ui.define([
		"tips/common/controller/BaseController",
		"tips/common/util/Formatter"
	], function (BaseController, Formatter) {
		"use strict";
	
	return BaseController.extend("tips.common.controller.Header", {
		
		onInit: function() {
			window.hd = {};
			window.hd.v = this;
			window.hd.w = 1068; //헤더 메뉴 width
			window.hd.h = "87px"; //헤더 메뉴 height
			window.hd.selMenuId = null; //선택된 메뉴 ID
			window.hd.selSubMenuId = null; //선택된 서브메뉴 ID
			window.hd.subMenuPopover = {}; // 서브메뉴 팝업
			window.hd.mId = "elevatorLiveChart"; //메인 메뉴 ID
			
			// 개별 헤더 초기화
			this.onInitHeader();
		},
		
		// 메뉴 동적 세팅 완료 후 호툴
		onAterMenuSetting: function(oEvent) {
			this.getRouter().attachRoutePatternMatched(this._onRouteMatched, this);
		},
		
		// 메뉴 이동시마다 발생
		_onRouteMatched: function(oEvent) {
			var sRouteName = oEvent.getParameter("name");
			var _menuId = sRouteName;
			var _subMenuId = sRouteName;
			
			if (sRouteName.indexOf('.') > -1) {
				_menuId = sRouteName.substring(0, sRouteName.indexOf("."));
				_subMenuId = sRouteName.substring(sRouteName.indexOf(".") + 1);
			}
			
			// 메뉴 선택한 색으로 변경
			sap.ui.getCore().byId(_menuId).addStyleClass("menuSelection");
			
			if (window.hd.selMenuId && window.hd.selMenuId != _menuId) {
				sap.ui.getCore().byId(window.hd.selMenuId).removeStyleClass("menuSelection");
			}
			window.hd.selMenuId = _menuId; // 현재 선택된 메뉴로 변경
			window.hd.selSubMenuId = _subMenuId;
		},

		// 메뉴 세팅(item 추가)
		onMenuSetting : function (oResult) {
			var oMenu = window.hd.v.byId("menuLayout");
			var _menuWidth = window.hd.w / oResult.length;
			if(_menuWidth > 250) _menuWidth = 250;
			
			oResult.forEach(function(oMenuInfo, index) {
				var oHbox = new sap.m.HBox({
					id: Formatter.formatFirstLowerCase(oMenuInfo.menu_id),
					displayBlock: true,
					alignItems: "Center",
					width: _menuWidth + "px",
					height: window.hd.h
				});
				oHbox.addStyleClass("headerHbox");
				
				var oText = new sap.m.Text({
					id: oHbox.getId() + "_txt",
					text: oMenuInfo.menu_name,
					width: (_menuWidth-50) + "px"
				});
				
				oHbox.addItem(oText);
				oMenu.addContent(oHbox);
				
				if(oMenuInfo.sub_menu) {
					oHbox.attachBrowserEvent("mouseover", function() {
						window.hd.v.onMouseOverMenuItem(oHbox.getId(), oMenuInfo.sub_menu);
					});
					oHbox.attachBrowserEvent("mouseout", function() {
						window.hd.v.onMouseOutMenuItem(oHbox.getId());
					});
				} else {
					oHbox.attachBrowserEvent("click", window.hd.v.onClickMenuItem);
				}
			});
			
			window.hd.v.onAterMenuSetting();
		},
		
		// 하위 메뉴 없는 메뉴에 마우스 오버할 경우 호출
		onMouseOutMenuItem : function (sId) {
			var isHoverMenu = jQuery.sap.byId(sId).filter(':hover').length == 0 ? false : true;
			var isHoverSubMenu = jQuery.sap.byId("pop_"+sId).filter(':hover').length == 0 ? false : true;
			
			if (!isHoverMenu && !isHoverSubMenu && window.hd.subMenuPopover[sId] && window.hd.subMenuPopover[sId].isOpen()) {
				window.hd.subMenuPopover[sId].close();
			}
		},
		
		// 하위 메뉴 있는 메뉴에 마우스 오버할 경우 호출
		onMouseOverMenuItem : function (sId, oSubMenu) {
			if (window.hd.subMenuPopover[sId] && window.hd.subMenuPopover[sId].isOpen()) {
				return;
			}
			
			if (!window.hd.subMenuPopover[sId]) {
				var _xml = '<Popover xmlns="sap.m" id="pop_'+ sId +'" showHeader="false" contentWidth="13rem" placement="Bottom" class="popoverSubMenu">'
					+ '<FlexBox height="'+ (oSubMenu.length*2.5) +'rem" direction="Column">';
				
				oSubMenu.forEach(function(oMenuInfo) {
					_xml +=	jQuery.sap.formatMessage('<Button id="{0}.{1}" text="{2}" width="13rem" class="popSubMenu" press="onClickSubMenu" type="Transparent" icon="sap-icon://feeder-arrow" />', [sId, oMenuInfo.menu_id, oMenuInfo.menu_name]);
				});
				
				_xml +=	'</FlexBox></Popover>';
				
				var oFragment = sap.ui.xmlfragment({fragmentContent : _xml}, window.hd.v);
				window.hd.subMenuPopover[sId] = oFragment;
				window.hd.v.getView().addDependent(oFragment);
				oFragment.attachBrowserEvent("mouseout", function() {
					window.hd.v.onMouseOutMenuItem(sId);
				});
			}
			
			var _menu = sap.ui.getCore().byId(sId);
			window.hd.subMenuPopover[sId].openBy(_menu);
		},
		
		// 서브 메뉴 클릭 시 호출
		onClickSubMenu : function (oEvent) {
			var _id = oEvent.getSource().getId();
			var sId = _id.substr(0, _id.indexOf("."));
			
			window.hd.subMenuPopover[sId].close();
			window.hd.v.getRouter().navTo(_id);
			sap.ui.getCore().byId(_id).addStyleClass("sapMBtnActive");
		},
		
		// 하위 메뉴 없는 메뉴 클릭 시 호출
		onClickMenuItem: function (oEvent) {
			window.hd.v.getRouter().navTo(oEvent.currentTarget.id); // 메뉴 이동
		},
		
		// 처음 페이지로 이동
		onMain : function () {
			this.getRouter().navTo(window.hd.mId);
		}
		
	});

}, /* bExport= */ true);