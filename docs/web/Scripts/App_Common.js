//<-----------------App setup-----------------------
//除app和def外，不要再定义其他的全局变量，均应定义命名对象
var app = angular.module("app", ['projectbase']);//应用对象，全局变量
//--------------Config StatesDefiner and Define Common States -------------------------------->
var def = new PB_Global_StateDefiner(app);//用于定义State的全局变量
def.SetDefaultLayout('/Shared/ContentLayout');
def.SetAjaxNavRootState('/home/Home/Introduction');
def.SetUrlContextPrefix('/docs');
def.SetUrlMappingPrefix('');
def.LayoutState('/Shared/ContentLayout.htm');
def.LayoutState('/Shared/ErrorLayout.htm');
//--------------------------------------------------->
//<-----------保存应用级别的全部全局变量与配置--------------------------
app.constant('App_DefaultState','/home/Home/Introduction');
app.constant('App_UniqueCheckerUri','/do/shared/Common/CheckUnique?name=');
app.constant('APP_ContextPrefix','/docs');
//----------------------------------------------------------------------->
//<----------------------config components
app.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', '$translateProvider', 'pbProvider','pbuiProvider','App_DefaultState','APP_ContextPrefix',
            function ($stateProvider, $urlRouterProvider, $httpProvider, $translateProvider, pbProvider,pbuiProvider,App_DefaultState,APP_ContextPrefix) {
    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
    $httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
   // $httpProvider.interceptors.push('ResponseCheckerForUiRouter');

    $translateProvider.useSanitizeValueStrategy('escape');
    $translateProvider.useLocalStorage();
    var lang = window.localStorage.lang || 'zh-cn';
    $translateProvider.preferredLanguage(lang);
    $translateProvider.useStaticFilesLoader({
        prefix: APP_ContextPrefix+'/Scripts/lang/',
        suffix: '.json'
    });

    $urlRouterProvider.otherwise(App_DefaultState);
    def.RegisterStates($stateProvider);
    pbuiProvider.SetTplPath(APP_ContextPrefix+'/Scripts/tpl');
}]);
app.run(['validator','myCustomErrorMessageResolver', 'myCustomElementModifier','defaultErrorMessageResolver','App_FuncTree','APP_ContextPrefix',
function(validator,myCustomErrorMessageResolver, myCustomElementModifier,defaultErrorMessageResolver,App_FuncTree,APP_ContextPrefix) {
    validator.registerDomModifier(myCustomElementModifier.key, myCustomElementModifier);
    validator.setDefaultElementModifier(myCustomElementModifier.key);
    validator.defaultFormValidationOptions.forceValidation=true;
    defaultErrorMessageResolver.setI18nFileRootPath(APP_ContextPrefix+"/Scripts/lang");
    defaultErrorMessageResolver.setCulture("zh-cn");
    validator.setErrorMessageResolver(myCustomErrorMessageResolver.resolve);
    App_FuncTree.SetForbiddenElementVisible(true);
}]);
//---------------------------------------------------------------------------------------------->
//<----------------------customize and extend framework components
app.factory('myCustomErrorMessageResolver', ['$q','defaultErrorMessageResolver','$translate','$window','$log', 
                                             function($q,defaultErrorMessageResolver,$translate,$window,$log) {
    /**
             * @ngdoc function
             * @name defaultErrorMessageResolver#resolve
             * @methodOf defaultErrorMessageResolver
             *
             * @description
             * Resolves a validate error type into a user validation error message
             *
             * @param {String} errorType - The type of validation error that has occurred.
             * @param {Element} el - The input element that is the source of the validation error.
             * @returns {Promise} A promise that is resolved when the validation message has been produced.
             */
    var resolve = function(errorType, el) {
        var p = defaultErrorMessageResolver.resolve(errorType, el);
        var msg;
        if(el.attr('pb-valmsg')){
	        try{
	        	var valmsg=angular.fromJson(el.attr('pb-valmsg'));
	        	msg=valmsg[errorType];
	        }catch(e){
	        	$log.error("pb-valmsg's value is invalid, should be a json string:"+el.attr('pb-valmsg'));
	        }
        }
        var translatekey=el.attr('translatekey');

        return 	p.then(function(automsg){
	        	var errorMsg=msg||automsg;
	        	$translate([translatekey,errorMsg]).then(function (translated) {
			    $window.alert(translated[translatekey]+" "+translated[errorMsg]);
			},function (notTranslatedMsgKey) {
			    $window.alert(notTranslatedMsgKey[translatekey]+" "+notTranslatedMsgKey[errorMsg]);
			});
		});
    }
    ;
    
    return {
        resolve: resolve
    };
}]);
app.factory('myCustomElementModifier', [function() {
    var /**
             * @ngdoc function
             * @name myCustomElementModifier#makeValid
             * @methodOf myCustomElementModifier
             *
             * @description
             * Makes an element appear valid by apply custom styles and child elements.
             *
             * @param {Element} el - The input control element that is the target of the validation.
             */
    makeValid = function(el) {
        el.css('border-color', 'green');
    }, 
    
    /**
             * @ngdoc function
             * @name myCustomElementModifier#makeInvalid
             * @methodOf myCustomElementModifier
             *
             * @description
             * Makes an element appear invalid by apply custom styles and child elements.
             *
             * @param {Element} el - The input control element that is the target of the validation.
             * @param {String} errorMsg - The validation error message to display to the user.
             */
    makeInvalid = function(el, errorMsg) {
        el.css('border-color', 'red');
    },
    makeDefault = function (el) {
          el.css('border-color', 'blue');
    };

      return {
          makeValid: makeValid,
          makeInvalid: makeInvalid,
          makeDefault: makeDefault,
          key: 'myCustomModifierKey'
      };
}]);
app.factory('PBPlugIn', ['PBPlugIn_Default','$window',function (PBPlugIn_Default,$window) {
	var ext={};
	angular.extend(ext,PBPlugIn_Default);
	ext.ShowResultMessage = function (msgKey) {
    	$window.alert('coding here for your own global method "ShowMessage" with msgKey='+msgKey);
    };
    ext.RefListContainItem = function (list, item) {
        for (var i = 0; i < list.length; i++) {
            if (list[i].id == item.id) {
                return true;
            }
        }
        return false;
    };
    ext.GetMoreOptionsDefault = function () {
        return {"全部":-1};
    };
    return ext;
} ]);
//----------------------------------------------->
//<---------------------------------------全局功能
app.factory('App_FuncTree', [function () {
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
app.directive('appUnique', ['pb', 'App_UniqueCheckerUri', '$q', function (pb, App_UniqueCheckerUri, $q) {
    return {
        restrict: "A",
        require: "ngModel",
        link: function (scope, ele, attrs, ngModelController) {
            ngModelController.$asyncValidators.unique = function(modelValue, viewValue) {
                var value = modelValue || viewValue;
                return pb.AjaxSubmit(null, { value: value }, { "ajax-url": App_UniqueCheckerUri + attrs["appUnique"] })
                    .then(function(response) {
                        return response.data ? true : $q.reject();
                    }, function() {
                        return $q.reject();
                    });
            };
        }
    };
} ]);
//-------------------------------------------------->>>>>>>>>>