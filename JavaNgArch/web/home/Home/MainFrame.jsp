<%@ page contentType="text/html; charset=UTF-8" %>
<p:viewdata model="LoginInfoViewModel" />
<!DOCTYPE html>
<html ng-app="app" lang="zh-CN">
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <title>WebApp开发框架+成品应用原型   纯真年代 </title>
	<link href="/Content/css/bootstrap.css" rel="stylesheet" />
	<link href="/Content/css/bootstrap-theme.css" rel="stylesheet" />
	<link href="/Content/css/Site.css" rel="stylesheet" />
	<script src="/Scripts/angular.js"></script>
	<script src="/Scripts/lang/angular-locale_zh-cn.js"></script>
	<script src="/Scripts/angular-animate.js"></script>
    <script src="/Scripts/angular-cookies.js"></script>
    <script src="/Scripts/angular-translate.js"></script>
    <script src="/Scripts/angular-translate-loader-static-files.js"></script>
    <script src="/Scripts/angular-translate-storage-cookie.js"></script>
    <script src="/Scripts/angular-translate-storage-local.js"></script>
    <script src="/Scripts/angular-ui-router.js"></script>
    <script src="/Scripts/jcs-auto-validate.js"></script>
    <script src="/Scripts/ui-bootstrap-tpls-2.2.0.js"></script>
   <script src="/Scripts/md5-min.js"></script>

    <script src="/Scripts/ProjectBase_Common.js"></script>
   <script src="/Scripts/ProjectBase_Ajax.js"></script>
   <script src="/Scripts/ProjectBase_UI.js"></script>
	<script src="/Scripts/App_Common.js"></script>
	<script src="/Scripts/App_Menu.js"></script>
	<script src="/Scripts/lang/dict-zh-cn.js"></script>
	<script src="/do/shared/Common/NgControllerJs?action"></script>
   <script>
   //make sure this be the last-loaded script
   def.RegisterImplicitControllers();
   </script>
   
  </head>
 <%--  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <title>WebApp开发框架+成品应用原型   纯真年代 </title>
	<link href="/Content/css/bootstrap.css" rel="stylesheet" />
	<link href="/Content/css/bootstrap-theme.css" rel="stylesheet" />
	<link href="/Content/css/Site.css" rel="stylesheet" />
	<script src="/Scripts/angular.min.js"></script>
	<script src="/Scripts/lang/angular-locale_zh-cn.js"></script>
	<script src="/Scripts/angular-animate.min.js"></script>
    <script src="/Scripts/angular-cookies.min.js"></script>
    <script src="/Scripts/angular-translate.min.js"></script>
    <script src="/Scripts/angular-translate-loader-static-files.min.js"></script>
    <script src="/Scripts/angular-translate-storage-cookie.min.js"></script>
    <script src="/Scripts/angular-translate-storage-local.min.js"></script>
    <script src="/Scripts/angular-ui-router.min.js"></script>
    <script src="/Scripts/jcs-auto-validate.js"></script>
    <script src="/Scripts/ui-bootstrap-tpls-2.2.0.js"></script>
   <script src="/Scripts/md5-min.js"></script>
   <!--  -->
    <script src="/Scripts/ProjectBase_Common.min.js"></script>
   <script src="/Scripts/ProjectBase_Ajax.min.js"></script>
   <script src="/Scripts/ProjectBase_UI.min.js"></script>
	<script src="/Scripts/App_Common.min.js"></script>
	<script src="/Scripts/App_Menu.js"></script>
	<script src="/Scripts/lang/dict-zh-cn.js"></script>
	<script src="/Scripts/RunTimeCtrls.min.js"></script>
   <script>
   //make sure this be the last-loaded script
   def.RegisterImplicitControllers();
   </script>
   
  </head>--%>
  <style>.plh{line-height:25px;}</style>
  <body ng-controller="BaseCtrl as baseCtrl" >
 	<nav class="navbar navbar-default" role="navigation"><!-- header -->
		<div ui-view="topview" ng-controller="TopViewCtrl as tvCtrl" onload="tvCtrl.Html_Load();" class="container-fluid">
			<div class="navbar-header">
			      <button type="button" class="navbar-toggle" ng-click="tvCtrl.navbarCollapsed=!tvCtrl.navbarCollapsed" ng-class="tvCtrl.navbarCollapsed?'collapsed':''" aria-expanded="false">
			        <span class="sr-only">Toggle navigation</span>
			        <span class="icon-bar"></span>
			        <span class="icon-bar"></span>
			        <span class="icon-bar"></span>
			      </button>
				<a class="navbar-brand" href="http://www.xinchunzhen.com">
					<img alt="Brand" src="/Content/images/czlogo_small.jpg" width="100" height="35"></img>
				</a>
	    	</div>
	    	<ul class="nav navbar-nav" ng-show="!baseCtrl.vm.loginCorpName">
	    		<li><h4 class="text-right" translate>ToChildren</h4></li>
        	</ul>
        	<ul class="nav navbar-nav" ng-show="!baseCtrl.vm.loginCorpName">
	    		<li><a href="#" class="btn-small" ng-click="tvCtrl.setLang('zh-cn');" translate="Chinese"></a></li>
        		<li><a href="#"  class="btn-small" ng-click="tvCtrl.setLang('en');" translate="English"></a></li>
        	</ul>
	    	<div class="navbar-collapse" ng-class="tvCtrl.navbarCollapsed?'collapse':''">
	    		<ul class="nav navbar-nav" ng-if="baseCtrl.vm.loginCorpName">
	    			<li uib-dropdown ng-repeat="item in baseCtrl.menuData" ng-class="item.status">
	    				<a  ng-class="item.status" class="dropdown-toggle"  uib-dropdown-toggle role="button">{{::item.text|translate}}<span class="caret"></span></a>
	    				<ul class="dropdown-menu" uib-dropdown-menu  >
				            <li ng-repeat="subitem in item.subMenus" ng-class="subitem.status">
				            	<a ng-if="subitem.status!='disabled'" ui-sref="{{::subitem.sref}}" ui-sref-opts="{reload:true,inherit:false}" ng-click="tvCtrl.navbarCollapsed=true">{{::subitem.text|translate}}</a>
				            	<a ng-if="subitem.status=='disabled'" >{{::subitem.text|translate}}</a>
				            </li>
				        </ul>
	    			</li>
	    		</ul>
	    		<html:form name="tvCtrl.frmTopLogin" class="navbar-form navbar-right" role="login" ng-show="!baseCtrl.vm.loginCorpName">
			        <div class="form-group">
			          	<html:label path="vm.input.code" class="control-label"/><html:input path="vm.input.code"  class="form-control" />
			          	<html:label path="vm.input.password" class="control-label"/><html:password path="vm.input.password" class="form-control"/>
			        </div>
			        <button type="button" class="btn btn-default" ng-click="tvCtrl.btnLogin_click()" translate>Login</button>
				</html:form>
				<ul class="nav navbar-nav navbar-right" ng-if="baseCtrl.vm.loginCorpName">
					<li class="navbar-text"><img src="/Content/images/user.gif"/>{{baseCtrl.vm.loginCorpName}}-{{baseCtrl.vm.loginUserName}} </li>
					<li><a href="#" ng-click="tvCtrl.navbarCollapsed=true;tvCtrl.btnLogout_click();" ><img src="/Content/images/logout.gif"/>{{'Logout'|translate}}</a></li>
				</ul>
	    	</div>
	    </div>
	</nav>
	<div class="container-fluid">
		<div class="row">
		 	<div ui-view="leftview" ng-controller="LeftViewCtrl as lvCtrl" ng-show="lvCtrl.showView"  class="col-sm-2  visible-md-block visible-lg-block"><!-- left menu  -->
		 		<div class="list-group" >
					<a ng-if="subitem.status!='disabled'" ng-class="subitem.status" class="list-group-item" ng-repeat="subitem in baseCtrl.currentSubMenus" ui-sref="{{::subitem.sref}}"  ui-sref-opts="{reload:true,inherit:false}">
						{{::subitem.text|translate}}
					</a>
				</div>
			</div>
			<div id="divMainContentView" ui-view  class="col-sm-{{baseCtrl.showLeftView?10:12}}"><!-- main content  -->
			</div>
		</div>
	</div>
	<br>
	<div class="row"><div class="col-sm-offset-1 col-sm-10">
		<div ng-if="baseCtrl.showLeftView">
			<div ng-include="'/home/Home/RainyArch.htm'"></div>
			<!-- <div ng-include="'/home/Home/Service.htm'"></div>
			<div ng-include="'/home/Home/Training.htm'"></div> -->
		</div>
		<h2 translate="Diagram"></h2>
	  <uib-tabset  active="active">
    <uib-tab index="0" heading="NetNgArch">
<img class="img-responsive" src="/home/Home/RainyArch_Net_{{baseCtrl.Lang}}.jpg"></img>
</uib-tab>
    <uib-tab index="1" heading="JavaNgArch">
<img class="img-responsive" src="/home/Home/RainyArch_Java_{{baseCtrl.Lang}}.jpg"></img>
</uib-tab>
</uib-tabset>
<ul>
<li translate="Diagram1">														
<li translate="Diagram2">															
</ul>
<h2 translate="MvcDiagram"></h2>
<img class="img-responsive" src="/home/Home/RainyArch_Mvc_{{baseCtrl.Lang}}.jpg"></img>
<ul>
<li translate="MvcDiagram1">
<li translate="MvcDiagram2">
<li translate="MvcDiagram3">
<li translate="MvcDiagram4">
<li translate="MvcDiagram5">
<li translate="MvcDiagram6">
<li translate="MvcDiagram7">
<li translate="MvcDiagram8">
<li translate="MvcDiagram9">
<li translate="MvcDiagram10">
</ul>
<p class="h2"><span class="label label-success" translate="Author"></span></p>
</div>
</div>

<br>
	<nav class="navbar navbar-default "><!-- footer -->
		<div class="text-center">
		<br>
		<p translate >WebSiteCopyRight</p>
		</div>
	</nav>
	<pb-processing-mask targetid="divMainContentView" ></pb-processing-mask>
</body>
</html>


