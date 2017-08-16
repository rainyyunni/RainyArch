def.ns = "/ta/Task/";
def.ContentState('Edit', "id").Controller(['pb', 'serverVm', '$scope',function (pb, serverVm, $scope) {
    var c = pb.Super(this, serverVm, $scope);

    c.ExecuteNoopResult = function (elementid) {
        pb.AjaxNavBack();
    };

}]);
def.ContentState('Add').WithController('Edit');
def.ContentState('Search').WithChild('List').WithChild('SrcCode.htm').NoResolve();