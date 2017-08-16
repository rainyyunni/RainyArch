<%@ page pageEncoding="UTF-8" %>
<p:viewdata model="TaskItemListVM"/>	
<div class="table-responsive"><table class="table table-striped table-bordered table-hover" >
	<thead><tr pb-sort-input="input.orderExpression" pb-sort-model="c.vm.input.orderExpression" ajax-url='' ajax-bind='parent'>
        <th><pb-checkall list-model="c.vm.resultList" list-prop="id" selected-model="c.vm.input.selectedValues"></pb-checkall>{{'Select'|translate}}</th>
		<th  nowrap pb-sort-expr="this.task"><span  translate="Task"></span></th>
		<th  pb-sort-expr="this.brief"><span  translate="TaskItem_Brief"></span></th>
		<th  nowrap pb-sort-expr="this.user"><span  translate="User"></span></th>
		<th  pb-sort-expr="this.requirement"><span  translate="TaskItem_Requirement"></span></th>
		<th  pb-sort-expr="this.record"><span  translate="TaskItem_Record"></span></th>
		<th  pb-sort-expr="this.keyInfo"><span  translate="TaskItem_KeyInfo"></span></th>
		<th  nowrap pb-sort-expr="this.createDate"><span  translate="TaskItem_CreateDate"></span></th>
		<th  nowrap pb-sort-expr="this.actionDate"><span  translate="TaskItem_ActionDate"></span></th>
		<th  nowrap pb-sort-expr="this.status"><span  translate="TaskItem_Status"></span></th>
		<th  nowrap pb-sort-expr="this.orderNum"><span  translate="TaskItem_OrderNum"></span></th>

	</tr></thead>
	<tbody><tr ng-repeat="item in c.vm.resultList">
        <th class="col-sm-1">
        	<input type="checkbox" name="input.selectedValues" value="{{item.id}}" ng-true-value="{{item.id}}" ng-model="c.vm.input.selectedValues[$index]"/>
        </th>
		<td><a ${Url.State("forward:Edit(id:item.id)")}>{{::item.taskName}}</a></td>
		<td><a ${Url.State("forward:Edit(id:item.id)")}>{{::item.brief}}</a></td>
		<td>{{::item.userName}}</td>
		<td>{{::item.requirement}}</td>
		<td>{{::item.record}}</td>
		<td>{{::item.keyInfo}}</td>
		<td>{{::item.createDate|Display}}</td>
		<td>{{::item.actionDate|Display}}</td>
		<td>{{::item.status|Display:'Task_StatusEnum'}}</td>
		<td>{{::item.orderNum}}</td>

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

         

