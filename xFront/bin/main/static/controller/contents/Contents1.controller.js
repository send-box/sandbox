sap.ui.define([
	"OpenUI5/controller/common/BaseController",
	'sap/ui/core/mvc/Controller',
    'sap/ui/model/BindingMode',
    'sap/ui/model/json/JSONModel',
    'sap/viz/ui5/format/ChartFormatter',
    'sap/viz/ui5/api/env/Format'
], function(BaseController,Controller ,BindingMode ,JSONModel ,ChartFormatter ,Format) {
    "use strict";
        
    return BaseController.extend("OpenUI5.controller.common.BaseController",
    {
        onInit : function ()
        {
        	window.contents1 = this;
            this.localApi();
        },
        callbackFunction : function(oModel)
        {
            //console.log(JSON.stringify(oModel, null, 2));
            
            var oData = oModel.getProperty("/result/list");
            //console.log("oData callbackFunction >>>> "+JSON.stringify(oData, null, 2));
                        
            var oTable = this.byId("idTable");
            oTable.setModel(new JSONModel(oData));
            oTable.setVisibleRowCount(oData.length);
            this.oTableAfterRendering();
        },
        oTableAfterRendering : function(){
        	this.charMeasureNameApi();
        },
        chartCallbackFunction : function(oChartModel){
        	   	
        	Format.numericFormatter(ChartFormatter.getInstance());
            var formatPattern = ChartFormatter.DefaultPattern;

            var oVizFrame = this.oVizFrame = this.getView().byId("idVizFrame");
            oVizFrame.setVizProperties({
                plotArea: {
                    dataLabel: {
                        formatString: formatPattern.SHORTFLOAT_MFD2,
                        visible: true
                    }
                },
                valueAxis: {
                    label: {
                        formatString: formatPattern.SHORTFLOAT
                    },
                    title: {
                        visible: false
                    }
                },
                categoryAxis: {
                    title: {
                        visible: false
                    }
                },
                title: {
                    visible: false,
                    text: 'Revenue by City and Store Name'
                }
            });
            console.log("chartModel11>>>> "+JSON.stringify(oChartModel, null, 2));
            var oChartData = oChartModel.getProperty("/result");
            
            console.log("chartModel22>>>> " +JSON.stringify(oChartData, null, 2));
            oVizFrame.setModel(new JSONModel(oChartData));
            //var aaa = new JSONModel(oChartData)
            //oVizFrame.setModel(new JSONModel(oChartData));

        /*    var oPopOver = this.getView().byId("idPopOver");
            oPopOver.connect(oVizFrame.getVizUid());
            oPopOver.setFormatString(formatPattern.STANDARDFLOAT);*/
            
           /* var that = this;
            dataModel.attachRequestCompleted(function() {
                that.dataSort(this.getData());
            });*/
        },
              
        errorCallbackFunction : function()
        {
            console.log("error callback");
        },
        
        localApi : function()
        {
            var oParam = {
                url     : "http://localhost:8081/list",
                type    : "GET",
                data    : "",
                callback: "callbackFunction",
                error   : "errorCallbackFunction"
            };
            
            this.callAjax(oParam);
        },
        
        callPublicApi : function()
        {
            var oParam = {
                    url     : "http://localhost:8088/",
                    type    : "GET"
                };
          
            var serviceId   = "";
            var operationNm = "";
          
                serviceId   = "ArpltnInforInqireSvc"    ;  // 대기오염정보 조회 서비스
                operationNm = "getCtprvnRltmMesureDnsty";  // 시도별 실시간 측정정보 조회

              //serviceId   = "MsrstnInfoInqireSvc"     ;  // 측정소정보 조회 서비스
              //operationNm = "getNearbyMsrstnList"     ;  // 근접측정소 목록 조회
              //operationNm = "getMsrstnList"           ;  // 측정소 목록 조회
              
            var serviceKey  = "bg9choiwFZX5JYcIIF76jFiVYe0VwiWdxdpCUldbALWxzJLNZA4Ipq2Z1SVqkZyWSW88og%2Bt8EiOCX9J%2BB3ZUw%3D%3D";
            var numOfRows   = "100" ;
            var pageNo      = "1"   ;
            var sidoName    = "서울" ;
            var version     = "1.3" ;
            var returnType  = "json";
          
            oParam.url += "http://openapi.airkorea.or.kr/openapi/services/rest/"
                       + serviceId
                       + "/"
                       + operationNm
                       + "?" + "serviceKey="   + serviceKey
                       + "&" + "numOfRows="    + numOfRows
                       + "&" + "pageNo="       + pageNo
                       + "&" + "sidoName="     + sidoName
                       + "&" + "ver="          + version
                       + "&" +  "_returnType=" + returnType;
          
            this.callAjax2(oParam);
        },
        //측정소별 Chart
        charMeasureNameApi : function()
        {
            var oParam = {
                url     : "http://localhost:8081/list/so2?sido_name=서울&mang_name=도시대기",
                type    : "GET",
                callback: "chartCallbackFunction",
                error   : "errorCallbackFunction"
            };
            
            this.callAjax(oParam);
        },
        
    });
}, true);
