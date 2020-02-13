sap.ui.define([
		"tips/common/controller/BaseController",
		"tips/common/controller/Header.controller",
		"tips/common/util/Formatter"
	], function (BaseController, HeaderController, Formatter) {
		"use strict";
	
	return BaseController.extend("tips.common.controller.Index", {
		onInit : function () {
			var oParam = {
				url: "/user/menu/info",
				callback: "callbackAjaxMenuInfo"
			};
			this.onAjaxCall(oParam);
			
			this.getRouter().attachBypassed(function(oEvent) {
				var sHash = oEvent.getParameter("hash");
				jQuery.sap.log.error("Sorry, but the hash '" + sHash + "' is invalid.", "The resource was not found.");
			});
		},
		
		// 메뉴 목록 콜백
		callbackAjaxMenuInfo : function (oModel) {
			var oResult = oModel.getData().result;
			
			console.error(oResult);
			
			var _self = this;
			var oRouter = this.getRouter();
			
			oResult.forEach(function(oMenuInfo, index) {
				var _rootView = _self.getOwnerComponent().getAggregation("rootControl").getId();
				var _menuId = Formatter.formatFirstLowerCase(oMenuInfo.menu_id);
				
				if(index > 0) {
					if(oMenuInfo.sub_menu) {
						oMenuInfo.sub_menu.forEach(function(item) {
							var _subMenuId = _menuId + "." + item.menu_id;
							oRouter.getTargets().addTarget(item.menu_id, {viewName: _subMenuId, viewLevel: index+1, viewId: item.menu_id, rootView: _rootView});
							oRouter.addRoute({name: _subMenuId, pattern: _menuId + "/" + item.menu_id, target: item.menu_id});
						});
					} else {
						oRouter.getTargets().addTarget(_menuId, {viewName: oMenuInfo.menu_id, viewLevel: index+1, viewId: _menuId, rootView: _rootView});
						oRouter.addRoute({name: _menuId, pattern: _menuId, target: _menuId});
					}
				}
			});
			
			oRouter.initialize();
			HeaderController.prototype.onMenuSetting(oResult);
		}
	});

}, /* bExport= */ true);