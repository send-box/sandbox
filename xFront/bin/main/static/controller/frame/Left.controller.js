sap.ui.define([
	"OpenUI5/controller/common/BaseController"
], function(BaseController){
	"use strict";
		
	return BaseController.extend("OpenUI5.controller.common.BaseController", {
		onInit : function() {
			console.log("Left.js OnInit()");
		},

		
		onNavToContent1 : function() {
			console.log("Left.js onNavToContent1 Clicked");
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("contents1");
		},
		
		onNavToContent2 : function(oEvent) {
			console.log("Left.js onNavToContent2 Clicked");

			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		    oRouter.navTo("contents2");
		    
		},
		
		onNavToContent3 : function(oEvent) {
			console.log("Left.js onNavToContent3 Clicked");

			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		    oRouter.navTo("contents3");
		    
		}
	});
}, true);