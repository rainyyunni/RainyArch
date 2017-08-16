<%@ page pageEncoding="UTF-8" %>
<p:viewdata model="DeptEditVM" />
<html:form ajaxAction="Save" name="c.frmEdit" >
<div class="form-horizontal">
	<html:hidden path="vm.input.id"/>
		
	<div class="form-group">
		<html:label path="vm.input.code" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.code"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.name" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.name"   class="form-control"/></div>
	</div>

	<div class="form-group">
	<pb-functree input-model="c.vm.input" tree-model="c.tmp_treeModel" disabled="!c.vm.canChangeDeptFunc"></pb-functree>
	</div>
	
	<div class="form-group text-center col-sm-6">
    	<button id="btnSave" translate="Save" type="button" ng-click="c.btnSave_click()"  class="btn btn-default"/>
    </div>
</div>
</html:form>



