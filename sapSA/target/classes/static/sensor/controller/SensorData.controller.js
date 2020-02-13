sap.ui.define([
		"tips/sensor/controller/BaseController",
		"sap/ui/model/json/JSONModel",
		"sap/m/MessageBox"
	], function (BaseController, JSONModel, MessageBox) {
		"use strict";

	return BaseController.extend("tips.sensor.controller.SensorData", {

		oParam: {
			url: "/sensor/data"
		},
		
		onInit : function () {
			this.onGetData();
		},
		
		// 목록 호출
		onGetData : function () {
			this.oParam.type = "get";
			this.oParam.data = null;
			this.oParam.callback = "callbackAjaxList";
			this.onAjaxCall(this.oParam);
			
		},
		
		// 목록 콜백
		callbackAjaxList : function (oModel) {
			this.setModel(oModel);
			console.info('Total Length: ' + oModel.getData().result.length);
		},
		
		onPressExport: function(oEvent) {
			this.onExport(this.createColumnConfig(), this.getModel().getProperty("/result"), "Sensor Data");
		},
		createColumnConfig: function() {
			return [
				{ label: 'Device Mac Addr', 	property: 'device_mac_addr', 	textAlign: "center", type: 'String', width: 40 },
				{ label: 'Avg Illuminace', 		property: 'avgIlluminace', 	textAlign: "center", type: 'String' },
				{ label: 'Min Illuminace', 		property: 'minIlluminace', 	textAlign: "center", type: 'String' },
				{ label: 'Max Illuminace', 		property: 'maxIlluminace', 	textAlign: "center", type: 'String' },
				{ label: 'Avg Temperature', 	property: 'avgTemperature', 	textAlign: "center", type: 'String' },
				{ label: 'Min Temperature', 	property: 'minTemperature', 	textAlign: "center", type: 'String' },
				{ label: 'Max Temperature', 	property: 'maxTemperature', 	textAlign: "center", type: 'String' },
				{ label: 'Avg Humidity', 		property: 'avgHumidity', 	textAlign: "center", type: 'String' },
				{ label: 'Min Humidity', 		property: 'minHumidity', 	textAlign: "center", type: 'String' },
				{ label: 'Max Humidity', 		property: 'maxHumidity', 	textAlign: "center", type: 'String' }
			];
		}
	});

}, /* bExport= */ true);