def.ns = "/GN/User/";
def.ContentState('Edit', "id").Controller(['pb', 'serverVm', '$scope', 'App_FuncTree', function (pb, serverVm, $scope, App_FuncTree) {
    var c = pb.Super(this, serverVm, $scope, false);

    c.ExecuteNoopResult = function (elementid) {
        pb.AjaxNavBack();
    };
    c.btnSave_click = function () {
        var s = App_FuncTree.GetUncheckedListString(c.tmp_treeModel);
        pb.AjaxSubmit('btnSave', null, { "ajax-data": "Input.UserFuncIds=" + s });
    };
}]);
def.ContentState('Add', "deptId").WithController('Edit');
def.ContentState('Search').Controller(['pb', 'serverVm', '$scope',function (pb, serverVm, $scope) {
    var c = pb.Super(this, serverVm, $scope, true);

    c.dept_change = function (event) {
        pb.AjaxSubmit('frmSearch');
    };
} ]).WithChild('List').NavBackRefresh(['pb', function (pb) {
    pb.AjaxSubmit('frmSearch');
} ]);
def.ContentState('PassEdit.htm').Controller(['pb', '$scope', 'pbui', function (pb, $scope, pbui) {
    var c = pb.Super(this, {}, $scope, false);

    c.btnOK_click = function () {
        if (c.frmPass.$invalid) {
            return;
        }
        pb.AjaxSubmit(null, null, { "ajax-url": '/GN/User/SavePassword', "ajax-data": 'OldPassword=' + hex_md5(c.vm.OldPassword) + '&NewPassword=' + hex_md5(c.vm.NewPassword) });
    };
    c.btnDialogSample_click = function () {
        pbui.Dialog('../Scripts/tpl/pbui.DialogSample.htm', { Title: 'DialogSampleTitle' },
        		['$scope', '$uibModalInstance', 'paramObj', function ($scope, $uibModalInstance, paramObj) {
        		    var dc = this;
        		    dc.Title = paramObj.Title;
        		    dc.save = function () {
        		        $uibModalInstance.close({ ReturnText: dc.InputText });
        		    };

        		    dc.cancel = function () {
        		        $uibModalInstance.dismiss({ ReturnText: 'canceled' });
        		    };
        		} ]).then(function (rtn) {
        		    pbui.Alert(rtn.ReturnText);
        		},function (rtn) {
        		    pbui.Alert(rtn.ReturnText);
        		});
    };
} ]);