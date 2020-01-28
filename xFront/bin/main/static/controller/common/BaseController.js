sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/core/UIComponent",
    "sap/ui/model/json/JSONModel",
], function (Controller, UIComponent, JSONModel) {
    "use strict";
    
    return Controller.extend("my.application.controller.BaseController", 
    {
        getRouter: function ()
        {
            return UIComponent.getRouterFor(this);
        },
 
        getModel: function (sName)
        {
            return this.getView().getModel(sName);
        },
 
        setModel: function (oModel, sName)
        {
            return this.getView().setModel(oModel, sName);
        },
 
        getResourceBundle: function ()
        {
            return this.getOwnerComponent().getModel("i18n").getResourceBundle();
        },
        
        callAjax : function(oParam)
        {  
            console.log("BaseController callAjax() oParam.url: " + oParam.url);
            
            var that   = this;
            var oModel = new sap.ui.model.json.JSONModel();
            
            jQuery.ajax({
                type        : oParam.type,
                data        : JSON.stringify(oParam.data),
                contentType : "application/json",
                url         : oParam.url,
                dataType    : "json",
                success     : 
                    function(oData, textStatus, jqXHR)
                    {
                        var oModel = new JSONModel();
                        oModel.setData(oData); 
                    
                        var proxyFunc = jQuery.proxy(that, oParam.callback, oModel);
                        proxyFunc();
                    },
                error       : 
                    function()
                    {
                        var proxyFunc = jQuery.proxy(that, oParam.error);
                        proxyFunc();
                    }
            });
        },
        
        callAjax2 : function (oParam)
        {
            console.log("callAjax2()");
            
            $.ajax({
            	"url"      : oParam.url,
            	"type"     : oParam.type,
                "success"  :
                    function (result)
                    {
                        if (result == null || result == "")
                        {
                            console.log("해당 주소로 얻을수 있는 좌표가 없습니다. 주소값을 다시 입력하세요");
                        }
                        else
                        {
                            $.each(result, function(i, value)
                            {
                                var oModel = new JSONModel();
                                oModel.setData(value); 

                                console.log("Response : " + i);
                              //console.log(JSON.stringify(oModel, null, 2));
                                console.log(JSON.stringify(value , null, 2));
                      
                                if (result.data == null) 
                                {
                                    if (i == 0) 
                                    {
                                        $("#x_coords").attr("value", value.posX   ); 
                                        $("#y_coords").attr("value", value.posY   ); 
                                        $("#address" ).attr("value", value.address);
                                    }
                                }
                            });
                        }
                        
                        console.log("End of Callback");
                    },
                "async"    : "false",
                "dataType" : "json",
                "error"    :
                    function(x, o, e) 
                    {
                        console.log(x.status + " : " + o + " : " + e);    
                    }
            });
        }
    });
});