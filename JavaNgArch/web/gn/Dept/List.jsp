<%@ page pageEncoding="UTF-8" %>
<p:viewdata model="DeptListVM"/>	
<html:form name="c.frmSearch"  role="form" ajax-url='${Url.Action("List")}' >
<div class="row"><div class="col-sm-6" id="tbl">
	<div class="row gridrow gridrow-top"  pb-sort-input="input.orderExpression" pb-sort-model="c.vm.input.orderExpression" ajax-url='' ajax-bind='parent'>
        <div class="col-sm-1 gridcell" translate="Select"></div>
		<div class="col-sm-4 gridcell" pb-sort-expr="this.code" ><span translate="Dept_Code" ></span></div>
		<div class="col-sm-4 gridcell" pb-sort-expr="this.name"><span translate="Dept_Name" ></span></div>
		<div class="col-sm-3 gridcell"  translate="Operation"></div>
	</div>
	<div class="row gridrow" ng-repeat="item in c.vm.resultList track by $index" ng-if="item" ng-hide="c.vm.editInput==item">
        <div class="col-sm-1 gridcell">
        	<input type="checkbox" name="input.selectedValues" value="{{item.id}}" ng-true-value="{{item.id}}" ng-model="c.vm.input.selectedValues[$index]"/>
        </div>
		<div class="col-sm-4 gridcell"><a ${Url.State("forward:Edit(id:item.id)")}>{{item.code}}</a></div>
		<div class="col-sm-4 gridcell">{{item.name}}</div>
		<div class="col-sm-3 gridcell" >
			<button translate="Edit"  type="button" class="btn btn-default btn-xs" ng-click="c.btnEditInLine_click(item,$index)"/>
			<button translate="Delete"  type="button" class="btn btn-default btn-xs" ng-click="c.btnDeleteInLine_click($index)"/>
		</div>
	</div>
	<div class="row gridrow" ng-hide="c.tmp.IsInLine" id="divNewRow">
        <div class="col-sm-1 gridcell">
        </div>
		<div class="col-sm-4 gridcell"></div>
		<div class="col-sm-4 gridcell"></div>
		<div class="col-sm-3 gridcell" >
			<button translate="Add"  type="button" class="btn btn-default btn-xs" ng-click="c.btnAddInLine_click()"/>
		</div>
	</div>
	<div class="row" >
		<div pager-input="input.pager" pager-model="c.vm.input.pager" pager-sizemax="200" pager-short="false" ajax-url='' ajax-bind='parent'></div>
	</div>
	<div class="row text-center">
		<button translate="Add"  type="button" ${Url.State("forward:Add")} class="btn btn-default"></button>
		<button translate="Delete"  type="button" ajax-url='${Url.Action("Delete")}' ajax-confirm-multi="c.vm.input.selectedValues" class="btn btn-default"></button>
		<br><span translate>ExperienceDBError</span>
	</div>
</div></div>
</html:form>
<div id="divStandBy">
	<div id="divEdit" class="row gridrow" ng-show="c.tmp.IsInLine">
		<html:form name="c.frmEdit"  role="form" ajaxAction='SaveInLine'>
		       <div class="col-sm-1 gridcell">
		       <html:hidden path="vm.editInput.id" />
		      </div>
			<div class="col-sm-4 gridcell"><html:input path="vm.editInput.code"   class="form-control"/></div>
			<div class="col-sm-4 gridcell"><html:input path="vm.editInput.name"   class="form-control"/></div>
			<div class="col-sm-3 gridcell" >			
				<button  id="btnSave" translate="Save"  type="button" class="btn btn-default btn-xs" ng-click='c.btnSaveInLine_click()'/>
				<button translate="Cancel"  type="button" class="btn btn-default btn-xs" ng-click='c.btnCancel_click()'/>
			</div>
		</html:form>
	</div>    
</div>
<br>
<div ui-view></div>