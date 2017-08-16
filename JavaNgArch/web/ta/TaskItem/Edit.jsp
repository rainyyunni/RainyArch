<%@ page pageEncoding="UTF-8" %>
<p:viewdata model="TaskItemEditVM" />
<html:form ajax-url='${Url.Action("Save")}' name="c.frmEdit" >
<div class="form-horizontal">
	<html:hidden path="vm.input.id"/>
		
	<div class="form-group">
		<html:label path="vm.input.task" class="col-sm-2 control-label"  translate="Task"/>
		<div class="col-sm-4"><html:select path="vm.input.task" selectList="c.vm.tasks" optionLabel="-choose-"  class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.brief" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.brief"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.user" class="col-sm-2 control-label" translate="User"/>
		<div class="col-sm-4"><html:select path="vm.input.user" selectList="c.vm.users" optionLabel="-choose-"  class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.requirement" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.requirement"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.record" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.record"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.keyInfo" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.keyInfo"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.createDate" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.createDate"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.actionDate" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.actionDate"   class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.status" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:select path="vm.input.status"  optionLabel="-choose-"  class="form-control"/></div>
	</div>
		
	<div class="form-group">
		<html:label path="vm.input.orderNum" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.orderNum"   class="form-control"/></div>
	</div>

	<div class="form-group text-center col-sm-6">
    	<button translate="Save" type="button" ajax-url=''  class="btn btn-default"/>
    </div>
</div>
</html:form>



