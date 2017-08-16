<%@ page pageEncoding="UTF-8" %>
<p:viewdata model="TaskListVM"/>	
<div class="table-responsive"><table class="table table-striped table-bordered table-hover" >
	<thead><tr pb-sort-input="input.orderExpression" pb-sort-model="c.vm.input.orderExpression" ajax-url='' ajax-bind='parent'>
        <th><pb-checkall list-model="c.vm.resultList" list-prop="id" selected-model="c.vm.input.selectedValues">></pb-checkall>{{'Select'|translate}}</th>
		<th  pb-sort-expr="this.name"><span  translate="Task_Name"></span></th>
		<th  nowrap pb-sort-expr="this.user"><span  translate="User"></span></th>
		<th  nowrap pb-sort-expr="this.createDate"><span  translate="Task_CreateDate"></span></th>
		<th  nowrap pb-sort-expr="this.planBeginDate"><span  translate="Task_PlanBeginDate"></span></th>
		<th  nowrap pb-sort-expr="this.planEndDate"><span  translate="Task_PlanEndDate"></span></th>
		<th  nowrap pb-sort-expr="this.beginDate"><span  translate="Task_BeginDate"></span></th>
		<th  nowrap pb-sort-expr="this.endDate"><span  translate="Task_EndDate"></span></th>
		<th  nowrap pb-sort-expr="this.status"><span  translate="Task_Status"></span></th>

	</tr></thead>
	<tbody><tr ng-repeat="item in c.vm.resultList">
        <th class="col-sm-1">
        	<input type="checkbox" name="input.selectedValues" value="{{item.id}}" ng-true-value="{{item.id}}" ng-model="c.vm.input.selectedValues[$index]"/>
        </th>
		<td><a ${Url.State("forward:Edit(id:item.id)")}>{{::item.name}}</a></td>
		<td>{{::item.user}}</td>
		<td>{{::item.createDate|Display}}</td>
		<td>{{::item.planBeginDate|Display}}</td>
		<td>{{::item.planEndDate|Display}}</td>
		<td>{{::item.beginDate|Display}}</td>
		<td>{{::item.endDate|Display}}</td>
		<td>{{::item.status|Display:'Task_StatusEnum'}}</td>

	</tr></tbody>
</table></div>
<div class="row text-center">
	<div pager-input="input.pager" pager-model="c.vm.input.pager" pager-sizemax="200" pager-short="false" ajax-url='' ajax-bind='parent'></div>
</div>
<div class="row text-center">
	<button translate="Add"  type="button" ${Url.State("forward:Add")} class="btn btn-default"/>
	<button translate="Delete"  type="button" ajax-url='${Url.Action("Delete")}' ajax-confirm-multi="c.vm.input.selectedValues" class="btn btn-default"/>
</div>
<br>
<div ui-view></div>

         

