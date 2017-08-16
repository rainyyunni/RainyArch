<%@ page pageEncoding="UTF-8" %>
<p:viewdata model="UserListVM"/>	
<div class="table-responsive"><table class="table table-striped table-bordered table-hover" >
	<thead><tr pb-sort-input="input.orderExpression" pb-sort-model="c.vm.input.orderExpression" ajax-url='' ajax-bind='parent'>
        <th translate="Select"></th>
		<th  pb-sort-expr="this.code" ><span translate="User_Code"></span></th>
		<th  pb-sort-expr="this.name" ><span translate="User_Name"></span></th>
		<th  translate="User_CellPhone"></th>
		<th  translate="User_IsActive"></th>
	</tr></thead>
	<tbody><tr ng-repeat="item in c.vm.resultList">
        <th>
        	<input type="checkbox" name="input.selectedValues" value="{{item.id}}" ng-true-value="{{item.id}}" ng-model="c.vm.input.selectedValues[$index]"/>
        </th>
		<td><a ${Url.State("forward:Edit(id:item.id)")}>{{::item.code}}</a></td>
		<td>{{::item.name}}</td>
		<td>{{::item.cellPhone}}</td>
		<td>{{::item.isActive|Display}}</td>
	</tr></tbody>
</table></div>
<div class="row text-center" >
	<div pager-input="input.pager" pager-model="c.vm.input.pager" pager-sizemax="200" pager-short="false" ajax-url='' ajax-bind='parent'></div>
</div>
<div class="row text-center" >
	<button translate="Add"  type="button" ${Url.State("forward:Add(deptId:c.vm.deptId)")} class="btn btn-default"/>
	<button translate="Delete"  type="button" ajax-url='${Url.Action("Delete")}' ajax-confirm-multi="c.vm.input.selectedValues" class="btn btn-default"/>
</div>

         

