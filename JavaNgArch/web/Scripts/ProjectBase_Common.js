(function (String, angular) {
    'use strict';

    var pbm = angular.module('projectbase', ['ui.router', 'ui.bootstrap', 'jcs-autoValidate', 'pascalprecht.translate', 'ngCookies', 'ngAnimate']);
  //application-wide logic
    pbm.controller("BaseCtrl", ['$rootScope', '$scope', '$window', '$state','$transitions', 'pb','pbui' ,'$translate','$log', 'App_Dict', 'App_MenuData','App_FuncTree',
                                function ($rootScope, $scope, $window, $state,$transitions, pb,pbui, $translate,$log, App_Dict, App_MenuData,App_FuncTree) {
    	var c = this;
    	$rootScope.Dict=App_Dict;
    	$rootScope.pbvar={};//pb通过$rootScope.pbvar绑定全局数据到pbui
    	$rootScope.pbvar.ShowProcessing=false;
    	$rootScope.pbvar.ProcessingRect={};
    	
    	c.Lang = 'zh-cn'; //上次翻译使用的语言
    	c.SetLang = function (lang) {
    		c.Lang = lang;
    	};
    	$state.defaultErrorHandler(function(error) {
      		if (error && error.isRcResult == false) {
    			//already done ExecuteErrorResult
    		} else {
    			$log.error(error);
    		}
	        pbui.PutProcessing(false);
    	});
    	$transitions.onStart({}, function(trans) {
    		pb.AjaxNavRegister(trans,true);
		    pbui.PutProcessing("_view");
		    pbui.PutProcessing(false);
    	  });
    	$transitions.onSuccess({}, function(trans) {
	        c.SyncMenuToState(trans.to());
	        pb.AjaxNavRegister(trans);
	        c.TranslateStateName(trans.to(), trans.params());
	        pbui.PutProcessing(false);
    	  });
       c.TranslateStateName = function (state,stateParam) {
           if (state.data.TranslatedName != null && state.data.Lang == c.Lang) return;
           var key = state.name;
           var navTranslate = stateParam.UrlTranslate;
    	   if (navTranslate) key = key + '?'+navTranslate;
    	   $translate(key).then(function (translated) {
    		   state.data.TranslatedName = translated;
    		   state.data.Lang = c.Lang;
    	   }, function () {
    		   var keys = state.name.split('/');
    		   var l = keys.length;
    		   $translate([keys[l - 1], keys[l - 2]]).then(function (translations) {
    			   state.data.TranslatedName = translations[keys[l - 2]] + translations[keys[l - 1]];
    		   });
    	   });
       };
       c.InitMenu = function (forbiddenFuncList,menuData) {
           c.forbiddenFuncList = forbiddenFuncList;
    	   c.menuData = menuData||App_MenuData;
    	   c.currentSubMenus = null;
    	   var fcodes = forbiddenFuncList ? forbiddenFuncList.toLowerCase() : '';
    	   angular.forEach(c.menuData, function (topmenuitem, index) {
    		   topmenuitem.status = fcodes.indexOf(topmenuitem.funcCode.toLowerCase()) >= 0 ? 'disabled' : '';
    		   angular.forEach(topmenuitem.subMenus, function (submenuitem, subindex) {
    			   var nav = submenuitem.nav || 'root';
    			   if (submenuitem.stateParam) {
     				   submenuitem.sref = submenuitem.stateName + '({' + submenuitem.stateParam + ',"ajax-nav":"' + nav + '"})';
    			   } else {
    				   submenuitem.sref = submenuitem.stateName + '({' + '"ajax-nav":"' + nav + '"})';
    			   }
    			   var state = $state.get(submenuitem.stateName);
    			   if (state) {
    				   state.data.SubMenu = submenuitem;
    				   state.data.Menu = topmenuitem;
    			   }
    			   submenuitem.status = fcodes.indexOf(submenuitem.funcCode.toLowerCase()) >= 0 ? 'disabled' : '';
    		   });
    	   });
       };
       c.SyncMenuToState = function (activeState) {
    	   angular.forEach(c.menuData, function (topmenuitem, index) {
    		   if (!topmenuitem.status && topmenuitem == activeState.data.Menu) {
    			   topmenuitem.status = 'active';
    			   c.currentSubMenus = topmenuitem.subMenus;
    		   }
    		   if (topmenuitem.status == 'active' && topmenuitem != activeState.data.Menu) topmenuitem.status = '';

    		   angular.forEach(topmenuitem.subMenus, function (submenuitem, subindex) {
    			   if (!submenuitem.status && submenuitem == activeState.data.SubMenu) submenuitem.status = 'active';
    			   if (submenuitem.status == 'active' && submenuitem != activeState.data.SubMenu) submenuitem.status = '';
    		   });
    	   });
       };
       c.test = function (any) {
    	   $window.alert('bingo');
       };
    } ]);
    //<<<<<<<------------Default implementation--------------------------------
    pbm.constant("App_Dict",{}); 
    pbm.factory('App_FuncTree', [function () {
        var forbiddenFuncList = null;
        var forbiddenElementVisible = false;
        var GetUncheckedListString = function (treeModel) {
            var s = [];
            angular.forEach(treeModel.funcList, function (val, idx) {
                if (treeModel.selectedList[idx] == false &&
                    (angular.isUndefined(treeModel.disableList[idx]) || treeModel.disableList[idx] == false)) {
                    s.push(val);
                }
            });
            s = s.join(',');
            return s;
        };
        var SetElementStatusByFunc = function (element, elementfunccode) {
         		if (forbiddenFuncList!=null&&forbiddenFuncList.indexOf(',' + elementfunccode.toLowerCase() + ',') < 0) return;
    			if(forbiddenElementVisible){
    				element.prop('disabled', true);
    			}else{
    				element.css('display','none');
    			}
        };
        var SetForbiddenFuncList = function (fcodes) {
            forbiddenFuncList = fcodes.toLowerCase();
        };
        var SetForbiddenElementVisible = function (visible) {
            forbiddenElementVisible = visible;
        };
        return { 
        	GetUncheckedListString: GetUncheckedListString,
        	SetElementStatusByFunc: SetElementStatusByFunc,
        	SetForbiddenFuncList: SetForbiddenFuncList,
        	SetForbiddenElementVisible: SetForbiddenElementVisible
        };
    }]);
    //----------------------------------------------->>>>>>>
//begin---------filter-----------------------------------------------------------------------------------
    pbm.filter("Dict", ['$filter', 'App_Dict', function ($filter, App_Dict) {
        return function (value, dictName) {
            var obj = App_Dict[dictName];
            for (var label in obj) {
                if (obj[label] == value) return label;
            }
            return value;
        };
    } ]);
    //将列表中加上一项：项数据为javascript对象{Id:1,RefText:'label'}}.只控件中加一项，原列表数据不受影响。
    pbm.filter("RefSelectEnforceMatch", ['$filter', 'PBPlugIn', function ($filter, PBPlugIn) {
        var newlist = null;
        return function (listvalue, itemobject) {
            if (newlist) return newlist;
            if (PBPlugIn.RefListContainItem(listvalue, itemobject)) {
                newlist = listvalue;
                return newlist;
            } 
            newlist = angular.copy(listvalue);
            newlist[listvalue.length] = itemobject;
            return newlist;
        };
    } ]);
    //将列表中加上一项或多项：项数据为javascript对象{labelasprop:valueasvalue}.只控件中加项，原列表数据不受影响。
    pbm.filter("SelectAddOptions", ['$filter', 'PBPlugIn', function ($filter, PBPlugIn) {
        var newlist = null;
        return function (listvalue, itemobject) {
            if (newlist) return newlist;
            newlist = angular.copy(listvalue);
            if (!angular.isObject(itemobject)) {
                var opnobj = PBPlugIn.GetMoreOptionsDefault();
                for (var prop in opnobj) {
                    if (itemobject == opnobj[prop]) {
                        itemobject = {};
                        itemobject[prop] = opnobj[prop];
                        break;
                    }
                }
            }
            for (var prop in itemobject) {
                newlist[prop] = itemobject[prop];
            }
            return newlist;
        };
    } ]);
    pbm.filter("Display", ['$filter', 'App_Dict', function ($filter, App_Dict) {//统一显示处理的接口
        return function (value, dictNameOrFormatOrFracSize, begin, end) {
            if (angular.isUndefined(value)) return '';
            if (angular.isNumber(value) && dictNameOrFormatOrFracSize) {
                if (angular.isString(dictNameOrFormatOrFracSize) && App_Dict[dictNameOrFormatOrFracSize]) {//second param is dictname
                    return $filter('Dict')(value, dictNameOrFormatOrFracSize);
                } else if (angular.isNumber(dictNameOrFormatOrFracSize)) {
                    return $filter('number')(value, dictNameOrFormatOrFracSize);
                }
            }
            if (value == true) return App_Dict.TrueDisplay;
            if (value == false) return App_Dict.FalseDisplay;
            if (angular.isString(value) && begin) return value.substring(begin, end);
            //now I assume it's a date value
            if (angular.isUndefined(dictNameOrFormatOrFracSize)) dictNameOrFormatOrFracSize = 'yyyy-MM-dd';
            return $filter('date')(value, dictNameOrFormatOrFracSize);
        };
    } ]);
    pbm.filter("ReIndex", ['$filter',  function ($filter) {
        return function (index, array) {
        	var cnt=index;
            for (var i=0;i<cnt;i++) {
                if (angular.isUndefined(array[i])) index=index-1;
            }
            return index;
        };
    } ]);
    //begin-------pb-required=label-------------------------------------------------------------------------------------
    pbm.directive("pbRequired", [function () {
        var directiveDefinitionObject = {
            priority: 10000,
            // transclude: false,
            restrict: 'A',
            compile: function (tElement, tAttrs, transclude) {
                if (tAttrs['pbRequired'] != 'label') return;
                tElement.append('<span>&nbsp;*&nbsp;</span>');
            }
        };
        return directiveDefinitionObject;
    } ]);
    //<----------------custom validator------------------------------------------------------------
    pbm.directive('pbValgroup', [function () {
        return {
            restrict: "A",
            require: "ngModel",
            link: function (scope, ele, attrs, ngModelController) {
                ngModelController.pb_valgroup=attrs["pbValgroup"];
            }
        };
    } ]);
    pbm.directive('pbEqualto', ['$parse', function ($parse) {
        return {
            restrict: "A",
            require: "ngModel",
            link: function (scope, ele, attrs, ngModelController) {
                ngModelController.$validators.equalto = function (modelValue, viewValue) {
                    var value = modelValue || viewValue;
                    return value == $parse(attrs["pbEqualto"])(scope);
                };
            }
        };
    } ]);
    pbm.directive('pbMaxByteLength', [function () {
        return {
            restrict: "A",
            require: "ngModel",
            link: function (scope, ele, attrs, ngModelController) {
                ngModelController.$validators.maxByteLength = function (modelValue, viewValue) {
                    var value = modelValue || viewValue;
                    return value == '' || value.getBytesLength() <= attrs["pbMaxByteLength"];
                };
            }
        };
    } ]);
    
    //------------------------------------
    //this function need the parameter stringvalue to be something in order of yearmonthday no matter what the delimeter is.
    function IsValidDate(stringvalue) {
        var year = stringvalue.substr(0, 4);
        var month = stringvalue.substr(5, 2);
        var day;
        if (isNaN(month.substr(1, 1))) {
            month = month.substr(0, 1);
            day = stringvalue.toString().substring(7, stringvalue.length);
        } else {
            day = stringvalue.toString().substring(8, stringvalue.length);
        }
        if (month.substr(0, 1) == '0') month = month.substr(1, 1);
        if (day.substr(0, 1) == '0') day = day.substr(1, 1);

        var tDateString = year + '/' + month + '/' + day;
        var tempDate = new Date(tDateString);
        if (isNaN(tempDate) == false) {
            if (((tempDate.getFullYear()).toString() == year) && (tempDate.getMonth() == parseInt(month) - 1) && (tempDate.getDate() == parseInt(day)))
                return true;
        }
        return false;
    }


    function GetParamFromUrl(url, name) {
        if (url.indexOf('?') < 0) return null;
        var qs = url.split('?')[1];
        var pos0 = qs.indexOf(name + '=');
        if (pos0 < 0) return null;
        var pos1 = qs.indexOf('&', pos0);
        if (pos1 < 0) return qs.substr(pos0 + name.length + 1);
        return qs.substring(pos0 + name.length + 1, pos1);
    }
    String.prototype.endWith = function (s) {
        if (s == null || s == "" || this.length == 0 || s.length > this.length)
            return false;
        if (this.substring(this.length - s.length) == s)
            return true;
        else
            return false;
    };

    String.prototype.startWith = function (s) {
        if (s == null || s == "" || this.length == 0 || s.length > this.length)
            return false;
        if (this.substr(0, s.length) == s)
            return true;
        else
            return false;
    };

    //begin和end都是yyyy-mm-dd格式的日期字符串
    function DateDiff(begin, end) {
        var beginArr = begin.split("-");
        var endArr = end.split("-");
        var beginRDate = new Date(beginArr[0], beginArr[1], beginArr[2]);
        var endRDate = new Date(endArr[0], endArr[1], endArr[2]);
        var result = (endRDate - beginRDate) / (24 * 60 * 60 * 1000);
        return result;
        //这样得到的result即为两个日期之间相差的天数。
    };
    String.prototype.getBytesLength = function () {
        return this.replace(/[^\x00-\xff]/gi, "--").length;
    };
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1,                 //月份 
            "d+": this.getDate(),                    //日 
            "h+": this.getHours(),                   //小时 
            "m+": this.getMinutes(),                 //分 
            "s+": this.getSeconds(),                 //秒 
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
            "S": this.getMilliseconds()             //毫秒 
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };
    //------------------------------------------------------------------------------->


} (String, angular));                         //end pack