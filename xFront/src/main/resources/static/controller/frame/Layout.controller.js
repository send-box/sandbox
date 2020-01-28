sap.ui.define([
	"OpenUI5/controller/common/BaseController"
], function(BaseController) {
    "use strict";
		
	return BaseController.extend("OpenUI5.controller.common.BaseController", {
		
		fullscreen : "",
		
		onInit : function () {
			
			window.layoutControll = this;
			
			console.log("Layout.js OnInit()");
			
		},
		
		onAfterRendering : function () {
			console.log("Layout.js onAfterRendering()");
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("contents1");
			
			// CHECK DEVICE TYPE AND SETTING FULL SCREEN MODE.
			
			var deviceFilter = "win16|win32|win64|macintel|mac|"
				
				if( navigator.platform) {

				    if( deviceFilter.indexOf(navigator.platform.toLowerCase())<0 ) {
				
				    	// "MOBILE" DEVICE TYPE + SETTING META INFO
				        var metaInfo 		= 	document.getElementsByTagName('meta').viewport;
				        metaInfo.content 	= 	"width=device-width, initial-scale=0.5, user-scalable=no";
				        
				        
				    }
				    else {
				    	
				    	// "PC" 	DEVICE TYPE + SETTING META INFO
				    	/*var fullScreenArea 	= 	document.getElementById('content');
				        this.fullscreen 	= 	fullScreenArea;
				        this.layoutControll.fullscreen.requestFullscreen(); */
				    	
				    }
				}
			
			
		},
		
		
		
	});
}, true);