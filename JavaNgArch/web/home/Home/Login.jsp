<%@ page contentType="text/html; charset=UTF-8" %>
<p:viewdata model="LoginAttemptViewModel" serialize="true"/>
  <uib-tabset  justified="true">
    <uib-tab index="0" >
    <uib-tab-heading><strong>WebApp开发——砌砖还是买楼</strong></uib-tab-heading>
	<div ng-include="'/home/Home/RainyArch.htm'"></div>
	</uib-tab>
    <uib-tab index="1" ><uib-tab-heading><strong>所见即所得——用吧!</strong></uib-tab-heading>
  
  <div  pb-initvm='${vmJson}' class="jumbotron" style="background:url(/Content/images/czhome.jpg);background-size:100% 100%;">
  	<div class="row"><div class="col-sm-offset-6 col-sm-5">
  	<div class="panel">
  	  <div class="panel-heading row">
    	<h2 translate class="col-sm-offset-4 col-sm-3">Login</h2>
  	</div>
  	<div class="panel-body">
      <html:form name="c.frmLogin" class="form-horizontal" role="form">
			<div class="form-group">
	            <html:label path="vm.corpCode" class="col-sm-3 control-label"/>
	            <div class="col-sm-8">
	            	<html:input path="vm.corpCode" ng-model-options="{updateOn: 'default blur'}" class="form-control"/>
	       		</div>
	        </div>
	        <div class="form-group">
	            <html:label path="vm.code"  class="col-sm-3 control-label"/>
	            <div class="col-sm-8">
	            	<html:input path="vm.code"  class="form-control" />
	            </div>
	        </div>
	        <div class="form-group">
	            <html:label path="vm.password"  class="col-sm-3 control-label" />
	            <div class="col-sm-8">
	            	<html:password path="vm.password" ng-keydown="c.password_keydown($event);" class="form-control"/>
	            </div>
	           
	        </div>
	        <div class="form-group">
	            <div class="col-sm-offset-2 col-sm-8"><button id="btnLogin" type="button"  ng-click="c.btnLogin_click();" translate="Login" class="btn btn-primary btn-block"></button></div>
	        </div>
        
 			<div class="form-group"><div class="col-sm-offset-4 col-sm-6">
        		<button  type="button"  ng-click="c.setLang('zh-cn');" translate="Chinese"></button>
        		<button  type="button"  ng-click="c.setLang('en');" translate="English"></button>
        		</div>
			</div>
    	</html:form>
    	</div>
    </div>
</div></div></div>


</uib-tab>
	<uib-tab index="2" ><uib-tab-heading><strong>所得胜所见——助力开发</strong></uib-tab-heading>
		<div ng-include="'/home/Home/Service.htm'"></div>
	</uib-tab>
	<uib-tab index="3" >
    <uib-tab-heading><strong>自由码农成长计划——招生招聘</strong></uib-tab-heading>
	<div ng-include="'/home/Home/Training.htm'"></div>
	</uib-tab>
  </uib-tabset>



