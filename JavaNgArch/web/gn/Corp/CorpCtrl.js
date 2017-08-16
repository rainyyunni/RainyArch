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


