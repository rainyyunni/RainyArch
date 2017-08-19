def.ns = "/TA/TaskItem/";
def.ContentState('Edit', "id").Controller(['pb', 'serverVm', '$scope',function (pb, serverVm, $scope) {
    var c = pb.Super(this, serverVm, $scope, false);

    c.ExecuteNoopResult = function (elementid) {
        pb.AjaxNavBack();
    };

}]);
def.ContentState('Add').WithController('Edit');
def.ContentState('Search').WithChild('List');