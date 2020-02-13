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

	return BaseController.extend("tips.sensor.controller.SparkRealtimeSensor", {
		dataPath : "sensor/model",

		tempJsonData : {
			milk : [ {
				time : "10",
				temperature : 0,
				illuminance : 0,
				humidity : 0,
				device_mac_addr : "1"
				},
				{
					time : "9",
					temperature : 0,
					illuminance : 0,
					humidity : 0,
					device_mac_addr : "1"
				},
				{
					time : "8",
					temperature : 0,
					illuminance : 0,
					humidity : 0,
					device_mac_addr : "1"
				},
				{
					time : "7",
					temperature : 0,
					illuminance : 0,
					humidity : 0,
					device_mac_addr : "1"
				},
				{
					time : "6",
					temperature : 0,
					illuminance : 0,
					humidity : 0,
					device_mac_addr : "1"
				},
				{
					time : "5",
					temperature : 0,
					illuminance : 0,
					humidity : 0,
					device_mac_addr : "1"
				},
				{
					time : "4",
					temperature : 0,
					illuminance : 0,
					humidity : 0,
					device_mac_addr : "1"
				},
				{
					time : "3",
					temperature : 0,
					illuminance : 0,
					humidity : 0,
					device_mac_addr : "1"
				},
				{
					time : "2",
					temperature : 0,
					illuminance : 0,
					humidity : 0,
					device_mac_addr : "1"
				},
				{
					time : "1",
					temperature : 0,
					illuminance : 0,
					humidity : 0,
					device_mac_addr : "1"
				}
			]
		},

		settingsModel : {
			deviceSet : {
				name : "DeviceSet",
				defaultSelected : 1,
				values : [ {
					name : "온도",
					value : [ "temperature" ]
				}, {
					name : "조도",
					value : [ "illuminance" ]
				}, {
					name : "습도",
					value : [ "humidity" ]
				} ]
			},
			dataset : {
				name : "Dataset",
				defaultSelected : 0,
				values : [ {
					name : "C5:34:78:B2:45:55",
					value : "C5:34:78:B2:45:55"
				}, {
					name : "EE:AB:94:BE:19:A9",
					value : "EE:AB:94:BE:19:A9"
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
				name : 'time',
				value : "{time}",
			} ],
			measures : [ {
				name : 'temperature',
				value : '{temperature}'
			}, {
				name : 'illuminance',
				value : '{illuminance}'
			}, {
				name : 'humidity',
				value : '{humidity}'
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

			var datasetRadio = this.getView().byId('select1').getSelectedItem();
			var bindValue = datasetRadio.getBindingContext().getObject();
			var resultCode = rtnData.code;
			if (resultCode == 200) {
				var result = rtnData.result;
				if (result.length > 0) {
					for (var i = 0; i < result.length; i++) {
						if (result[i].device_mac_addr == bindValue.name) {
							this.jsonData.milk.splice(0, 1);
							this.jsonData.milk.push(result[i]);
						}
					}
				} else {
					var date = new Date();
					var nonData = {
						time : date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds(),
						temperature : 0,
						illuminance : 0,
						humidity : 0,
						device_mac_addr : bindValue.name
					}
					this.jsonData.milk.splice(0, 1);
					this.jsonData.milk.push(nonData);
				}
				console.error(this.jsonData);
				console.error(this.tempJsonData);

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
			this.oParam.url = "/spark/data";
			this.oParam.type = "get";
			this.oParam.callback = "fnDataSetCallback";
			var that = this;
			
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
				   console.error("onBeforeShow");
				   that.dataInterval = setInterval(function() {
						that.onAjaxCall(that.oParam);
					}, 5000);
			   },
			   onAfterShow: function(oEvent) {
				   console.error("onAfterShow");
			   },
			   onAfterHide: function(oEvent) {
				   console.error("onAfterHide");
				   clearInterval(that.dataInterval);
			   }
			});
//			this.dataInterval = setInterval(function() {
//				that.onAjaxCall(that.oParam);
//			}, 5000);
			//			this.onAjaxCall(this.oParam);

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
				valueAxis2 : {
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
		press : function(oEvent) {
			MessageToast.show("The Line Micro Chart has been pressed.");
		},
		onChangeChk : function(oEvent) {
			var chk1 = this.getView().byId("chk1").getSelected();
			var chk2 = this.getView().byId("chk2").getSelected();
			var chk3 = this.getView().byId("chk3").getSelected();

			var cnt1 = 0;
			var cnt2 = 0;
			if (chk1) {
				cnt2++;
			}
			if (chk3) {
				cnt1++;
			}
			if (chk2) {
				cnt1++;
			}

			var test1 = new Array(cnt1);
			var n = 0;
			if (chk3) {
				test1[n] = "illuminance";
				n++;
			}
			if (chk2) {
				test1[n] = "humidity";
				n++;
			}
			var test2 = new Array(cnt2);
			if (chk1) {
				test2[0] = "temperature";
			}

			var feedValueAxis = this.getView().byId('valueAxisFeed');

			this.oVizFrame.removeFeed(feedValueAxis);
			feedValueAxis.setValues(test1);
			this.oVizFrame.addFeed(feedValueAxis);
			
			var feedValueAxis2 = this.getView().byId('valueAxisFeed2');
			
			this.oVizFrame.removeFeed(feedValueAxis2);
			feedValueAxis2.setValues(test2);
			this.oVizFrame.addFeed(feedValueAxis2);

		},
		onChangeDevice : function(oEvent) {
			this.jsonData = this.clone(this.tempJsonData);
			var datasetRadio = oEvent.getSource().getSelectedItem();
			var bindValue = datasetRadio.getBindingContext().getObject();

			console.error(bindValue)
			var dataset = {
				data : {
					path : "/milk"
				}
			};
			var dim = this.settingsModel.dimensions;
			dataset.dimensions = dim;
			dataset.measures = this.settingsModel.measures;
			var oDataset = new FlattenedDataset(dataset);
			this.oVizFrame.setDataset(oDataset);
			var dataModel = new JSONModel(this.tempJsonData);
			this.oVizFrame.setModel(dataModel);

			var feedCategoryAxis = this.getView().byId('categoryAxisFeed');
			this.oVizFrame.removeFeed(feedCategoryAxis);
			var feed = [];
			for (var i = 0; i < dim.length; i++) {
				feed.push(dim[i].name);
			}
			feedCategoryAxis.setValues(feed);
			this.oVizFrame.addFeed(feedCategoryAxis);
		},
		
		onAfterRendering : function() {
			//			var deviceSelect = this.getView().byId('select1');
			//			deviceSelect.getSelectedKey("--전체--");
			//
			//			var datasetRadioGroup = this.getView().byId('datasetRadioGroup');
			//			datasetRadioGroup.setSelectedIndex(this.settingsModel.dataset.defaultSelected);

			//			var seriesRadioGroup = this.getView().byId('seriesRadioGroup');
			//			seriesRadioGroup.setSelectedIndex(this.settingsModel.series.defaultSelected);
		},
		//		onDatasetSelected : function(oEvent) {
		//			this.jsonData = null;
		//			this.jsonData = this.tempJsonData;
		//			var datasetRadio = oEvent.getSource();
		//			if (this.oVizFrame && datasetRadio.getSelected()) {
		//				var bindValue = datasetRadio.getBindingContext().getObject();
		//				var dataset = {
		//					data : {
		//						path : "/milk"
		//					}
		//				};
		//				var dim = this.settingsModel.dimensions;
		//				dataset.dimensions = dim;
		//				dataset.measures = this.settingsModel.measures;
		//				var oDataset = new FlattenedDataset(dataset);
		//				this.oVizFrame.setDataset(oDataset);
		//				var dataModel = new JSONModel(this.dataPath + bindValue.value);
		//				this.oVizFrame.setModel(dataModel);
		//
		//				var feedCategoryAxis = this.getView().byId('categoryAxisFeed');
		//				this.oVizFrame.removeFeed(feedCategoryAxis);
		//				var feed = [];
		//				for (var i = 0; i < dim.length; i++) {
		//					feed.push(dim[i].name);
		//				}
		//				
		//				console.error(dataset)
		//				feedCategoryAxis.setValues(feed);
		//				this.oVizFrame.addFeed(feedCategoryAxis);
		//			}
		//		},
		//		onSeriesSelected : function(oEvent) {
		//			var seriesRadio = oEvent.getSource();
		//			if (this.oVizFrame && seriesRadio.getSelected()) {
		//				var bindValue = seriesRadio.getBindingContext().getObject();
		//
		//				var feedValueAxis = this.getView().byId('valueAxisFeed');
		//				this.oVizFrame.removeFeed(feedValueAxis);
		//				feedValueAxis.setValues(bindValue.value);
		//				this.oVizFrame.addFeed(feedValueAxis);
		//			}
		//		},
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