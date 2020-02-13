sap.ui.define([
		"tips/common/controller/BaseController"
	], function (BaseController) {
		"use strict";

		return BaseController.extend("tips.sensor.controller.BaseController", {
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
			}
		});
	}
);