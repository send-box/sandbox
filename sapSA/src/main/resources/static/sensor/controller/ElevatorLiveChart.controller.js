sap.ui.define([
	'jquery.sap.global',
	"tips/sensor/controller/BaseController",
	"sap/m/MessageToast",
	'sap/ui/model/json/JSONModel',
	'sap/viz/ui5/data/FlattenedDataset',
	'sap/viz/ui5/format/ChartFormatter',
	'sap/viz/ui5/api/env/Format'
], function(jQuery, BaseController, MessageToast, JSONModel, FlattenedDataset, ChartFormatter, Format) {
	"use strict";

	return BaseController.extend("tips.sensor.controller.ElevatorLiveChart", {
		dataPath : "elevator/model",

		tempJsonData : {
			milk : [ 
			]
		},

		settingsModel : {
			deviceSet : {
				name : "DeviceSet",
				defaultSelected : 1,
				values : [ {
					name : "에러부품수",
					value : [ "componentCnt" ]
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
				name : 'build',
				value : "{buildingNm}{elevatorNo}",
			} ],
			measures : [ {
				name : 'componentCnt',
				value : '{componentCnt}'
			} ]
		},

		oVizFrame : null,

		oParam : {
			url : "",
			data : {},
			type : "",
			callback : ""
		},

		jsonData : null,
		
		/**
		 * callback
		 */
		fnDataSetCallback : function(oData) {
			var rtnData = oData.getData();

			var resultCode = rtnData.code;
			if (resultCode == 200) {
				var result = rtnData.result;
				if (result.length > 0) {
					this.jsonData.milk.splice(0,24);
					for (var i = 0; i < result.length; i++) {
							this.jsonData.milk.push(result[i]);
					}
				}
//				console.error(this.jsonData)

				var dataModel = new JSONModel(this.jsonData);
				this.oVizFrame.setModel(dataModel);
			}else{
				
			}
		},
		dataInterval : null,
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

		clone : function(obj) {
			if (obj === null || typeof (obj) !== 'object')
				return obj;
			var copy = obj.constructor();
			for (var attr in obj) {
				if (obj.hasOwnProperty(attr)) {
					copy[attr] = this.clone(obj[attr]);
				}
			}
			return copy;
		},

		onInit : function() {
			this.jsonData = this.clone(this.tempJsonData);
			this.oParam.url = "/elevator/liveData2";
			this.oParam.type = "get";
			this.oParam.callback = "fnDataSetCallback";
			var that = this;
			that.onAjaxCall(that.oParam);
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
//				   console.error("onBeforeShow");
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

			Format.numericFormatter(ChartFormatter.getInstance());
			var formatPattern = ChartFormatter.DefaultPattern;
			// set explored app's demo model on this sample
			var oModel = new JSONModel(this.settingsModel);
			oModel.setDefaultBindingMode(sap.ui.model.BindingMode.OneWay);
			this.getView().setModel(oModel);

			var oVizFrame = this.oVizFrame = this.getView().byId("idVizFrame");

//			console.error(oVizFrame)
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
						visible : true,
						text : '수량'
					}
				},
				categoryAxis : {
					title : {
						visible : true,
						text : '엘리베이터명'
					}
				},
				title : {
					visible : true,
					text : '엘리베이터 실시간 고장 부품 수'
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
		press : function(oEvent) {
			MessageToast.show("The Line Micro Chart has been pressed.");
		},
		onDataLabelChanged : function(oEvent) {
			if (this.oVizFrame) {
				this.oVizFrame.setVizProperties({
					plotArea : {
						dataLabel : {
							visible : oEvent.getParameter('state')
						}
					}
				});
			}
		},
		onAxisTitleChanged : function(oEvent) {
			if (this.oVizFrame) {
				var state = oEvent.getParameter('state');
				this.oVizFrame.setVizProperties({
					valueAxis : {
						title : {
							visible : state
						}
					},
					categoryAxis : {
						title : {
							visible : state
						}
					}
				});
			}
		}
	});

}, /* bExport= */ true);