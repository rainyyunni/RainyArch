//<-----------------App setup-----------------------
//<--------------框架上的应用定义的全局变量，方便于脚本中引用各组件,仅此两个，不要再定义其他的全局变量，需要时应该定义命名对象
var app = angular.module("app", ['projectbase']);
//--------------Config StatesDefiner and Define Common States -------------------------------->
var def = new PB_Global_StateDefiner(app);
def.SetDefaultLayout('/Shared/ContentLayout');
def.SetAjaxNavRootState('/home/Home/MainFrameLoggedIn');
def.SetUrlMappingPrefix('/do');
def.LayoutState('/Shared/ContentLayout.htm');
def.LayoutState('/Shared/ErrorLayout.htm');
//--------------------------------------------------->
//<-----------保存应用级别的全部全局变量与配置--------------------------
app.constant('App_DefaultState','/home/Home/ShowLogin');
app.constant('App_UniqueCheckerUri','/do/shared/Common/CheckUnique?name=');
		
//menu text should be unique
app.constant('App_MenuData', [
               { text: 'Sample', funcCode: 'M_Sample', stateRef: '', subMenus: [
                                                            { text: 'Task', funcCode: 'M_Task', stateName: '/ta/Task/SrcCode'},
                                                            { text: 'TaskItem', funcCode: 'M_TaskItem', stateName: '/ta/TaskItem/SrcCode' }]               
               },
               { text: 'System', funcCode: 'M_System', subMenus: [
                                                            { text: 'Corp', funcCode: 'M_Corp', stateName: '/gn/Corp/MultiViewsSample' },
                                                            { text: 'Dept', funcCode: 'M_Dept', stateName: '/gn/Dept/SrcCode' },
                                                            { text: 'User', funcCode: 'M_User', stateName: '/gn/User/List'}]
               },
               { text: 'Personal', funcCode: 'M_Personal', subMenus: [
                                                                { text: 'ChangePass', funcCode: 'M_Pass', stateName: '/gn/User/PassEdit'}]
               }
]);
//----------------------config components-------------------------------------------------------------------------------
app.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', '$translateProvider', 'pbProvider','App_DefaultState',
            function ($stateProvider, $urlRouterProvider, $httpProvider, $translateProvider, pbProvider,App_DefaultState) {
    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
    $httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
   // $httpProvider.interceptors.push('ResponseCheckerForUiRouter');

    $translateProvider.useSanitizeValueStrategy('escape');
    $translateProvider.useLocalStorage();
    var lang = window.localStorage.lang || 'zh-cn';
    $translateProvider.preferredLanguage(lang);
    $translateProvider.useStaticFilesLoader({
        prefix: '/Scripts/lang/',
        suffix: '.json'
    });

    $urlRouterProvider.otherwise(App_DefaultState);
    def.RegisterStates($stateProvider);
}]);
app.run(['validator','myCustomErrorMessageResolver', 'myCustomElementModifier','defaultErrorMessageResolver','$rootScope','$state','$stateParams',
function(validator,myCustomErrorMessageResolver, myCustomElementModifier,defaultErrorMessageResolver,$rootScope,$state,$stateParams) {
    validator.registerDomModifier(myCustomElementModifier.key, myCustomElementModifier);
    validator.setDefaultElementModifier(myCustomElementModifier.key);
    validator.defaultFormValidationOptions.forceValidation=true;
    defaultErrorMessageResolver.setI18nFileRootPath("/Scripts/lang");
    defaultErrorMessageResolver.setCulture("zh-cn");
    validator.setErrorMessageResolver(myCustomErrorMessageResolver.resolve);
    $rootScope.$state=$state;
    $rootScope.$stateParams=$stateParams;
}]);
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
        if(el.attr('valmsg')){
	        try{
	        	var valmsg=angular.fromJson(el.attr('valmsg'));
	        	msg=valmsg[errorType];
	        }catch(e){
	        	$log.error("valmsg's value is invalid, should be a json string:"+el.attr('valmsg'));
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
    }
      , 
    
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
    }
    ;
    
    return {
        makeValid: makeValid,
        makeInvalid: makeInvalid,
        key: 'myCustomModifierKey'
    };
}]);
app.factory('App_FuncTree', function () {
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
    return { GetUncheckedListString: GetUncheckedListString };
});