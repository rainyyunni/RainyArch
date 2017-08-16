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

