sap.ui.define([
		"tips/sensor/controller/BaseController",
		"sap/ui/model/json/JSONModel",
		"sap/m/MessageBox",
		'sap/viz/ui5/data/FlattenedDataset',
		'sap/viz/ui5/format/ChartFormatter',
		'sap/viz/ui5/api/env/Format',
		'tips/common/util/DateUtil',
	], function (BaseController, JSONModel, MessageBox, FlattenedDataset, ChartFormatter, Format, DateUtil) {
		"use strict";

	return BaseController.extend("tips.sensor.controller.ElevatorStatisticsData", {

		oParam: {
		},
		
		tempJsonData : {
			milk : [ 
			]
		},

		settingsModel : {
			dimensions : [ {
				name : 'date',
				value : "{date}",
			} ],
			measures : [ {
				name : 'c1',
				value : '{c1}'
			}, {
				name : 'c2',
				value : '{c2}'
			}]
		},

		oVizFrame : null, 
		oVizFrame2 : null, 
		jsonData : null,

		/**
		 * 차트 초기화( 고장횟수[idVizFrame], 고장시간[idVizFrame2])
		 */
		initPageSettings : function(oView) {
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

				var vizframe2 = oView.byId("idVizFrame2");
				var oChartContainerContent2 = new sap.suite.ui.commons.ChartContainerContent({
					icon : "sap-icon://line-chart",
					title : "vizFrame Line Chart Sample",
					content : [ vizframe2 ]
				});
				var oChartContainer2 = new sap.suite.ui.commons.ChartContainer({
					content : [ oChartContainerContent2 ]
				});
				oChartContainer2.setShowFullScreen(true);
//				oChartContainer.setAutoAdjustHeight(true);
				oView.byId('chartFixFlex2').addContent(oChartContainer2);
			}
		},
		
		// 화면 초기설정
		onInit : function () {
//			this.jsonData = this.clone(this.tempJsonData);

			//조회구분 기본선택 건물별
			this.onChangeSearchType("B");
			
			//조회일자  기본설정 현재일(시작일,종료일)
			this.getView().byId("__component0---elevatorStatisticsData--dateSearch--dpStart").setValue(DateUtil.getToday("."));
			this.getView().byId("__component0---elevatorStatisticsData--dateSearch--dpEnd").setValue(DateUtil.getToday("."));
			
			//차트 기본 세팅 설정
			this.initChart();
		},
		
		onAfterRendering : function(){
			//렌더링 후 날짜 조회 영역 비활성화 처리
			 $(".dateAreaVbox2").css("display","none");
		},
		
		// 차트 초기설정
		initChart : function () {
			Format.numericFormatter(ChartFormatter.getInstance());
			var formatPattern = ChartFormatter.DefaultPattern;
			// set explored app's demo model on this sample
			var oModel = new JSONModel(this.settingsModel);
			oModel.setDefaultBindingMode(sap.ui.model.BindingMode.OneWay);
			this.getView().setModel(oModel);

			var oVizFrame = this.oVizFrame = this.getView().byId("idVizFrame");	//고장횟수 차트
			var oVizFrame2 = this.oVizFrame2 = this.getView().byId("idVizFrame2");	//고장시간 차트

			var dataModel = new JSONModel(this.jsonData);
			oVizFrame.setModel(dataModel);	//데이터 모델 초기화
			oVizFrame2.setModel(dataModel);	//데이터 모델 초기화

			var oPopOver = this.getView().byId("idPopOver");	//차트 버튼
			oPopOver.connect(oVizFrame.getVizUid());
			oPopOver.setFormatString(formatPattern.STANDARDFLOAT);
			var oPopOver2 = this.getView().byId("idPopOver2");
			oPopOver2.connect(oVizFrame2.getVizUid());
			oPopOver2.setFormatString(formatPattern.STANDARDFLOAT);
			this.initPageSettings(this.getView());
		},
		
		// 조회구분 selectBox Data Binding
		callbackInitAjaxList : function (oModel) {
			this.getView().setModel(oModel,"view");
			this.getView().byId('select1').setSelectedKey("-전체-");	//조회구분 변경시 선택박스 인덱스 초기화
		},
		
		// 조회구분 설정 변경
		onChangeSearchType : function(gubun){
			this.oParam.type = "get";
			this.oParam.data = {gubun:gubun};
			this.oParam.url = "/elevator/device";
			this.oParam.callback = "callbackInitAjaxList";
			this.onAjaxCall(this.oParam);
		},
		
		//조회 후처리
		callbackDataAjaxList : function (oModel) {
			
			//조회 조건에 따른 리스트 헤더 숨김처리
			if(this.getView().byId('__component0---elevatorStatisticsData--gubunRadiobtnGrp').getSelectedIndex()==0){
				this.getView().byId('td1').setVisible(true);
				this.getView().byId('td2').setVisible(false);
				this.getView().byId('td3').setVisible(false);
				this.getView().byId('td7').setVisible(true);
				this.getView().byId('td8').setVisible(true);
				this.getView().byId('td9').setVisible(false);
				this.getView().byId('td10').setVisible(false);
			}else if(this.getView().byId('__component0---elevatorStatisticsData--gubunRadiobtnGrp').getSelectedIndex()==1){
				this.getView().byId('td1').setVisible(false);
				this.getView().byId('td2').setVisible(true);
				this.getView().byId('td3').setVisible(false);
				this.getView().byId('td7').setVisible(true);
				this.getView().byId('td8').setVisible(true);
				this.getView().byId('td9').setVisible(false);
				this.getView().byId('td10').setVisible(false);
			}else if(this.getView().byId('__component0---elevatorStatisticsData--gubunRadiobtnGrp').getSelectedIndex()==2){
				this.getView().byId('td1').setVisible(false);
				this.getView().byId('td2').setVisible(false);
				this.getView().byId('td3').setVisible(true);
				this.getView().byId('td4').setVisible(false);
				this.getView().byId('td7').setVisible(false);
				this.getView().byId('td8').setVisible(false);
				this.getView().byId('td9').setVisible(true);
				this.getView().byId('td10').setVisible(true);
			}
			
			this.getView().setModel(oModel,"list");

			this.jsonData = this.clone(this.tempJsonData);
			
			var resultCode = oModel.oData.code;
			if (resultCode == 200) {
				//차트 새로 고침
				this.createChart(oModel, this.oVizFrame, oModel.oData.result.chart_info, "엘리베이터 고장횟수", "valueAxisFeed");
				this.createChart(oModel, this.oVizFrame2, oModel.oData.result.chart_info2, "엘리베이터 고장시간", "valueAxisFeed2");
			}else{
			}
			
		},
		
		//차트 설정 
		createChart : function(oModel, chart, resultData, title, valueAxisFeedIdName){
			var chartData = this.clone(this.tempJsonData);	//데이터 초기화
			var result = resultData;
			if (result.length > 0) {
				for (var i = 0; i < result.length; i++) {
//					console.error(result[i])
						chartData.milk.push(result[i]);	//조회 결과 
				}
			}
			var feedSet = this.getView().getModel("view").oData.result;
			var formatPattern = ChartFormatter.DefaultPattern;
            chart.destroyDataset();	//차트 데이터 설정 초기화
            chart.setVizType("column");	//차트 타입 막대형 
            var dataModel = new JSONModel(chartData);
            chart.setModel(dataModel);	//차트 데이터 연결
            chart.setVizProperties({	//차트 속성 설정
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
					visible : true,
					text : title
				}
			});
            var dataset = {
				data : {
					path : "/milk"
				}
			};
			dataset.dimensions = this.settingsModel.dimensions;	//차트 x축 설정
			
			var measuresNameArr = new Array();	//차트 시리즈 목록
			var measuresListArr = new Array();	//차트 y축 설정
			for(var i=1; i<feedSet.length;i++){
				var measures = new Object();
				
				if(this.getView().byId('__component0---elevatorStatisticsData--gubunRadiobtnGrp').getSelectedIndex()==2){
					
					measures.name = feedSet[i].deviceKey+feedSet[i].deviceValue;
					measures.value="{"+feedSet[i].deviceKey+feedSet[i].deviceValue+"}"
				}else{
					
					measures.name = feedSet[i].deviceValue;
					measures.value="{"+feedSet[i].deviceValue+"}"
				}
				
				measuresListArr.push(measures);
				measuresNameArr.push(measures.name);
				
			}
//			console.error(measuresListArr)
			dataset.measures = measuresListArr;
			var oDataset = new FlattenedDataset(dataset);
			chart.setDataset(oDataset);
			
			var feedValueAxis = this.getView().byId(valueAxisFeedIdName);

			chart.removeFeed(feedValueAxis);//기존 차트 feed 설정 제거
			feedValueAxis.setValues(measuresNameArr);
			chart.addFeed(feedValueAxis);	//설정된 차트 feed 추가
		},
		
		//구분  라디오 버튼 변경 이벤트
		onChangeGubunRadioBtn :function(oEvent) {
			var gubun;
			switch(oEvent.getParameters().selectedIndex){
			case 0 :
				gubun = "B";
				break;
			case 1 :
				gubun = "O";
				break;
			case 2 :
				gubun = "C";
				break;
			}
			
			this.onChangeSearchType(gubun);
		},
		
		//유형 라디오 버튼 변경 이벤트
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
		
		//조회하기
		onSearch: function(oEvent) {
			var data = {};
			
			if(this.getView().byId("__component0---elevatorStatisticsData--typeB").getSelected()){
				data.gubun = "B";
			}
			if(this.getView().byId("__component0---elevatorStatisticsData--typeO").getSelected()){
				data.gubun = "O";
			}
			if(this.getView().byId("__component0---elevatorStatisticsData--typeC").getSelected()){
				data.gubun = "C";
			}
			
			if(this.getView().byId("__component0---elevatorStatisticsData--typeD").getSelected()){
				data.type = "D";
			}
			if(this.getView().byId("__component0---elevatorStatisticsData--typeM").getSelected()){
				data.type = "M";
			}
			if(this.getView().byId("__component0---elevatorStatisticsData--typeY").getSelected()){
				data.type = "Y";
			}
			
			data.year = this.getView().byId("__component0---elevatorStatisticsData--dateSearch2--selectYear").getSelectedKey();
			data.month = this.getView().byId("__component0---elevatorStatisticsData--dateSearch2--selectMonth").getSelectedKey();

			data.deviceKey = this.getView().byId('select1').getSelectedKey();
			data.startDt = this.getView().byId('__component0---elevatorStatisticsData--dateSearch--dpStart').getValue();
			data.endDt = this.getView().byId('__component0---elevatorStatisticsData--dateSearch--dpEnd').getValue();
			
			
			this.oParam.type = "get";
			this.oParam.data = data;
			this.oParam.url = "/elevator/statistics";
			this.oParam.callback = "callbackDataAjaxList";
			this.onAjaxCall(this.oParam);
		}
	});

}, /* bExport= */ true);