def.ns = "/gn/Corp/";
def.tmp=def.MultiViewsState('MultiViewsSample.html');
def.tmp.Layout.Controller(['pb', '$scope','$rootScope',function (pb, $scope,$rootScope) {
	var c = pb.Super(this, {}, $scope);
	
	$rootScope.$on("/gn/Corp/Edit.deleteContact",function(event,args){
		c.tmp.MsgFromEdit=args;
	});
}]);
def.tmp.BranchView('Edit', "id").Controller(['pb', 'serverVm', '$scope','$rootScope','$translate',function (pb, serverVm, $scope,$rootScope,$translate) {
	var c = pb.Super(this, serverVm, $scope);
	
	c.btnDeleteRow_click= function (idx) {
		var deleted=c.vm.input.contactList[idx].name;
		delete c.vm.input.contactList[idx];
		$translate('ContactDeleted').then(function(ContactDeleted){
			$rootScope.$emit("/gn/Corp/Edit.deleteContact",ContactDeleted+deleted);
		})
	};
	c.btnAddRow_click= function () {
		if(!pb.ValidateUtil.Validate(c.frmEdit,'newrow'))return;
		c.vm.input.contactList[c.vm.input.contactList.length]=c.vm.input.newContact;
		c.vm.input.newContact={};
		pb.ValidateUtil.SetPristine(c.frmEdit,'newrow');
	};
}])
.BranchView('AnotherView.htm').Controller(['pb', '$scope','$rootScope',function (pb, $scope,$rootScope) {
	var c = pb.Super(this, {}, $scope);
	
	$rootScope.$on("/gn/Corp/Edit.deleteContact",function(event,args){
		c.tmp.MsgFromEdit=args;
	});
}]);
def.ContentState("TestAjaxNavBackToMultiViewsState.htm").NoController();


def.ns = "/gn/Dept/";
def.ContentState('Edit', "id").Controller(['pb', 'serverVm', '$scope','App_FuncTree',function (pb, serverVm, $scope,App_FuncTree) {
    var c = pb.Super(this, serverVm, $scope);

    c.ExecuteNoopResult = function (elementid) {
        pb.AjaxNavBack();
    };
    c.btnSave_click = function () {
    	var s=App_FuncTree.GetUncheckedListString(c.tmp_treeModel);
        pb.AjaxSubmit('btnSave',null,{"ajax-data":"input.deptFuncIds="+s});
    };
}]);
def.ContentState('Add').WithController('Edit');
def.ContentState('List')
.Controller(['pb', 'serverVm', '$scope','$window','$uibPosition','pbui',function (pb, serverVm, $scope,$window,$uibPosition,pbui) {
    var c = pb.Super(this, serverVm, $scope);
    
    c.vm.editInput={id:0};
    c.tmp.IsInLine=false;
    
    c.btnDeleteInLine_click= function (idx) {
    	var item=c.vm.resultList[idx];
        pbui.Confirm('ConfirmDelete',item.name).then(function(){
        	pb.CallAction('/do/gn/Dept/DeleteInLine?id='+item.id,null,function(result){
        		if(result.data==true){
        			delete c.vm.resultList[idx];
        		}
        	});
        });

    };
    c.btnAddInLine_click= function () {
    	c.moveToLine(-1);
    };
    c.btnEditInLine_click= function (item,index) {
    	c.moveToLine(index);
    	c.vm.editInput=item;
    	c.tmp.backupItem=angular.copy(item);
    };
    c.btnSaveInLine_click = function () {
    	pb.AjaxSubmit('c.frmEdit',null,null,function(result){
    		if(!result.IsData)return;
    		if(c.vm.editInput.id==0){
    			c.vm.editInput.id=result.data;
    			c.vm.resultList[c.vm.resultList.length]=c.vm.editInput;
    		}
    		c.resetEditLine();
    	});
    };
    c.btnCancel_click = function () {
    	angular.copy(c.tmp.backupItem,c.vm.editInput);
    	c.resetEditLine();
    };
    c.resetEditLine=function(){
		c.vm.editInput={id:0};
        c.frmEdit.$setPristine();
        c.moveToLine(null);
    };
    c.moveToLine=function(rowIndex){
    	var tbl=pb.ElementById('tbl');
    	var divEdit=pb.ElementById('divEdit');
    	var divNewRow=pb.ElementById('divNewRow');
    	c.tmp.IsInLine=true;
    	if(rowIndex==null){
        	c.tmp.IsInLine=false;
    		pb.ElementById('divStandBy').append(divEdit);
    	}
    	else if(rowIndex==-1)
    		divNewRow.after(divEdit);
    	else
    		tbl.children().eq(rowIndex+1).after(divEdit);
    };
}]).WithChild("SrcCode.htm").NoController();
def.ns = "/gn/User/";
def.ContentState('Edit', "id").Controller(['pb', 'serverVm', '$scope','App_FuncTree',function (pb, serverVm, $scope,App_FuncTree) {
    var c = pb.Super(this, serverVm, $scope);

    c.ExecuteNoopResult = function (elementid) {
        pb.AjaxNavBack();
    };
    c.btnSave_click = function () {
    	var s=App_FuncTree.GetUncheckedListString(c.tmp_treeModel);
        pb.AjaxSubmit('btnSave',null,{"ajax-data":"input.userFuncIds="+s});
    };
}]);
def.ContentState('Add', "deptId").WithController('Edit');
def.ContentState('Search').Controller(['pb', 'serverVm', '$scope',function (pb, serverVm, $scope) {
    var c = pb.Super(this, serverVm, $scope);

    c.dept_change = function () {
        pb.AjaxSubmit('frmSearch');
    };
}]).WithChild('List').NavBackRefresh(['pb',function(pb){
	pb.AjaxSubmit('frmSearch');
}]);
def.ContentState('PassEdit.htm').Controller(['pb',  '$scope', function (pb,  $scope) {
    var c = pb.Super(this, {}, $scope);

    c.btnOK_click = function () {
        if (c.frmPass.$invalid) {
            return;
        }
        pb.AjaxSubmit(null, null, { "ajax-url": '/do/gn/User/SavePassword', "ajax-data": 'OldPassword=' + hex_md5(c.vm.OldPassword) + '&NewPassword=' + hex_md5(c.vm.NewPassword) });
    };

}]);
def.ns="";
def.RootState('/home/Home/MainFrameLoggedIn.htm').NoController();
def.RootState('/home/Home/ShowLogin').Controller(['pb','serverVm','serverResult','$translate',
                                         function (pb,  serverVm,  serverResult,  $translate) {
	var c=this;
	pb.ExecuteResult(serverResult,c);
	
	c.password_keydown=function(event){
	    if (event.which == 13) {
	    	c.btnLogin_click();
	        return false;
	    }
	    return true;
	};
	
	c.btnLogin_click=function(){
	   	if (c.frmLogin.$invalid) {
	   		return;
	   	}
		pb.CallAction('/do/home/Home/Login','corpCode=' + c.vm.corpCode+'&code=' + c.vm.code + '&password=' + hex_md5(c.vm.password));
	};

	c.setLang=function(lang){
		$translate.use(lang);
	};

}]).ForceViewByAction();

app.controller("LeftViewCtrl", ['pb','$scope',function (pb,$scope) {
	var c=this;
    $scope.$on('$stateChangeSuccess', 
    	    function(event, toState, toParams, fromState, fromParams) {
    	        	c.showView=(toState.name!="/home/Home/ShowLogin");
    	        	$scope.$parent.baseCtrl.showLeftView=c.showView;
    	    });	
}]);
app.controller("TopViewCtrl", ['pb','$scope','$state',function (pb, $scope,$state) {
    var c = this;
    c.showView = true;
    c.navbarCollapsed=true;
    c.vm={input:{code:"test",password:"1"}};//不是state又没使用init-vm,则需要编程初始化vm
    $scope.$on('$stateChangeSuccess',
	    	    function (event, toState, toParams, fromState, fromParams) {
	    	        c.showView = (toState.name != "/home/Home/ShowLogin");
	    	        if (c.showView && ($scope.baseCtrl.vm==undefined ||$scope.baseCtrl.vm.loginCorpName == undefined)) {
	    	            pb.CallAction('/do/home/Home/MainFrame',null, function (result) {
	    	                $scope.baseCtrl.vm = result.data.ViewModel;
	    	                if(result.data.ViewModel==null) {
	    	                	$state.go('/home/Home/ShowLogin');
	    	                	return;
	    	                }
	    	                $scope.baseCtrl.InitMenu(result.data.ViewModel.forbiddenMenuFuncList);
	    	            });
	    	        }
	    	    });

    c.Html_Load = function () {
        ////app.GlobalDict.LoadSelector('app.GlobalDict.PartyClass');
    };
	c.btnLogin_click=function(){
	   	if (c.frmTopLogin.$invalid) {
	   		return;
	   	}
		pb.CallAction('/do/home/Home/Login', 'corpCode=1&code=' + c.vm.input.code + '&password=' + hex_md5(c.vm.input.password));
	};
	c.btnLogout_click=function(){
		$scope.baseCtrl.vm={};
		pb.CallAction('/do/home/Home/Logout');
	};
 
}]);

def.ns = "/ta/Task/";
def.ContentState('Edit', "id").Controller(['pb', 'serverVm', '$scope',function (pb, serverVm, $scope) {
    var c = pb.Super(this, serverVm, $scope);

    c.ExecuteNoopResult = function (elementid) {
        pb.AjaxNavBack();
    };

}]);
def.ContentState('Add').WithController('Edit');
def.ContentState('Search').WithChild('List').WithChild('SrcCode.htm').NoResolve();
def.ns = "/ta/TaskItem/";
def.ContentState('Edit', "id").Controller(['pb', 'serverVm', '$scope',function (pb, serverVm, $scope) {
    var c = pb.Super(this, serverVm, $scope);

    c.ExecuteNoopResult = function (elementid) {
        pb.AjaxNavBack();
    };

}]);
def.ContentState('Add').WithController('Edit');
def.ContentState('Search').WithChild('List').WithChild('SrcCode.htm').NoResolve();
﻿(function (String, angular) {
    'use strict';

    var pbm = angular.module('projectbase');

    pbm.directive("ajaxNavbar", ['pb', '$state', '$stateParams',function factory(pb, $state, $stateParams) {
        var directiveDefinitionObject = {
            priority: 0,
            templateUrl: "/Shared/Directive/AjaxNavbar.htm",
            // transclude: false,
            restrict: 'EA',
            scope: true,
            controllerAs: 'd',
            controller: ['$scope', '$element', '$attrs', '$transclude',function ($scope, $element, $attrs, $transclude) {
                var d = this;
                d.AjaxNavStack=pb.AjaxNavData.Stack;
                d.AjaxNavRefresh = function () {
                	pb.AjaxNavRefresh();
                };
                d.AjaxNavBack = function () {
                    pb.AjaxNavBack();
                };
                d.AjaxNavTo = function (state,stateParams) {
                	pb.AjaxNavTo(state,stateParams);
                };
            }]
        };
        return directiveDefinitionObject;
    }]);



} (String, angular));                                                          //end pack
﻿(function (String, angular) {
    'use strict';

    var pbm = angular.module('projectbase');

    pbm.directive("pbFunctree", ['pb', '$state', '$stateParams',function factory(pb, $state, $stateParams) {
        var directiveDefinitionObject = {
            priority: 0,
            templateUrl: "/do/shared/Common/FuncList?action",
            // transclude: false,
            restrict: 'EA',
            scope: {
                inputModel: "=inputModel",
                treeModel: "=treeModel",
                disabled: "=disabled"
            },
            controllerAs: 'd',
            controller: ['$scope', '$element', '$attrs', '$transclude',function ($scope, $element, $attrs, $transclude) {
                var d = this;
                d.chk_change = function (level) {
                    var foundIdx = 0;
                    for (var i = 0; i < $scope.d.levelList.length; i++) {
                        if ($scope.d.levelList[i] == level) {
                            foundIdx = i;
                            break;
                        }
                    }
                    for (var j = foundIdx + 1; j < $scope.d.levelList.length; j++) {
                        if ($scope.d.levelList[j].length <= level.length) {
                            break;
                        }
                        $scope.d.selectedList[j] = $scope.d.selectedList[foundIdx];
                    }
                };
            }],
            link: function (scope, iElement, iAttrs, controller) {
                var SetUnCheck = function (funcIds, canChange) {
                    var funcIdArray = funcIds.split(',');
                    angular.forEach(funcIdArray, function (funcId) {
                        angular.forEach(scope.d.funcList, function (selVal, index) {
                            if (selVal == funcId) {
                                scope.d.selectedList[index] = false;
                                scope.d.disableList[index] = !canChange;
                            }
                        });
                    });
                };
                scope.d.funcList = scope.d._initData.funcList;
                scope.d.levelList = scope.d._initData.levelList;
                scope.d.selectedList = [];
                for (var i = 0; i < scope.d.funcList.length; i++) {
                    scope.d.selectedList[i] = true;
                }
                scope.d.disableList = [];
                scope.treeModel = { funcList: scope.d.funcList, selectedList: scope.d.selectedList, disableList: scope.d.disableList };
                var canChangeDept=angular.isUndefined(scope.inputModel.userFuncIds);
            	var canChangeCorp=angular.isUndefined(scope.inputModel.deptFuncIds);
            	SetUnCheck(scope.inputModel.corpFuncIds,canChangeCorp);
            	if(scope.inputModel.deptFuncIds)SetUnCheck(scope.inputModel.deptFuncIds,canChangeDept);
            	if(scope.inputModel.userFuncIds)SetUnCheck(scope.inputModel.userFuncIds,true);
            	
            }
        };
        return directiveDefinitionObject;
    }]);



} (String, angular)); //end pack
﻿(function (String, angular) {
    'use strict';

    var pbm = angular.module('projectbase');

    pbm.directive("pagerInput", ['pb', '$timeout',function factory(pb, $timeout) {
        var directiveDefinitionObject = {
            priority: 0,
            templateUrl: "/Shared/Directive/Pager.htm",
            // transclude: false,
            restrict: 'A',
            scope: {
                m: "=pagerModel"
            },
            controllerAs: 'd',
            controller: ['$scope', '$element', '$attrs', '$transclude',function ($scope, $element, $attrs, $transclude) {
                var d = this;
                var m = $scope.m;
                d.inputName_PageNum = $attrs["pagerInput"] + ".pageNum";
                d.inputName_PageSize = $attrs["pagerInput"] + ".pageSize";
                d.submitData = function () {
                    return { "ajax-data":
                        d.inputName_PageNum + "=" + $scope.m.pageNum + "&" + d.inputName_PageSize + "=" + $scope.m.pageSize
                    };
                };
                d.pagerShort = $attrs["pagerShort"];
                d.pagesizeItems = [5, 15, 100];
                if ($attrs["pagerSizemax"] != null) d.pagesizeItems[3] = $attrs["pagerSizemax"];
                if (m.pageNum == null || m.pageNum < 1) m.pageNum = 1;
                //adjust the pageIndex to the actual number of the last page
                if (m.pageNum > m.pageCount) {
                    m.pageNum = m.pageCount;
                }
                if (m.pageCount >= 0 && m.pageNum <= 0) {
                    m.pageNum = 1;
                }
                d.goPageNum = m.pageNum;
                d.firstpage_click = function () {
                    if ($scope.m.pageNum != 1) {
                        d.goPageNum = $scope.m.pageNum = 1;
                        pb.AjaxSubmit($element, null, d.submitData());
                    }
                    return false;
                };
                d.prevpage_click = function () {
                    if ($scope.m.pageNum > 1) {
                        d.goPageNum = $scope.m.pageNum -= 1;
                        pb.AjaxSubmit($element, null, d.submitData());
                    }
                    return false;
                };
                d.nextpage_click = function () {
                    if ($scope.m.pageNum < $scope.m.pageCount) {
                        d.goPageNum = $scope.m.pageNum += 1;
                        pb.AjaxSubmit($element, null, d.submitData());
                    }
                    return false;
                };
                d.lastpage_click = function () {
                    if ($scope.m.pageNum != $scope.m.pageCount) {
                        d.goPageNum = $scope.m.pageNum = $scope.m.pageCount;
                        pb.AjaxSubmit($element, null, d.submitData());
                    }
                    return false;
                };
                d.go_click = function () {
                    if (d.goPageNum == null) return;
                    $scope.m.pageNum = d.goPageNum;
                    pb.AjaxSubmit($element, null, d.submitData(), function (response) {//直接手动绑定，对比下面依赖自动绑定的做法，两种方法二选一
                        d.goPageNum = response.data.ViewModel.input.pager.pageNum;
                    });
                };
                d.pagesize_change = function () {
                    pb.AjaxSubmit($element, null, d.submitData(), function () {
                        $timeout(function () {//此处依赖angular自动绑定response中数据到$scope（自动绑定将在当前代码控制序列结束后才进行）,因此需延迟使用
                            d.goPageNum = $scope.m.pageNum;
                        });
                    });
                };
            }]
        };
        return directiveDefinitionObject;
    }]);



} (String, angular));                                          //end pack
