def.ns="/home/Home/";
def.RootState('Introduction.htm').NoController();
def.StaticState('Architecture.htm');

app.controller("LeftViewCtrl", ['pb','$scope',function (pb,$scope) {
	var c=this;

}]);
app.controller("TopViewCtrl", ['pb','$scope','$state','$translate','$transitions','App_MenuData',
                               function (pb, $scope,$state,$translate,$transitions,App_MenuData) {
    var c = this;
    c.navbarCollapsed=true;
    $scope.baseCtrl.InitMenu('',App_MenuData);
    $scope.baseCtrl.CurrentSubMenus=App_MenuData[0].SubMenus;

	c.lnkMenu_click=function(topmenu){
		$scope.baseCtrl.CurrentSubMenus=topmenu.SubMenus;
		$state.go(topmenu.SubMenus[0].StateName);
	};

	c.setLang=function(lang){
		$scope.baseCtrl.SetLang(lang);
		$translate.use(lang);
	};
}]);

