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