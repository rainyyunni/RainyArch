def.ns = "/gn/Dept/";
def.ContentState('Edit', "id").Controller(['pb', 'serverVm', '$scope','App_FuncTree',function (pb, serverVm, $scope,App_FuncTree) {
    var c = pb.Super(this, serverVm, $scope);

    c.ExecuteNoopResult = function (elementid) {
        pb.AjaxNavBack();
    };
    c.btnSave_click = function () {
    	var s=App_FuncTree.GetUncheckedListString(c.tmp_treeModel);
        pb.AjaxSubmit('btnSave',null,{"ajax-data":"input.deptFuncIds="+s});
    };
}]);
def.ContentState('Add').WithController('Edit');
def.ContentState('List')
.Controller(['pb', 'serverVm', '$scope','$window','$uibPosition','pbui',function (pb, serverVm, $scope,$window,$uibPosition,pbui) {
    var c = pb.Super(this, serverVm, $scope);
    
    c.vm.editInput={id:0};
    c.tmp.IsInLine=false;
    
    c.btnDeleteInLine_click= function (idx) {
    	var item=c.vm.resultList[idx];
        pbui.Confirm('ConfirmDelete',item.name).then(function(){
        	pb.CallAction('/do/gn/Dept/DeleteInLine?id='+item.id,null,function(result){
        		if(result.data==true){
        			delete c.vm.resultList[idx];
        		}
        	});
        });

    };
    c.btnAddInLine_click= function () {
    	c.moveToLine(-1);
    };
    c.btnEditInLine_click= function (item,index) {
    	c.moveToLine(index);
    	c.vm.editInput=item;
    	c.tmp.backupItem=angular.copy(item);
    };
    c.btnSaveInLine_click = function () {
    	pb.AjaxSubmit('c.frmEdit',null,null,function(result){
    		if(!result.IsData)return;
    		if(c.vm.editInput.id==0){
    			c.vm.editInput.id=result.data;
    			c.vm.resultList[c.vm.resultList.length]=c.vm.editInput;
    		}
    		c.resetEditLine();
    	});
    };
    c.btnCancel_click = function () {
    	angular.copy(c.tmp.backupItem,c.vm.editInput);
    	c.resetEditLine();
    };
    c.resetEditLine=function(){
		c.vm.editInput={id:0};
        c.frmEdit.$setPristine();
        c.moveToLine(null);
    };
    c.moveToLine=function(rowIndex){
    	var tbl=pb.ElementById('tbl');
    	var divEdit=pb.ElementById('divEdit');
    	var divNewRow=pb.ElementById('divNewRow');
    	c.tmp.IsInLine=true;
    	if(rowIndex==null){
        	c.tmp.IsInLine=false;
    		pb.ElementById('divStandBy').append(divEdit);
    	}
    	else if(rowIndex==-1)
    		divNewRow.after(divEdit);
    	else
    		tbl.children().eq(rowIndex+1).after(divEdit);
    };
}]).WithChild("SrcCode.htm").NoController();
