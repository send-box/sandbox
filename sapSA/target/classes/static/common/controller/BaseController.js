sap.ui.define([
		"sap/ui/core/mvc/Controller",
		"sap/ui/model/json/JSONModel",
		"sap/m/MessageToast",
		"sap/m/MessageBox",
		'sap/ui/model/SimpleType',
		"sap/ui/export/Spreadsheet"
	], function (Controller, JSONModel, MessageToast, MessageBox, SimpleType, Spreadsheet) {
		"use strict";

		return Controller.extend("tips.common.controller.BaseController", {
			/**
			 * Convenience method for getting the manifest.json file entry value.
			 * @public
			 * @returns {string} manifest.json file entry value
			 */
			getManifestEntry : function (sEntry) {
				return this.getOwnerComponent().getManifestEntry(sEntry);
			},

			/**
			 * Convenience method for getting the session value.
			 * @public
			 * @returns {string} the session value
			 */
			getSessionValue : function (sKey) {
				return window.sessionStorage.getItem(sKey);
			},
			
			/**
			 * Convenience method for setting the session value.
			 * @public
			 */
			setSessionValue : function (sKey, sVal) {
				window.sessionStorage.setItem(sKey, sVal);
			},

			/**
			 * Convenience method for accessing the router in every controller of the application.
			 * @public
			 * @returns {sap.ui.core.routing.Router} the router for this component
			 */
			getRouter : function () {
				return this.getOwnerComponent().getRouter();
			},

			/**
			 * Convenience method for getting the view model by name in every controller of the application.
			 * @public
			 * @param {string} sName the model name
			 * @returns {sap.ui.model.Model} the model instance
			 */
			getModel : function (sName) {
				return this.getView().getModel(sName);
			},

			/**
			 * Convenience method for setting the view model in every controller of the application.
			 * @public
			 * @param {sap.ui.model.Model} oModel the model instance
			 * @param {string} sName the model name
			 * @returns {sap.ui.mvc.View} the view instance
			 */
			setModel : function (oModel, sName) {
				return this.getView().setModel(oModel, sName);
			},

			/**
			 * Convenience method for getting the resource bundle.
			 * @public
			 * @returns {sap.ui.model.resource.ResourceModel} the resourceModel of the component
			 */
			getResourceBundle : function () {
				return this.getOwnerComponent().getModel("i18n").getResourceBundle();
			},

			getResourceBundleCommon : function () {
				return this.getOwnerComponent().getModel("i18nCommon").getResourceBundle();
			},

			showMsgToast : function (oText) {
				MessageToast.show(oText);
//				MessageToast.show(oText, {duration: 1000, at: 'center middle'});
			},

			/**
			 * Event handler for navigating back.
			 * It there is a history entry we go one step back in the browser history
			 * If not, it will replace the current entry of the browser history with the master route.
			 * @public
			 */
			onNavBack : function() {
				var sPreviousHash = sap.ui.core.routing.History.getInstance().getPreviousHash();

				if (sPreviousHash !== undefined) {
					history.go(-1);
				} else {
					this.getRouter().navTo("sensorData");
				}
			},

			/**
			 * Event handler for ajax call.
			 * example(controller에서 호출)
			// onInit 내에서 작성
			var oParam = {
				url: "https://sapui5.hana.ondemand.com/sdk/test-resources/sap/ui/demokit/explored/products.json",
				data: {tag_id: "111", start_date: "", end_date: ""},
				type: "get",
				callback: "callbackAjax"
			};
			this.onAjaxCall(oParam);

			// 원하는 callback 함수 작성
			callbackAjax : function (oModel) {
				this.setModel(oModel, "name");
				jQuery.sap.log.info("successed to callback");
			}
			 */
			onAjaxCall : function(oParam) {
				if (!oParam.callback) { return; }
				var that = this;
				var _oData = oParam.data || '';
				var _sType = oParam.type || "get";

				if(_sType.toLowerCase() != 'get' && typeof _oData == 'object') {
					_oData = JSON.stringify(_oData);
				}

				jQuery.ajax({
					type : _sType,
					data : _oData,
					dataType : "json",
					contentType : "application/json; charset=utf-8",
					mediatype : "application/json",
					url : oParam.url,
					success : function(oData) {
						if(oData.status && oData.status == 'fail') {
							jQuery.sap.log.error(jQuery.sap.formatMessage("Api response code: {0}, message: {1}", [oData.code, oData.message]));
							
							var errMsg = 'apiErr_' + (oData.message || "Server error.");
							if (!that.getSessionValue(errMsg)) { that.setSessionValue(errMsg, 0); }
							if (that.getSessionValue(errMsg) == 0) {
								that.onOpenApiMessageBox(errMsg);
							}
							
							sap.ui.core.BusyIndicator.hide();

							return;
						}

						var oModel = new JSONModel();
						oModel.setData(oData);

						var proxyFunc = jQuery.proxy(that, oParam.callback, oModel);
						proxyFunc();

						jQuery.sap.log.info("Successed to api execution");
					},
					error : function(e) {
						sap.ui.core.BusyIndicator.hide();
						
						var errMsg = null;
					    if(e.responseBody) {
					    	errMsg = e.responseBody;
					    } else {
					    	errMsg = e.responseText;
					    }
					    jQuery.sap.log.info('errMsg: ' + errMsg);

					    try {
							var returnJsonObj = JSON.parse(errMsg);
							if(returnJsonObj) {
								jQuery.sap.log.error("Failed to api execution: " + returnJsonObj.error);
								
								errMsg = "apiErr_Failed to api execution: " + returnJsonObj.error;
								if (!that.getSessionValue(errMsg)) { that.setSessionValue(errMsg, 0); }
								if (that.getSessionValue(errMsg) == 0) {
									that.onOpenApiMessageBox(errMsg);
								}
							}
						} catch(e) {
							jQuery.sap.log.error("Api execution failed.");
							
							errMsg = "apiErr_Api execution failed.";
							if (!that.getSessionValue(errMsg)) { that.setSessionValue(errMsg, 0); }
							if (that.getSessionValue(errMsg) == 0) {
								that.onOpenApiMessageBox(errMsg);
							}
						}
					}
				});
			},
			
			// API Error 관련 메시지 창 띄우기
			onOpenApiMessageBox : function (sMessage) {
				var that = this;
				that.setSessionValue(sMessage, eval(that.getSessionValue(sMessage)) + 1);
				
				MessageBox.error(sMessage.replace('apiErr_', ''), {
					onClose: function (oAction) {
						that.setSessionValue(sMessage, eval(that.getSessionValue(sMessage)) - 1);
					}
				});
			},

			// 이메일 체크 Validation
			customEMailType :
				SimpleType.extend("email", {
					formatValue: function (oValue) {
						return oValue;
					},
					parseValue: function (oValue) {
						//parsing step takes place before validating step, value could be altered here
						return oValue;
					},
					validateValue: function (oValue) {
						// The following Regex is NOT a completely correct one and only used for demonstration purposes.
						// RFC 5322 cannot even checked by a Regex and the Regex for RFC 822 is very long and complex.
						var rexMail = /^\w+[\w-+\.]*\@\w+([-\.]\w+)*\.[a-zA-Z]{2,}$/;
						if (!oValue.match(rexMail)) {
							throw new sap.ui.model.ValidateException("Enter a valid email address.");
						}
					}
				})
			,
			/**
			 * export Spreadsheet.
			 * oVizFrame 작성은 Trend.controller.js 참고
			 */
			onExport: function(aCols, aProducts, _aTitle) {
				var oSettings = {workbook: {columns: aCols}, dataSource: aProducts, fileName: _aTitle};
				new Spreadsheet(oSettings).build().then(function(){MessageToast.show("Spreadsheet export has finished");});
			},
			/**
			 * print current content.
			 */
			onPrint: function() {
				window.print();
			},
			setPeriodD: function(s,start,end){
				var dpStart=start;
				var dpEnd=end;
				if(typeof s != "string"){
					dpStart = s.getSource().oParent.oParent.mAggregations.items[1].mAggregations.items[0];
					dpEnd = s.getSource().oParent.oParent.mAggregations.items[1].mAggregations.items[2];
					s = s.getSource().data("type");
				}
				var d = new Date();
				dpEnd.setValue(d.getFullYear()+"."+(d.getMonth()>8?(d.getMonth()+1):"0"+(d.getMonth()+1))+
						"."+(d.getDate()>9?d.getDate():("0"+d.getDate())));
				if(s=="d")			{ d.setDate(d.getDate()-1); }
				else if(s=="w")		{ d.setDate(d.getDate()-7); }
				else if(s=="3w")	{ d.setDate(d.getDate()-21); }
				else if(s=="m")		{ d.setMonth(d.getMonth()-1); }
				else if(s=="3m")	{ d.setMonth(d.getMonth()-3); }
				else if(s=="6m")	{ d.setMonth(d.getMonth()-6); }
				dpStart.setValue(d.getFullYear()+"."+(d.getMonth()>8?(d.getMonth()+1):"0"+(d.getMonth()+1))+
						"."+(d.getDate()>9?d.getDate():("0"+d.getDate())));
			},
			dpHandleChange: function (oEvent) {
				if (oEvent.getParameter("valid")) {
					oEvent.oSource.setValueState(sap.ui.core.ValueState.None);
				} else {
					oEvent.oSource.setValueState(sap.ui.core.ValueState.Error);
				}
			}
		});
	}
);