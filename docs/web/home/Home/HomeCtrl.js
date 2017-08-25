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
    $scope.baseCtrl.currentSubMenus=App_MenuData[0].subMenus;

	c.lnkMenu_click=function(topmenu){
		$scope.baseCtrl.currentSubMenus=topmenu.subMenus;
		$state.go(topmenu.subMenus[0].stateName);
	};

	c.setLang=function(lang){
		$scope.baseCtrl.SetLang(lang);
		$translate.use(lang);
	};
}]);

