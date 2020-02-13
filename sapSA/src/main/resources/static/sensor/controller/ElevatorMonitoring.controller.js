sap.ui.define([
		"tips/sensor/controller/BaseController",
		"sap/ui/model/json/JSONModel",
		"sap/m/MessageBox"
	], function (BaseController, JSONModel, MessageBox) {
		"use strict";

	return BaseController.extend("tips.sensor.controller.ElevatorMonitoring", {

		oParam: {
			url: "/elevator/liveData"
		},
		
		onInit : function() {
			this.jsonData = this.clone(this.tempJsonData);
			this.oParam.url = "/elevator/liveData";
			this.oParam.type = "get";
			this.oParam.callback = "callbackAjaxList";
			var that = this;
			this.onAjaxCall(this.oParam);
			
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
				   console.error("onBeforeShow");
				   that.dataInterval = setInterval(function() {
						that.onAjaxCall(that.oParam);
					}, 5000);
			   },
			   onAfterShow: function(oEvent) {
//				   console.error("onAfterShow");
			   },
			   onAfterHide: function(oEvent) {
//				   console.error("onAfterHide");
				   clearInterval(that.dataInterval);
			   }
			});
		},
		
		// 목록 콜백
		callbackAjaxList : function (oModel) {
			this.setModel(oModel);
			console.info('Total Length: ' + oModel.getData().result.length);
		},
		
		onPressCell: function(oEvent) {
			var rowDataPath = oEvent.getParameters().rowBindingContext.sPath;//get binding row data path
			var rowData = this.getView().getModel().getProperty(rowDataPath);
			console.error(rowData.deviceId);
			
			var oParam = {
				url : "/elevator/breakComponent/" + rowData.deviceId,
				callback : "callbackAjaxCompList"
			}
			this.onAjaxCall(oParam);
		},
		
		callbackAjaxCompList: function(oModel) {
			this.setModel(oModel, "breakComponent");
			
			if (!this.oComponentDialog) {
				this.oComponentDialog = sap.ui.xmlfragment("tips.sensor.fragment.ElevatorMonitoringComponent", this);
				
				this.getView().addDependent(this.oComponentDialog);
			}
			
			this.oComponentDialog.open();
		},
		
		onCloseDialog: function(oEvent) {
			oEvent.getSource().oParent.close();
		},
		
		onPressExport: function(oEvent) {
			this.onExport(this.createColumnConfig(), this.getModel().getProperty("/result"), "실시간 고장현황");
		},
		createColumnConfig: function() {
			return [
				{ label: '건물', 	property: 'buildingNm', 	textAlign: "center", type: 'String', width: 40 },
				{ label: '호기', 		property: 'avgIlluminace', 	textAlign: "center", type: 'String' },
				{ label: '기기운영회사', 		property: 'minIlluminace', 	textAlign: "center", type: 'String' },
				{ label: '소재지', 		property: 'maxIlluminace', 	textAlign: "center", type: 'String' },
				{ label: '기기운영책임자', 	property: 'avgTemperature', 	textAlign: "center", type: 'String' },
				{ label: 'APT관리책임자', 	property: 'minTemperature', 	textAlign: "center", type: 'String' },
				{ label: '고장시간', 	property: 'maxTemperature', 	textAlign: "center", type: 'String' }
			];
		}
	});

}, /* bExport= */ true);