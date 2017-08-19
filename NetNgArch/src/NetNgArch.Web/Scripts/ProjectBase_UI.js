(function (String, angular) {
    'use strict';

    var pbm = angular.module('projectbase');
    //begin-------datepicker-------------------------------------------------------------------------------------
    pbm.directive("pbDatepickerButton", [function () {
        var pickerbuttonNum = 0;
        var directiveDefinitionObject = {
            priority: 0,
            // transclude: false,
            restrict: 'A',
            scope: false, //因为跟uib-datepicker用在同一个元素上，而前者已经用了isolatedscope，所以这个只能用falsescope了，不同实例的数据通过在d中存多个属性数据来实现
            controllerAs: '_pbDatepickerButtonCtrl', //specific name
            controller: ['$scope', '$element', '$attrs', '$transclude',function ($scope, $element, $attrs, $transclude) {
                var d = this;
                d.open = function (pickerbuttonNum) {
                    d['pbtn' + pickerbuttonNum] = { IsOpen: true };
                };
            }],
            compile: function (tElement, tAttrs, transclude) {
                if (tAttrs['pbDatepickerButton'] == 'no' || tElement.parent().hasClass('input-group')) return;
                pickerbuttonNum += 1;
                var div = tElement.wrap("<div class='input-group'></div>").parent();
                var btnHtml = '<span class="input-group-btn">'
            			+ '<a class="btn btn-default" ng-click="_pbDatepickerButtonCtrl.open(' + pickerbuttonNum + ')"><i class="glyphicon glyphicon-calendar"></i></a>';
                div.append(btnHtml);
                tElement.attr('is-open', '_pbDatepickerButtonCtrl.pbtn' + pickerbuttonNum + '.IsOpen');
            }
        };
        return directiveDefinitionObject;
    } ]);
    //begin-------checkall-------------------------------------------------------------------------------------
    pbm.directive("pbCheckall", [function () {
        var directiveDefinitionObject = {
            priority: 0,
            template: '<input type="checkbox" ng-model="d.checked" ng-change="d.chkCheckAll_click()"/>',
            // transclude: false,
            restrict: 'E',
            scope: {
                listModel: "=listModel",
                selectedModel: "=selectedModel"
            },
            controllerAs: 'd',
            controller: ['$scope', '$element', '$attrs', '$transclude',function ($scope, $element, $attrs, $transclude) {
                var d = this;
                var listProp = $attrs["listProp"];
                d.allSelectedList = [];
                angular.forEach($scope.listModel, function (value, index) {
                    d.allSelectedList[index] = value[listProp];
                });
                d.chkCheckAll_click = function () {
                    $scope.selectedModel = d.checked ? angular.copy(d.allSelectedList) : [];
                };
            }]
        };
        return directiveDefinitionObject;
    } ]);
    //begin------sort--------------------------------------------------------------------------------------
    pbm.directive("pbSortInput", ['pb', function (pb) {
        var directiveDefinitionObject = {
            priority: 0,
            restrict: 'A',
            scope: {
                sortModel: '=pbSortModel'
            },
            controllerAs: 'd',
            controller: ['$scope', '$element', '$attrs', '$transclude',function ($scope, $element, $attrs, $transclude) {
                var d = this;
                d.inputName_Sort = $attrs["pbSortInput"];
                d.sortExpr = $scope.sortModel;
                d.submit = function () {
                    $scope.sortModel = d.sortExpr;
                    pb.AjaxSubmit($element, null, { "ajax-data": d.inputName_Sort + "=" + $scope.sortModel });
                };
            }],
            compile: function compile(tElement, tAttrs, transclude) {
                tElement.prepend('<input value="{{' + tAttrs['pbSortModel'] + '}}" type="hidden" name="' + tAttrs['pbSortInput'] + '" />');
            }
        };
        return directiveDefinitionObject;
    } ]);
    pbm.directive("pbSortExpr", [function () {
        var directiveDefinitionObject = {
            priority: 0,
            require: '^pbSortInput',
            restrict: 'A',
            scope: false,
            compile: function compile(tElement, tAttrs, transclude) {
                tElement.addClass("bg-warning");
                var sort = tAttrs['pbSortExpr'];
                tElement.append("<span ng-class='pb_SortExpr.indexOf(\"desc\")>0?\"glyphicon glyphicon-triangle-bottom\":\"glyphicon glyphicon-triangle-top\"' ng-if='pb_SortExpr.indexOf(\""+sort+"\")>=0'></span>");
                var colexpr;
                if (tAttrs['pbSortAltExpr'] == null) {
                    if (sort.indexOf(',') < 0) {
                        if (sort.toLowerCase().indexOf(' asc') >= 0) {
                            colexpr = sort.split(' ')[0];
                            tElement.attr('pb-sort-expr', colexpr + ' asc');
                            tElement.attr('pb-sort-altExpr', colexpr + ' desc');
                        } else if (sort.toLowerCase().indexOf(' desc') >= 0) {
                            colexpr = sort.split(' ')[0];
                            tElement.attr('pb-sort-expr', colexpr + ' desc');
                            tElement.attr('pb-sort-altExpr', colexpr + ' asc');
                        } else {
                            tElement.attr('pb-sort-expr', sort + ' asc');
                            tElement.attr('pb-sort-altExpr', sort + ' desc');
                        }
                    } else {
                        tElement.attr('pb-sort-altExpr', sort);
                    }
                }
                return function postLink(scope, iElement, iAttrs, sortInputCtrl) {
                	scope.pb_SortExpr=sortInputCtrl.sortExpr;
                    iElement.bind('click', function () {
                        var lastsort = sortInputCtrl.sortExpr;
                        var newsort = iElement.attr('pb-sort-expr'); //note that I use iElement.attr() instead of iAttrs which doesn't reflect compile-phase changes 
                        var togglesort = iElement.attr('pb-sort-altExpr');
                        sortInputCtrl.sortExpr = lastsort != newsort ? newsort : togglesort;
                        sortInputCtrl.submit();
                        scope.pb_SortExpr=sortInputCtrl.sortExpr;
                        return false;
                    });
                };
            }
        };
        return directiveDefinitionObject;
    }]);
    //begin-------processing-------------------------------------------------------------------------------------
    pbm.directive("pbProcessingMask", [function () {
        var directiveDefinitionObject = {
            priority: 0,
            restrict: 'E',
            compile: function compile(tElement, tAttrs, transclude) {
            	var mask=angular.element('<div ng-style="$root.pbvar.ProcessingRect" ng-show="$root.pbvar.ShowProcessing"></div>');
            	if(tAttrs['class']==null)mask.addClass("projectbase-processing");
            	mask.append(tElement.children());
            	tElement.append(mask);
                return function postLink(scope, iElement, iAttrs) {
                	scope.$root.pbvar.DefaultViewMaskId=iAttrs['targetid'];
                };
            }
        };
        return directiveDefinitionObject;
    } ]);
    
    //<------------------------pbui service--------------------------------------------------
    pbm.provider('pbui',function(){
    	var me=this;
    	var TplPath='../Scripts/tpl';
    	me.SetTplPath=function(path){
    		TplPath=path;
    	}
        var pbuiFn=['$uibModal','$q','$translate','$uibPosition','$rootScope','$window','$log',
                    function($uibModal,$q,$translate,$uibPosition,$rootScope,$window,$log){
        	var AlertModal=function(msg,size,animationsEnabled){
        		animationsEnabled=animationsEnabled||true;
        		var modalInstance = $uibModal.open({
    			       animation: animationsEnabled,
    			       size: size,
    			       resolve: {
    			         msg: function(){return msg;}
    			       },
    			       templateUrl: TplPath+'/pbui.Alert.htm',
    			       controller: ['$scope', '$uibModalInstance','msg', function($scope, $uibModalInstance,msg){
    			    	   		$scope.msg=msg;
    			    	   		$scope.ok = function () {
    			    	   			$uibModalInstance.close(true);
    			    	   		};
    			       }]
    		     });
        		 return modalInstance.result;
        	};
        	var ConfirmModal=function(msg,msgParams,size,animationsEnabled){
        		animationsEnabled=animationsEnabled||true;
        		var modalInstance = $uibModal.open({
    			       animation: animationsEnabled,
    			       size: size,
    			       resolve: {
    			         msg: function(){return msg;},
    			         msgParams: function () { return msgParams; }
    			       },
    			       templateUrl: TplPath+'/pbui.Confirm.htm',
    			       controller: ['$scope', '$uibModalInstance', 'msg', 'msgParams', '$translate',
    			                    function ($scope, $uibModalInstance, msg, msgParams, $translate) {
    				   		$translate(msg).then(function (msg) {
    				   		    $scope.msg = msgParams ? msg.replace('{0}', msgParams) : msg;
    						},function (msg) {
    						    $scope.msg = msgParams ? msg.replace('{0}', msgParams) : msg;
    						});
    		    	   		$scope.msg=msg;
    		    	   		$scope.ok = function () {
    		    	   			$uibModalInstance.close(true);
    		    	   		};
    	
    		    	   		$scope.cancel = function () {
    		    	   			$uibModalInstance.dismiss(false);
    		    	   		};
    			       }]
    		     });
        		 return modalInstance.result;
        	};
        	var ShowCommandModal=function(command,msg,size,animationsEnabled){
        		animationsEnabled=animationsEnabled||true;
        		var modalInstance = $uibModal.open({
    			       animation: animationsEnabled,
    			       size: size,
    			       resolve: {
    			         msg: function(){return msg;}
    			       },
    			       templateUrl: TplPath+'/pbui.'+command+'.htm',
    			       controller: ['$scope', '$uibModalInstance','msg', function($scope, $uibModalInstance,msg){
    			    	   		$scope.msg=msg;
    			    	   		$scope.ok = function () {
    			    	   			$uibModalInstance.close(true);
    			    	   		};
    			       }]
    		     });
        		 return modalInstance.result;
        	};
            var DialogModal = function (templateUrl, paramObj, controller,size, animationsEnabled) {
               animationsEnabled = animationsEnabled || true;
               var modalInstance = $uibModal.open({
                   animation: animationsEnabled,
                   size: size,
                   resolve: {
                       paramObj: function () { return paramObj; }
                   },
                   templateUrl: templateUrl,
                   controllerAs:'dc',
                   controller:controller
               });
               return modalInstance.result;
           };
        	var PutProcessing=function(elementId){
        		if(!elementId){
        			$rootScope.pbvar.ShowProcessing=false;
        			return;
        		}
        		var element;
        		if(elementId=='_view')elementId=$rootScope.pbvar.DefaultViewMaskId;
        		element=$window.document.querySelector('#'+elementId);
        		if(!element){
        			$rootScope.pbvar.ShowProcessing=false;
        			$log.error('no element for '+elementId);
        			return;
        		}
        		var pos=$uibPosition.position(element);
        	    $rootScope.pbvar.ProcessingRect.width=pos.width+'px';
        	    $rootScope.pbvar.ProcessingRect.height=pos.height>30?pos.height+'px':50+'px';
        	    var top=0, left=0;
        	    while(element.tagName!='HTML'){
        	        pos=$uibPosition.position(element);
        	        left += pos.left;
        	        top += pos.top;
        	        element=$uibPosition.offsetParent(element);
        	    }
        	    $rootScope.pbvar.ProcessingRect.top=top+'px';
        	    $rootScope.pbvar.ProcessingRect.left=left+'px';
        	    $rootScope.pbvar.ShowProcessing=true;
        	};
    		return {
    			 Alert:AlertModal,
    			 Confirm:ConfirmModal,
    			 ShowCommand:ShowCommandModal,
    			 Dialog: DialogModal,
    			 PutProcessing:PutProcessing
    		};
    	}];
        
    	me.$get = pbuiFn;
    });


} (String, angular));                         //end pack