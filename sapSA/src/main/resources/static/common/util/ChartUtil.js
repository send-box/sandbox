sap.ui.define(["sap/viz/ui5/data/FlattenedDataset", "sap/viz/ui5/controls/common/feeds/FeedItem"], 
		function(FlattenedDataset, FeedItem) {
	"use strict";
	return {
		/**
		 * set property VizFrame chart.
		 * oVizFrame 작성은 Trend.controller.js 참고
		 * Chart Property Reference: https://sapui5.hana.ondemand.com/docs/vizdocs/index.html
		 */
		setVizFrame: function(oVizFrame, v, vizFrame) {
			if(undefined==vizFrame||null==vizFrame){ vizFrame = v.byId(oVizFrame.id); }
			vizFrame.setVizProperties(oVizFrame.properties);
			vizFrame.setDataset(new FlattenedDataset(oVizFrame.dataset));
			vizFrame.setVizType(oVizFrame.type);
			for (var i = 0; i < oVizFrame.feedItems.length; i++) {vizFrame.addFeed(new FeedItem(oVizFrame.feedItems[i]));}
		},
		resetSlider: function(oVizFrame, v, oModel) {
			var oslider = v.byId(oVizFrame.sliderId);
			oslider.applySettings();
			oslider.setVizType(oVizFrame.sliderType);
			oslider.setValueAxisVisible(false);
			oslider.setModel(oModel);
			oslider.setDataset(new FlattenedDataset(oVizFrame.silderDataset));
			oslider.setLayoutData(new sap.m.FlexItemData({
				maxHeight: '14%', baseSize: '100%', order: 1, styleClass: 'rangeSliderPadding'
			}));
			for (var i = 0; i < oVizFrame.sliderFeedItems.length; i++) {
				oslider.addFeed(new FeedItem(oVizFrame.sliderFeedItems[i]));
			}
		}
	};
});