<%@ page pageEncoding="UTF-8" %>
<p:viewdata model="UserEditVM" />
<html:form ajax-url='${Url.Action("Save")}' name="c.frmEdit" >
<div class="form-horizontal">
	<html:hidden path="vm.input.id"/>
		
	<div class="form-group">
		<html:label path="vm.input.dept" class="col-sm-2 control-label" translate="Dept"/>
		<div class="col-sm-4"><html:select path="vm.input.dept" selectList="c.vm.depts" optionLabel="-choose--" class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.code" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.code"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.name" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.name"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.cellPhone" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.cellPhone"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.isActive" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:checkbox path="vm.input.isActive"   class="form-control"/></div>
	</div>
	<div class="form-group">
	<pb-functree input-model="c.vm.input" tree-model="c.tmp_treeModel" disabled="!c.vm.canChangeUserFunc"></pb-functree>
	</div>
	<div class="form-group text-center col-sm-6">
    	<button id="btnSave" translate="Save" type="button" ng-click="c.btnSave_click()" class="btn btn-default"/>
    </div>
</div>
</html:form>



