sap.ui.define([
		"tips/sensor/controller/BaseController",
		"sap/ui/model/json/JSONModel",
		"sap/m/MessageBox",
		'sap/viz/ui5/data/FlattenedDataset',
		'sap/viz/ui5/format/ChartFormatter',
		'sap/viz/ui5/api/env/Format',
		'tips/common/util/DateUtil'
	], function (BaseController, JSONModel, MessageBox, FlattenedDataset, ChartFormatter, Format, DateUtil) {
		"use strict";

	return BaseController.extend("tips.sensor.controller.StatisticsData", {

		oParam: {
//			url: "/sensor/data"
		},
		
		tempJsonData : {
			milk : [ 
			]
		},

		settingsModel : {
			deviceSet : {
				name : "DeviceSet",
				defaultSelected : 1,
				values : [ {
					name : "온도",
					value : [ "avgTemperature" ]
				}, {
					name : "조도",
					value : [ "avgIlluminace" ]
				}, {
					name : "습도",
					value : [ "avgHumidity" ]
				} ]
			},
			dataset : {
				name : "Dataset",
				defaultSelected : 0,
				values : [ {
					name : "EE:AB:94:BE:19:A9",
					value : "EE:AB:94:BE:19:A9"
				}, {
					name : "C5:34:78:B2:45:55",
					value : "C5:34:78:B2:45:55"
				} ]
			},
			dataLabel : {
				name : "Value Label",
				defaultState : true
			},
			axisTitle : {
				name : "Axis Title",
				defaultState : false
			},
			dimensions : [ {
				name : 'deviceTime',
				value : "{deviceTime}",
			} ],
			measures : [ {
				name : 'avgTemperature',
				value : '{avgTemperature}'
			}, {
				name : 'avgIlluminace',
				value : '{avgIlluminace}'
			}, {
				name : 'avgHumidity',
				value : '{avgHumidity}'
			}
			]
		},

		oVizFrame : null,
		

		initPageSettings : function(oView) {
			// Hide Settings Panel for phone
			if (sap.ui.Device.system.phone) {
				var oSettingsPanel = oView.byId('settingsPanel');
				if (oSettingsPanel) {
					oSettingsPanel.setExpanded(false);
				}
			}

			// try to load sap.suite.ui.commons for using ChartContainer
			// sap.suite.ui.commons is available in sapui5-sdk-dist but not in demokit
			var libraries = sap.ui.getVersionInfo().libraries || [];
			var bSuiteAvailable = libraries.some(function(lib) {
				return lib.name.indexOf("sap.suite.ui.commons") > -1;
			});
			if (bSuiteAvailable) {
				jQuery.sap.require("sap/suite/ui/commons/ChartContainer");
				var vizframe = oView.byId("idVizFrame");
				var oChartContainerContent = new sap.suite.ui.commons.ChartContainerContent({
					icon : "sap-icon://line-chart",
					title : "vizFrame Line Chart Sample",
					content : [ vizframe ]
				});
				var oChartContainer = new sap.suite.ui.commons.ChartContainer({
					content : [ oChartContainerContent ]
				});
				oChartContainer.setShowFullScreen(true);
//				oChartContainer.setAutoAdjustHeight(true);
				oView.byId('chartFixFlex').addContent(oChartContainer);
			}
		},
		
		onInit : function () {
			this.jsonData = this.clone(this.tempJsonData);
			this.onInitData();
			
			this.getView().byId("__component0---statisticsData--dateSearch--dpStart").setValue(DateUtil.getToday("."));
			this.getView().byId("__component0---statisticsData--dateSearch--dpEnd").setValue(DateUtil.getToday("."));
			
//			this.getView().addEventDelegate({
//			   onBeforeShow: function(oEvent) {
//				   console.error("onBeforeShow");
//			   },
//			   onAfterShow: function(oEvent) {
//				   console.error("onAfterShow");
//			   },
//			   onAfterHide: function(oEvent) {
//				   console.error("onAfterHide");
//			   }
//			});
		},
		onAfterRendering : function(){
			 $(".dateAreaVbox2").css("display","none");
//			 console.error($(".period1"))
//				$(".period1").press();
		},
		
		// 목록 호출
		onInitData : function () {
			this.oParam.type = "get";
			this.oParam.data = null;
			this.oParam.url = "/sensor/device";
			this.oParam.callback = "callbackInitAjaxList";
			this.onAjaxCall(this.oParam);
			

			Format.numericFormatter(ChartFormatter.getInstance());
			var formatPattern = ChartFormatter.DefaultPattern;
			// set explored app's demo model on this sample
			var oModel = new JSONModel(this.settingsModel);
			oModel.setDefaultBindingMode(sap.ui.model.BindingMode.OneWay);
			this.getView().setModel(oModel);

			var oVizFrame = this.oVizFrame = this.getView().byId("idVizFrame");

			console.error(oVizFrame)
			oVizFrame.setVizProperties({
				plotArea : {
					window: {
                        start: "firstDataPoint",
                        end: "lastDataPoint"
                    },
					dataLabel : {
						formatString : formatPattern.SHORTFLOAT_MFD2,
						visible : true
					}
				},
				valueAxis : {
					label : {
						formatString : formatPattern.SHORTFLOAT
					},
					title : {
						visible : false
					}
				},
				categoryAxis : {
					title : {
						visible : false
					}
				},
				title : {
					visible : false,
					text : 'Revenue by City and Store Name'
				}
			});
			var dataModel = new JSONModel(this.jsonData);
			//			dataModel.setDefaultBindingMode(sap.ui.model.BindingMode.OneWay);
			oVizFrame.setModel(dataModel);

			var oPopOver = this.getView().byId("idPopOver");
			oPopOver.connect(oVizFrame.getVizUid());
			oPopOver.setFormatString(formatPattern.STANDARDFLOAT);
			this.initPageSettings(this.getView());
		},
		
		// 목록 콜백
		callbackInitAjaxList : function (oModel) {
			this.getView().setModel(oModel,"view");
		},
		
		jsonData : null,
		
		callbackDataAjaxList : function (oModel) {
			console.error(oModel)
			this.jsonData = this.clone(this.tempJsonData);
			this.getView().setModel(oModel,"list");
			
			
			var resultCode = oModel.oData.code;
			if (resultCode == 200) {
				var result = oModel.oData.result;
				if (result.length > 0) {
					for (var i = 0; i < result.length; i++) {
							this.jsonData.milk.push(result[i]);
					}
				}
				console.error(this.jsonData);

				var dataModel = new JSONModel(this.jsonData);
				this.oVizFrame.setModel(dataModel);
			}else{
				
			}
			
		},
		
		onChangeRadioBtn :function(oEvent) {
			if(oEvent.getParameters().selectedIndex==1){
				 $(".dateAreaVbox1").css("display","none");
				 $(".dateAreaVbox2").css("display","");
				 $(".monthClass").css("display","");
			}else if(oEvent.getParameters().selectedIndex>1){
				$(".dateAreaVbox1").css("display","none");
				$(".dateAreaVbox2").css("display","");
				$(".monthClass").css("display","none");
			}else{
				$(".dateAreaVbox1").css("display","");
				$(".dateAreaVbox2").css("display","none");
				$(".monthClass").css("display","none");
			}
		},
		
		onPressExport: function(oEvent) {
			this.onExport(this.createColumnConfig(), this.getModel().getProperty("/result"), "Sensor Data");
		},
		onSearch: function(oEvent) {
			var data = {};
			
			if(this.getView().byId("__component0---statisticsData--typeD").getSelected()){
				data.type = "D";
			}
			if(this.getView().byId("__component0---statisticsData--typeM").getSelected()){
				data.type = "M";
			}
			if(this.getView().byId("__component0---statisticsData--typeY").getSelected()){
				data.type = "Y";
			}
			
			data.year = this.getView().byId("__component0---statisticsData--dateSearch2--selectYear").getSelectedKey();
			data.month = this.getView().byId("__component0---statisticsData--dateSearch2--selectMonth").getSelectedKey();

			data.deviceMacAddr = this.getView().byId('select1').getSelectedKey();
			data.startDt = this.getView().byId('__component0---statisticsData--dateSearch--dpStart').getValue();
			data.endDt = this.getView().byId('__component0---statisticsData--dateSearch--dpEnd').getValue();
			
			
			this.oParam.type = "get";
			this.oParam.data = data;
			this.oParam.url = "/sensor/statistics";
			this.oParam.callback = "callbackDataAjaxList";
			console.log(this.oParam)
			this.onAjaxCall(this.oParam);
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