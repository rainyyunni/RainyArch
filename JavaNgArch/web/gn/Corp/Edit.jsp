<%@ page pageEncoding="UTF-8" %>
<p:viewdata model="CorpEditVM" />
<html:form ajax-url='${Url.Action("Save")}' name="c.frmEdit">
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
		<html:label path="vm.input.phone" class="col-sm-2 control-label"/>
		<div class="col-sm-4"><html:input path="vm.input.phone"   class="form-control"/></div>
	</div>

	<div class="form-group">
	<span translate="CorpContact" class="col-sm-2 control-label"></span>
<div class="col-sm-8 table-responsive"><table class="table table-striped table-bordered table-hover" >
	<thead><tr >
		<th><span  translate="CorpContact_Phone"></span></th>
		<th><span  translate="CorpContact_Name"></span></th>
		<th><span  translate="CorpContact_Position"></span></th>
		<th><span  translate="Operation"></span></th>
	</tr></thead>
	<tbody><tr ng-repeat="item in c.vm.input.contactList track by $index" ng-if="item">
		<td><html:input path="vm.input.newContact.phone" ng-model="c.vm.input.contactList[$index].phone" /></td>
		<td><html:input path="vm.input.newContact.name" ng-model="c.vm.input.contactList[$index].name" /></td>
		<td><html:input path="vm.input.newContact.position" ng-model="c.vm.input.contactList[$index].position" /></td>
		<td>
			<button translate="Delete"  type="button" class="btn btn-default btn-xs" ng-click="c.btnDeleteRow_click($index)"/>
		</td>
	</tr>
	<tr>
		<td><html:input path="vm.input.newContact.phone" name="" pb-group="newrow" /></td>
		<td><html:input path="vm.input.newContact.name"  name="" pb-group="newrow" /></td>
		<td><html:input path="vm.input.newContact.position"  name="" pb-group="newrow"/></td>
		<td><button translate="Add"  type="button" class="btn btn-default btn-xs" ng-click="c.btnAddRow_click()"/>
		</td>
	</tr>
	</tbody>
</table></div>
    </div>
    <div class="form-group text-center col-sm-6">
    	<button translate="Save" type="button" ajax-url='' class="btn btn-default"/>
    </div>
</div>
<br>
</html:form>



