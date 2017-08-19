def.ns = "";
def.RootState('/Home/MainFrameLoggedIn.htm').NoResolve().NoController();
def.RootState('/Home/ShowLogin').Controller(['pb','serverVm','serverResult','$translate',
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
    			pb.CallAction('/Home/Login', 'corpCode=' + c.vm.CorpCode+'&code=' + c.vm.Code + '&password=' + hex_md5(c.vm.Password));
    		};
    		c.setLang=function(lang){
    			$translate.use(lang);
    		};
}]).ForceViewByAction();


app.controller("LeftViewCtrl", ['pb', '$scope', '$transitions', function (pb, $scope, $transitions) {
	var c=this;

	$transitions.onSuccess({}, 
    	    function(trans) {
    	        	c.showView=(trans.to().name!="/Home/ShowLogin");
    	    });	
}]);
app.controller("TopViewCtrl", ['pb', '$scope', '$state', '$transitions', 'App_FuncTree', function (pb, $scope, $state,$transitions, App_FuncTree) {
    var c = this;
    c.showView = true;
    c.vm = { Input: {} }; //不是state又没使用init-vm,则需要编程初始化vm
    $transitions.onStart({},
	function (trans) {
	    c.showView = (trans.to().name != "/Home/ShowLogin");
	    if (c.showView && ($scope.baseCtrl.vm == undefined || $scope.baseCtrl.vm.LoginCorpName == undefined)) {
	        return pb.AjaxSubmit(null, null, { "ajax-url": '/Home/MainFrame' }, function (result) {
	            $scope.baseCtrl.vm = result.data.ViewModel;
	            if (result.data.ViewModel == null) {
	                return trans.router.stateService.target('/Home/ShowLogin');
	            }
	            $scope.baseCtrl.InitMenu(result.data.ViewModel.ForbiddenFuncList);
	            App_FuncTree.SetForbiddenFuncList(result.data.ViewModel.ForbiddenFuncList);
	        });
	    }
	});
    
    c.Html_Load = function () {
        ////app.GlobalDict.LoadSelector('app.GlobalDict.PartyClass');
    };
    c.btnLogin_click = function () {
        if (c.frmTopLogin.$invalid) {
            return;
        }
        pb.CallAction('/Home/Login', 'corpCode=1&code=' + c.vm.input.code + '&password=' + hex_md5(c.vm.input.password));
    };
    c.btnLogout_click = function () {
        $scope.baseCtrl.vm = {};
        pb.CallAction('/Home/Logout');
    };
} ]);

