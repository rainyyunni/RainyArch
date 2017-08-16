<%@ page pageEncoding="UTF-8" %>
  <p:viewdata model="TaskSearchVM" />
  <html:form name="c.frmSearch"  role="form" ajax-url='${Url.Action("List")}'>
    <div class="form-inline">
           <div class="form-group">
       		<html:label path="vm.input.name"/>
            <html:input path="vm.input.name" class="form-control"/>
       </div>
   <%--   [todo:以下查询区里的控件只是例子，应根据界面详细设计进行增删或修改]
    	<div class="form-group">
    		<html:label path="vm.input.doRef" />
            <html:select path="vm.input.doRef" selectList='c.vm.doRefs' optionLabel="" class="form-control">
            </html:select>
       </div>
       <div class="form-group">
       		<html:label path="vm.input.string"/>
            <html:input path="vm.input.string" class="form-control"/>
       </div>
       <div class="form-group">
            <html:label path="vm.input.int"/>
            <html:input path="vm.input.int" class="form-control"/>
       </div>
       	<div class="form-group">
	       	<html:label path="vm.input.boolean"/>
			<html:radiobutton path="vm.input.boolean" class="form-control" value="true"/><span translate>true</span>
			<html:radiobutton path="vm.input.boolean" class="form-control" value="false"/><span translate>false</span>
			<html:radiobutton path="vm.input.boolean" class="form-control" value="null" /><span translate>any</span>
		</div>
--%>
       <div class="form-group">
            <button type="button" translate="Search" ajax-url='' ajax-bind="divMain" class="btn btn-default"/>
 		</div>
 	</div>
    <br/>
    <div id="divMain" ui-view></div>
  </html:form>
 

  


