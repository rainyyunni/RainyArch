<%@ page pageEncoding="UTF-8" %>
  <p:viewdata model="UserSearchVM" />
  <html:form id="frmSearch" name="c.frmSearch"  role="form" ajax-url='${Url.Action("List")}' ajax-bind="divMain">
  <div class="row">
    	<div class="col-sm-2 col-md-2">
        <html:select path="vm.input.dept" selectList='c.vm.depts' optionLabel="-all-" class="form-control" size="30" ng-change="c.dept_change()">
        </html:select>
       </div>
      <div id="divMain" class="col-sm-10 col-md-10" ui-view></div>
   </div>
  </html:form>

  


