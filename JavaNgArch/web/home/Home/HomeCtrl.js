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

app.controller("LeftViewCtrl", ['pb','$scope','$transitions',function (pb,$scope,$transitions) {
	var c=this;
	$transitions.onSuccess({},function(trans) {
    	        	c.showView=(trans.to().name!="/home/Home/ShowLogin");
    	        	$scope.$parent.baseCtrl.showLeftView=c.showView;
    	    });	
}]);
app.controller("TopViewCtrl", ['pb','$scope','$state','$transitions','App_MenuData','App_FuncTree',function (pb, $scope,$state,$transitions,App_MenuData,App_FuncTree) {
    var c = this;
    c.showView = true;
    c.navbarCollapsed=true;
    c.vm={input:{code:"test",password:"1"}};//不是state又没使用init-vm,则需要编程初始化vm
    $transitions.onStart({},function(trans) {
	    	        c.showView = (trans.to().name != "/home/Home/ShowLogin");
	    	        if (c.showView && ($scope.baseCtrl.vm==undefined ||$scope.baseCtrl.vm.loginCorpName == undefined)) {
	    	            return pb.CallAction('/do/home/Home/MainFrame',null, function (result) {
	    	                $scope.baseCtrl.vm = result.data.ViewModel;
	    	                if(result.data.ViewModel==null) {
	    	                	return trans.router.stateService.target('/home/Home/ShowLogin');
	    	                }
	    	                $scope.baseCtrl.InitMenu(result.data.ViewModel.forbiddenMenuFuncList,App_MenuData);
	    	                App_FuncTree.SetForbiddenFuncList(result.data.ViewModel.forbiddenFuncList);
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

