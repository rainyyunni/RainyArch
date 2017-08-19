def.ns = "/TA/Task/";
def.ContentState('Edit', "id").Controller(['pb', 'serverVm', '$scope',function (pb, serverVm, $scope) {
    var c = pb.Super(this, serverVm, $scope, false);

    c.ExecuteNoopResult = function (elementid) {
        pb.AjaxNavBack();
    };

}]);
def.ContentState('Add').WithController('Edit');
def.ContentState('Search').WithChild('List', 'test').Controller(['pb', 'serverVm', '$scope', function (pb, serverVm, $scope) {
    var c = pb.Super(this, serverVm, $scope, false);
    c.btnOK_click = function () {
        if (c.frmEdit.$invalid) {
            return;
        }
    };

} ]);