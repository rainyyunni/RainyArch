<%@ page pageEncoding="UTF-8" %>
<p:viewdata model="TaskEditVM" />
<html:form ajax-url='${Url.Action("Save")}' name="c.frmEdit" >
<div class="form-horizontal">
	<html:hidden path="vm.input.id"/>
		
	<div class="form-group">
		<html:label path="vm.input.name" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.name"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.user" class="col-sm-2 control-label" translate="User"/>
		<div class="col-sm-4"><html:select path="vm.input.user" selectList="c.vm.users" optionLabel="-choose-"  class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.createDate" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.createDate"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.planBeginDate" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.planBeginDate"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.planEndDate" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.planEndDate"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.beginDate" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.beginDate"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.endDate" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.endDate"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.status" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:select path="vm.input.status"  optionLabel="-choose-"  class="form-control"/></div>
	</div>

	<div class="form-group text-center col-sm-6">
    	<button translate="Save" type="button" ajax-url=''  class="btn btn-default"/>
    </div>
</div>
</html:form>



