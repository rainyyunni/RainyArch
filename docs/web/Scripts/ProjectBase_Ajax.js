var PB_Global_StateDefiner;

(function (String, angular) {
    'use strict';
    //first state.
    var AjaxNavRoot = { PrevState: "tobeset", PrevStateParams: null, State:null,StateParams: null,StateData: null};
    var AjaxNavData= {ControllerDataMapForCache:{},Stack:[AjaxNavRoot]};//note that this must be an {} object with a property to store the array,otherwise angular may not track it.
    var ProjectBaseGlobalVar = {//这里的常量与服务器GlobalConstant一致以互相配合使用
	    DefaultPopupWindowFeature: "height=600, width=600, top=50, left=300, toolbar=yes, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no",
	    AuthFailure: "_AuthFailure",
	    AuthFailureMaskElementId: "divAuthFailureMask",
	    KeyForViewModelOnly: "_ForViewModelOnly",
	    Command_Noop:"Noop",
	    Command_Message:"Message",
	    Command_Redirect:"Redirect",  //重定向到state
	    Command_AppPage:"AppPage",//重新进入onepage app
	    Command_ServerVM:"ServerVM",
	    Command_ServerData:"ServerData",
	    Command_AjaxNavVM:"AjaxNavVM",
	    Command_HttpStatus:"HttpStatus",  
	    Command_NewWindow:"NewWindow", //打开新窗口
	    UrlContextPrefix:"tobeset",
	    UrlMappingPrefix:"tobeset",
	    AutoControllerMarker:"PB_Auto",
	    UnSubmitNameMarker:"_"
	};
	var SetResultMarker=function(rcJsonResult){
		var command=rcJsonResult.command;
		if(command==ProjectBaseGlobalVar.Command_ServerVM||rcJsonResult.command==ProjectBaseGlobalVar.Command_AjaxNavVM)rcJsonResult.IsVM=true;
		else if(command==ProjectBaseGlobalVar.Command_Message)rcJsonResult.IsMsg=true;
		else if(command==ProjectBaseGlobalVar.Command_ServerData)rcJsonResult.IsData=true;
		else if(command==ProjectBaseGlobalVar.Command_Noop)rcJsonResult.IsNoop=true;
		else if(command==ProjectBaseGlobalVar.Command_Redirect)rcJsonResult.IsRedirect=true;
		else if(command==ProjectBaseGlobalVar.Command_AppPage)rcJsonResult.IsAppPage=true;
	};
	var PrefixUrl=function(url){
		if(ProjectBaseGlobalVar.UrlContextPrefix!='' && !url.startWith(ProjectBaseGlobalVar.UrlContextPrefix))
			return ProjectBaseGlobalVar.UrlContextPrefix+url;
		return url;
	};
	var DePrefixUrl=function(url){
		if(url.startWith(ProjectBaseGlobalVar.UrlContextPrefix))
			return url.substring(ProjectBaseGlobalVar.UrlContextPrefix.length,url.length);
		return url;
	};
//begin-----------PB_Global_Definer:define state-----------------------------------------------
PB_Global_StateDefiner=function(app){
		var me=this;
		var isViewRef=function(name){
			return name.endWith('.htm') || name.endWith('.html') ||name.endWith('.jsp') || name.endWith('.aspx')|| name.endWith('.cshtml');
		};
		me.ns="";
		me.DefaultLayout="";
		me.tmpStates={};//map
		me.SetDefaultLayout=function(stateName){
			me.DefaultLayout=stateName;
		};
		me.SetUrlContextPrefix=function(prefix){
			ProjectBaseGlobalVar.UrlContextPrefix=prefix;
		};
		me.SetUrlMappingPrefix=function(prefix){
			ProjectBaseGlobalVar.UrlMappingPrefix=prefix;
		};
		me.SetAjaxNavRootState=function(statename){
			AjaxNavRoot.PrevState=statename;
		};
		//create state or view config
		//ownerOfView:the owner state of the view
		me.CreateWrappedState=function(templateUrlOrRawState,params,parent,isHtm,ownerOfView){
			var isForView=ownerOfView?true:false;
			var CustomizedResultGetter,ResultGetter,VmGetter,templateUrlFn,stateOrView,uri,templateUrl,controllerInternalName;
			if(angular.isString(templateUrlOrRawState)){
				templateUrl=(templateUrlOrRawState.startWith('/')?'':me.ns)+templateUrlOrRawState;
				uri=isHtm?templateUrl.substring(0,templateUrl.indexOf('.')):templateUrl;
				if(uri.indexOf('?')>0) uri=uri.substring(0,uri.indexOf('?'));
				if(isForView){
					stateOrView={
						name:isHtm?templateUrlOrRawState.substring(0,templateUrlOrRawState.indexOf('.')):templateUrlOrRawState,
						//templateUrl:isHtm?templateUrl:null,
						controller:ProjectBaseGlobalVar.AutoControllerMarker,
						controllerAs:"c",
						resolve:null
					};
					controllerInternalName=ownerOfView.name+".views_"+stateOrView.name;
				}else{
					stateOrView={
							name:uri,
							url:uri+(params?'?'+params:""),
							//templateUrl:isHtm?templateUrl:null,
							controller:ProjectBaseGlobalVar.AutoControllerMarker,
							controllerAs:"c",
							params: {
	                            'ajax-nav': {
	                                value: undefined,
	                                squash: true,
	                                array:false
	                            } ,
	                            'ajax-nav-back': {
	                                value: undefined,
	                                squash: true,
	                                array:false
	                            }  ,
	                            'UrlTranslate': {
	                                value: undefined,
	                                squash: true,
	                                array:false
	                            } 
							},
							reloadOnSearch:false,
							resolve:null,
							"abstract":false,
							parent:typeof(parent)=='undefined'?me.DefaultLayout:parent
						};
					controllerInternalName=stateOrView.name;
				}
				if(isHtm){
					stateOrView.templateUrl=ProjectBaseGlobalVar.UrlContextPrefix +templateUrl;
				}else{
					templateUrlFn=function(){
						return ProjectBaseGlobalVar.UrlContextPrefix+ProjectBaseGlobalVar.UrlMappingPrefix + templateUrl;//no qs to ensure get view from server only once and use cache thereafter
					};
				    CustomizedResultGetter = ['pb','$transition$',function(pb, $transition$) {//默认AjaxNavBack返回后使用cache
				        var src = pb.AjaxNavGetBackSrc(controllerInternalName,$transition$.params());
				        if (src&&src.IsBack) {
				        	return src.LastData;
				        }
				        return false;
				    }];
					ResultGetter=['pb', 'customizedResultGetter'+(isForView?'_'+stateOrView.name:''),'$transition$',function(pb, customizedResultGetter,$transition$) {
					    if(customizedResultGetter) return customizedResultGetter;
					    var url=ProjectBaseGlobalVar.UrlContextPrefix+ProjectBaseGlobalVar.UrlMappingPrefix+uri;
						if(!url) return null;
					    var p = angular.copy($transition$.params());
					    delete p['ajax-nav'];
					    delete p['ajax-nav-back'];
					    delete p['UrlTranslate'];
						return pb.AjaxSubmit(null,p,{"ajax-url":url,"ajax-bind":"nobind","ajax-method":"GET"},function(responsedata){
                                return responsedata;
						});
					}];
					VmGetter=['pb', 'serverResult'+(isForView?'_'+stateOrView.name:''),function(pb, serverResult) {
						SetResultMarker(serverResult);
						if(serverResult.IsVM){
							if(serverResult.data.ViewModel)
								serverResult.data.ViewModel._FromResult=serverResult;
							else
								console.log("can't append _FromResult, serverResult.data.ViewModel is null");
							return serverResult.data.ViewModel;
						}else if(serverResult.isRcResult==false)
							throw serverResult;//trigger state change error,make state change fail 
						else
							return {};
					}];
					stateOrView.templateUrl=templateUrlFn;
					if(!isForView){
						stateOrView.resolve= {
							customizedResultGetter:CustomizedResultGetter,
						    serverResult:ResultGetter,
						    serverVm:VmGetter
						};
					}else{//different names for resolves
						if(!ownerOfView.resolve) ownerOfView.resolve={};
						ownerOfView.resolve['customizedResultGetter_'+stateOrView.name]=CustomizedResultGetter;
						ownerOfView.resolve['serverResult_'+stateOrView.name]=ResultGetter;
						ownerOfView.resolve['serverVm_'+stateOrView.name]=VmGetter;
					}
				}
			}else{
				stateOrView=templateUrlOrRawState;
			}
		    if(!isForView&&stateOrView&&!stateOrView.data) stateOrView.data= {};
			return {
				isForView:isForView,
				state:stateOrView,
				WithController:function(stateName){//use the given state's controller and template,usually when getting data from a different url
					var st,name;
					if(this.isForView){
						st=this.OwnerState.views[stateName];
						name=this.OwnerState.name+".views_"+st.name;
					}else{
						stateName=(stateName.startWith('/')?'':me.ns)+stateName;
						st = me.tmpStates[stateName];
						name=st.name;
					}
					this.state.controller=st.controller;
				    this.state.templateUrl = st.templateUrl;
				    this.state.templateUrlFn = st.templateUrlFn;
				    var fn=st.controller;
				    var f=angular.isArray(fn)?fn[fn.length-1]:fn;
				    if(f){//named controller can be used by other states/views.
				    	f.prototype._UsedBy=f.prototype._UsedBy||{};
				    	f.prototype._UsedBy[name]=true;
				    }
					return this;
				},
				WithChild:function(childname,childparams){
					if(this.isForView)throw "WithChild is only for state to use";
					var ishtm=isViewRef(childname);
					var ws=me.CreateWrappedState(childname,childparams,this.state.name,ishtm);
					me.tmpStates[ws.state.name]=ws.state;
				    this.state['abstract'] = true;
					return ws;
				},
				Controller:function(fn){
					this.state.controller=fn;
					var f=angular.isArray(fn)?fn[fn.length-1]:fn;
					if(f){
						f.prototype._DefinedBy=this.isForView?
								this.OwnerState.name+".views_"+this.state.name
								:this.state.name;//name controller after state/view name
					}
					return this;
				},
				NoController:function(){
					this.state.controller=null;
					this.state.controllerAs=null;
					return this;
				},
				NoResolve:function(){
					if(!this.isForView){
						this.state.resolve=null;
					}else{//different names for resolves
						delete this.OwnerState.resolve['customizedResultGetter_'+stateOrView.name];
						delete this.OwnerState.resolve['serverResult_'+stateOrView.name];
						delete this.OwnerState.resolve['serverVm_'+stateOrView.name];
					}
					return this;
					
				},
				ResultGetter:function(fn){
					this.state.resolve.customizedResultGetter=fn;
					return this;
				},
				NavBackRefresh:function(fn){//use this to do refresh after navback,otherwise no refresh by default
					if(fn){//run fn after navback
						this.state.Tmp_NavBackRefresh=fn;
					}else{//resolve data from server
						this.state.resolve.customizedResultGetter=function(){return false;};
					}
					return this;
				},
				ForceViewByAction:function(url){
				    if(!url) url = this.state.name;
				    this.state.templateUrl=ProjectBaseGlobalVar.UrlContextPrefix+ProjectBaseGlobalVar.UrlMappingPrefix + url+"?"+ProjectBaseGlobalVar.KeyForViewModelOnly+"=false";
				    this.state.templateFn=null;
					return this;
				},
				NoServerResolve:function(){
					if(!this.isForView && this.state.resolve){
						this.state.resolve.serverResult=function(){return null;};
						this.state.resolve.serverVm=function(){return null;};
					}else if(this.isForView && this.OwnerState.resolve['customizedResultGetter_'+stateOrView.name]){
						this.OwnerState.resolve['serverResult_'+stateOrView.name]=function(){return null;};
						this.OwnerState.resolve['serverVm_'+stateOrView.name]=function(){return null;};
					}
					return this;
				},
				NoServerAction:function(){
					if(!this.isForView && this.state.resolve){
						this.state.resolve.serverResult=function(){return null;};
						this.state.resolve.serverVm=function(){return null;};
					}else if(this.isForView && this.OwnerState.resolve['customizedResultGetter_'+stateOrView.name]){
						this.OwnerState.resolve['serverResult_'+stateOrView.name]=function(){return null;};
						this.OwnerState.resolve['serverVm_'+stateOrView.name]=function(){return null;};
					}
					return this;
				},
				BranchView:function(name,params){
					if(!this.isForView)throw "meant to be called by a wrapped view";
					var isHtm=isViewRef(name);
					var wrappedview=me.CreateWrappedState(name,params,undefined,isHtm,this.OwnerState);
					this.OwnerState.views[wrappedview.state.name]=wrappedview.state;
					wrappedview.OwnerState=this.OwnerState;
					return wrappedview;
				}
			};
		};
		me.RootState=function(name,params){
			var isHtm=isViewRef(name);
			var ws= me.CreateWrappedState(name,params,'',isHtm);
			me.tmpStates[ws.state.name]=ws.state;
			return ws;
		};
		me.LayoutState=function(name,params,parent){
			var isHtm=isViewRef(name);
			if(!parent)parent='';
			var ws=me.CreateWrappedState(name,params,parent,isHtm);
			ws.state['abstract']=true;
			ws.state.controller=null;
			ws.state.resolve=null;
			me.tmpStates[ws.state.name]=ws.state;
			return ws;
		};
		me.ContentState=function(name,params,parent){
			var isHtm=isViewRef(name);
			if(!parent)parent=me.DefaultLayout;
			var ws=me.CreateWrappedState(name,params,parent,isHtm);
			me.tmpStates[ws.state.name]=ws.state;
			return ws;
		};
		me.StaticState=function(name,params,parent){
			if(!parent)parent=me.DefaultLayout;
			var ws=me.CreateWrappedState(name,params,parent,true);
			ws.state.controller=null;
			ws.state.controllerAs=null;
			me.tmpStates[ws.state.name]=ws.state;
			return ws;
		};
		me.MultiViewsState=function(name,params,parent){
			var isHtm=isViewRef(name);
			if(!parent)parent=me.DefaultLayout;
			var ws=me.CreateWrappedState(name,params,parent,isHtm);
			ws.state.name="/MultiViewsLayout_"+ws.state.name.substring(1,ws.state.name.length);
			me.tmpStates[ws.state.name]=ws.state;
		    ws.state['abstract'] = true;
		    name=(name.startWith('/')?'':me.ns)+name;
			name=isHtm?name.substring(0,name.indexOf('.')):name;
			if(name.indexOf('?')>0) name=name.substring(0,name.indexOf('?'));
			var childState={
					name:name,
					url:name+(params?'?'+params:""),
					params:ws.state.params,
					reloadOnSearch:false,
					parent:ws.state.name,
					views:{}
			};
			ws.state.params=null;
			ws.state.url=ws.state.name;
			me.tmpStates[name]=childState;
			return {
				state:childState,
				BranchView:function(name,params){
					var isHtm=isViewRef(name);
					var wrappedview=me.CreateWrappedState(name,params,undefined,isHtm,this.state);
					wrappedview.OwnerState=this.state;
					wrappedview.OwnerState.views[wrappedview.state.name]=wrappedview.state;
					return wrappedview;
				},
				Layout: ws
			};
		};
        me.RawState = function(rawState) {
            var ws = me.CreateWrappedState(rawState);
            me.tmpStates[ws.state.name]=rawState;
            return ws;
        };
		me.RegisterImplicitControllers=function(){
		    angular.forEach(me.tmpStates, function(st, name) {
		        if(st.controller==ProjectBaseGlobalVar.AutoControllerMarker){
			    	if(st.resolve!=null&&st.resolve.serverVm)
				    	st.controller=['pb','serverVm','$scope',function(pb, serverVm, $scope) {
				            pb.Super(this, serverVm, $scope);
				        }];		
			    	else
			    		st.controller=['pb','$scope',function(pb,  $scope) {
				            pb.Super(this, {}, $scope);
				        }];	
				}
		        if(st.views){
		        	angular.forEach(st.views, function(viewconfig, name) {
		        		if(viewconfig.controller==ProjectBaseGlobalVar.AutoControllerMarker){
		        			if(st.resolve!=null&&st.resolve['serverVm_'+name])
			        			viewconfig.controller=['pb','serverVm_'+name,'$scope',function(pb, serverVm, $scope) {
						            pb.Super(this, serverVm, $scope);
						        }];
		        			else
		        				viewconfig.controller=['pb','$scope',function(pb,  $scope) {
						            pb.Super(this, {}, $scope);
						        }];
		        		}
				    	var fn=viewconfig.controller;
						var f=angular.isArray(fn)?fn[fn.length-1]:fn;
						if(f){
							name=st.name+".view_"+name;
							f.prototype._DefinedBy=f.prototype._DefinedBy||name;//name controller after view name
							f.prototype._NavBackRefresh=st.Tmp_NavBackRefresh;
							delete st.Tmp_NavBackRefresh;
						}
		        	});
		        }else{
			    	var fn=st.controller;
					var f=angular.isArray(fn)?fn[fn.length-1]:fn;
					if(f){
						f.prototype._DefinedBy=f.prototype._DefinedBy||st.name;//name controller after state name
						f.prototype._NavBackRefresh=st.Tmp_NavBackRefresh;
						delete st.Tmp_NavBackRefresh;
					}
		        }
		    });
		};
		me.RegisterStates=function(stateProvider){
		    angular.forEach(me.tmpStates, function(st, name) {
		        stateProvider.state(st);
		    });
			me.tmpStates={};
		};
};
//end-----------PB_Global_Definer-----------------------------------------------
//begin-------------pb service:centered on AjaxSubmit,provide all common utilities about it---------------------------------------------------------------
function pbFn(validationManager,$http,$state,$parse,$window,$injector,$timeout,$q,pbui,PBPlugIn){
	var $=function(ele){
		if(angular.isString(ele)){
			var found=$window.document.getElementById(ele);
			if(found)return angular.element(found);
			found=$window.document.getElementsByName(ele);
			return angular.element(found);
		}
		return angular.element(ele);
	};
    var parentForm=function(ele){
        var f = ele;
        while(f.length!=0 && f.prop('tagName')!='FORM' && f.prop('tagName')!='NG-FORM') {
            f = f.parent();
        }
		return f;
	};
    var AjaxSubmit = function(control, qsJson, attrObj, funcExecuteResult, argsForFunc, overrideSuper) {
        var confirmText, confirmParams = '';

        if (control == null) control = undefined;
        if (control) {
            control = $(control);
            if (control.length > 1) control = control.eq(0);
            if (control.length < 1) {
                $window.alert("param control's indicated no control");
                return;
            }
        }

        if (typeof($(control).attr('ajax-confirm')) != 'undefined'
            || typeof($(control).attr('ajax-confirm-single')) != 'undefined'
                || typeof($(control).attr('ajax-confirm-multi')) != 'undefined') {
            confirmText = $(control).attr('ajax-confirm');
            if (typeof($(control).attr('ajax-confirm-single')) != 'undefined') {
                confirmParams = $(control).attr('ajax-confirm-single');
            } else if (typeof($(control).attr('ajax-confirm-multi')) != 'undefined') {
                var parseFunc = $parse($(control).attr('ajax-confirm-multi'));
                var a = parseFunc($(control).scope());
                confirmParams = HasSelectedRows(a);
            }
            ConfirmSubmit(confirmText, confirmParams).then(function(confirmed) {
                if (confirmed != true) return;
                return AjaxSubmitMain(control, qsJson, attrObj, funcExecuteResult, argsForFunc, overrideSuper);
            });
        } else {
            return AjaxSubmitMain(control, qsJson, attrObj, funcExecuteResult, argsForFunc, overrideSuper);
        }
    };
	var AjaxSubmitMain=function (control, qsJson, attrObj, funcExecuteResult, argsForFunc,overrideSuper) {	
	    var form,url,processingAreaId,bindTargetId,confirmText,method,valgroup,ajaxData,ajaxBodyData;
	    var bindTargetControllerInstance;
	    var noform=false;
	    var autoBind=true;//默认自动绑定返回的数据
	    
        if(attrObj) {//attrObj优先于controller的属性
            if (attrObj['ajax-data']) {
                ajaxData = attrObj['ajax-data'];
            }
            if (attrObj['ajax-form']) {
                form = $(attrObj['ajax-form']);
            }
            if (attrObj['ajax-url']) {
                url = attrObj['ajax-url'];
            }
            if (attrObj['ajax-bind']) {
                bindTargetId = attrObj['ajax-bind'];
            }
            if (attrObj['ajax-processing']) {
                processingAreaId = attrObj['ajax-processing'];
            }
            if (attrObj['ajax-method']) {
                method = attrObj['ajax-method'];
            }
            if (attrObj['ajax-valgroup']) {
                valgroup = attrObj['ajax-valgroup'];
            }
        }
	    if (typeof (form) == 'undefined') {
	        if (typeof (control) == 'undefined') {
	        	noform = true; 
	        }
	        else if ($(control).prop('tagName')=='FORM'||$(control).prop('tagName')=='NG-FORM') {
	            form = $(control);
	        } else {
	            var formid = $(control).attr('ajax-form');
	            if (typeof (formid) != 'undefined') {
	                if (formid == '') {
	                	noform = true;
	                } else {
	                    form = $(formid);
	                    if (form.length == 0) {
	                    	$window.alert('form not found,id=' + formid);
	                        return;
	                    }
	                }
	            } else {
	                form = parentForm($(control));
	            }
	        }
	    }
	
	    if (typeof (url) == 'undefined' || url == '') url = $(control).attr('ajax-url')||$(form).attr('ajax-url');
	    if (typeof (url) == 'undefined' || url == '') {
	        return $window.alert('need value for ajax-url!');
	    }
	    if (typeof (processingAreaId) == 'undefined') processingAreaId = $(control).attr('ajax-processing')||$(form).attr('id');
	    if (typeof (method) == 'undefined') method = $(control).attr('ajax-method')||$(form).attr('ajax-method')||'POST';
	    if (typeof (valgroup) == 'undefined') valgroup = $(control).attr('ajax-valgroup')||$(form).attr('ajax-valgroup');
	   	if (typeof (bindTargetId)== 'undefined') bindTargetId=$(control).attr("ajax-bind")||$(form).attr("ajax-bind");
	    //使用元素指定ajax属性时默认ajax-bind为当前元素，不使用元素而直接指定attrObj,attrObj默认auto-bind=nobind
	    if (typeof (bindTargetId)== 'undefined' && attrObj && !attrObj['ajax-bind']) bindTargetId= 'nobind';
	        
	   	if (bindTargetId== 'nobind') {
	   		autoBind=false;
	   	}else if (bindTargetId== 'parent') {
	   	    bindTargetControllerInstance = $(control).parent().controller();
	   	    if(bindTargetControllerInstance==null) bindTargetControllerInstance=$(form).controller();
	   		if(bindTargetControllerInstance==null) {
	   		    $window.alert('ajax-bind=parent targets no controller!');
	   		    return;
	   		}
	   	}else if (typeof (bindTargetId)== 'undefined' || bindTargetId=='') {
	   	    bindTargetControllerInstance = $(control).controller();
	   	    if(bindTargetControllerInstance==null) bindTargetControllerInstance=$(form).controller();
	   		if(bindTargetControllerInstance==null) {
	   		    $window.alert('an default ajax-bind targets no controller!');
	   		    return;
	   		}
	   	}

	   	if(autoBind && bindTargetControllerInstance==null){
		   	if(typeof (processingAreaId) == 'undefined') processingAreaId=bindTargetId;
		   	if(typeof($(bindTargetId).attr('ui-view'))!='undefined'){
		   		bindTargetControllerInstance=$(bindTargetId).children().eq(0).controller();
		   		if(bindTargetControllerInstance==null){
		   			$window.alert('bindTargetId:'+bindTargetId+' targets a ui-view which has not a controller!');
		   			return;
		   		}
		   	}else{
		   		$window.alert('bindTargetId:'+bindTargetId+' should target an ui-view !');
	   			return;
		   	}
	   	}
	    var formData;
        if($(form).length>0){
			if(!ValidateOnSubmit(form,valgroup))return;
			formData=SerializeAngularForm(form);
	    }
	    ajaxBodyData=MergeQS(formData,ajaxData);

	    url=PrefixUrl(url);
        var urlfull = url.split('?');
	    url = urlfull[0] + '?' +ProjectBaseGlobalVar.KeyForViewModelOnly+'=true'+(urlfull[1]?'&'+urlfull[1]:'') ;
		pbui.PutProcessing(processingAreaId);
		var promise= $http({method: method, url: url,data:ajaxBodyData,params:qsJson})
		    .success(function(responseData, status, headers, config) {
		    	SetResultMarker(responseData);
		    	if(autoBind && responseData.IsVM){
		    		var bindData=responseData.data.ViewModel;
		    		bindTargetControllerInstance.vm=bindData;
		    	}
		    	if(responseData.isRcResult==true){
			    	if(!funcExecuteResult) {
			    	    var controller = $(control).controller();
			    	    if (controller && controller.ExecuteResult) {//自定义的结果处理优先
			    	        controller.ExecuteResult($(control).attr('id'), responseData);
			    	    }else {
			    	    	PBPlugIn.ExecuteResult(responseData);
			    	    }
			    	    if (responseData.IsNoop && controller && controller.ExecuteNoopResult) {//没有服务器指令则查找客户端定义的callback
			    	        controller.ExecuteNoopResult($(control).attr('id'));
			    	    }
			    	}else if(overrideSuper!=true){
			    		PBPlugIn.ExecuteResult(responseData);
			    	}
		    	}else if(responseData.isRcResult==false){
		    		PBPlugIn.ExecuteErrorResult(responseData);
		    	}
		    	pbui.PutProcessing(false);
		    })
		    .error(function(responseData, status, headers, config,statusText) {
		    	PBPlugIn.ExecuteHttpError(responseData, status, headers, config);
		    	pbui.PutProcessing(false);
		    });
		if(!funcExecuteResult) return promise;
		return promise.then(function(response){
			if(response.data.isRcResult==true)
				return funcExecuteResult(response.data,argsForFunc);
		    return response.data;
		});
			
	};
	var CallAction=function (url, params, funcExecuteResult, argsForFunc,overrideSuper) {
		url=PrefixUrl(url);
        var urlfull = url.split('?');
	    url = urlfull[0] + '?' +ProjectBaseGlobalVar.KeyForViewModelOnly+'=true'+(urlfull[1]?'&'+urlfull[1]:'') ;
		var promise= $http({method: "POST", url: url,data:params})
			.success(function(responseData, status, headers, config) {
				SetResultMarker(responseData);
				if(responseData.isRcResult==true&&overrideSuper!=true){
					PBPlugIn.ExecuteResult(responseData);
				}else if(responseData.isRcResult==false){
					PBPlugIn.ExecuteErrorResult(responseData);
				}
		    })
		    .error(function(responseData, status, headers, config) {
		    	PBPlugIn.ExecuteHttpError(responseData, status, headers, config);
		    });
		if(!funcExecuteResult) return promise;
		return promise.then(function(response){
			if(response.data.isRcResult==true)
				return funcExecuteResult(response.data,argsForFunc);
		});
			
	};
    //begin---------functions parts used by ajaxsubmit,can be used as plugin points by modifing their code-------------------------------------------------------------------
    var SerializeAngularForm = function(jqForm) {
        var r20 = /%20/g,
	    rbracket = /\[\]$/,
	    rCRLF = /\r?\n/g,
	    rinput = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,
	    rselectTextarea = /^(?:select|textarea)/i;
        
        var inputs  =jqForm.find('input');
        var ipt2=jqForm.find('textarea');
        var ipt3=jqForm.find('select');
        var resultArray = [];
        var f = function(control, index1) {
            if (control.tagName == 'INPUT' || control.tagName == 'SELECT' || control.tagName == 'TEXTAREA') {
                if (control.name && control.name.substring(0,1)!="_" && !control.disabled &&
                    (control.checked || rselectTextarea.test(control.nodeName) || rinput.test(control.type))) {
                    var name = angular.element(control).attr('name');
                    var val = angular.element(control).val();
                    if(angular.isArray(val)) {
                        angular.forEach(val, function (value,index2) {
                            value = value.replace(rCRLF, "\r\n");
                            resultArray[resultArray.length] = $window.encodeURIComponent(name) + "=" + $window.encodeURIComponent(value);
                        });
                    }else {
                        val = val.replace(rCRLF, "\r\n");
                        if (control.type == 'radio' && val == 'on') val = '';
                        resultArray[resultArray.length] = $window.encodeURIComponent(name) + "=" + $window.encodeURIComponent(val);
                    }
                }
            }
        };

        angular.forEach(inputs, f);
        angular.forEach(ipt2, f);
        angular.forEach(ipt3, f);
        var result=resultArray.join("&").replace(r20, "+");
        return result.replace(/number%3A/g, '');
    };
    var ValidateOnSubmit = function(formElement,valgrouptobe) {
        if(valgrouptobe=='none') return true;
		var formCtrl=$(formElement).controller('form');
		if(formCtrl.$invalid) {
			var ok=true;
			for(var valtype in formCtrl.$error){
				var ngModelCtrls=formCtrl.$error[valtype];
				for(var i=0;i<ngModelCtrls.length;i++){
					var excluded=ngModelCtrls[i].$name.substring(0,1)==ProjectBaseGlobalVar.UnSubmitNameMarker;
					if(excluded)continue;
				    var valgroups = ngModelCtrls[i].pb_valgroup;
				    if (valgroups) {
                        try {
                            valgroups = angular.fromJson(valgroups);
                            var valtypes =','+valgroups[valgrouptobe]+',';
                            if(valtypes.indexOf(valtype)>=0) {
                                ok=false;
						        break; 
                            } else {
                                ok = true;
                            }
                        } catch (e) {
                            $log.error("pb-valgroup's value is invalid, should be a json string:" + valgroups);
                        }
                    } else {
				        ok=false;
				        break; 
				    }
				};
				if(!ok)break;
			};
			if(!ok){
				validationManager.validateForm(formElement);
				return false;
			}
		}
		return true;
    };
    var SetPristine = function(frmCtrl,groupName) {
    	angular.forEach(frmCtrl.PB_Group[groupName],function(ele,eleName){
    		frmCtrl[eleName].$setPristine();
    	});
    };
    var Validate = function(frmCtrl,groupName) {
    	var isvalid=true;
    	angular.forEach(frmCtrl.PB_Group[groupName],function(ele,eleName){
    		var ctrl=ele.controller("ngModel");
    		if(ctrl.$invalid){
    			isvalid=false;
    			validationManager.validateElement(ctrl,ele,{disabled:false,forceValidation:true});
    		}
    	});
    	return isvalid;
    };

    var HasSelectedRows = function(array) { //angular对绑定的多选列表中未选项保留绑定值为false，数组长度不变。
            var cnt = array.length;
            for (var i = 0; i < cnt; i++) {
                if (array[i]!=null && array[i] != false) return true;
            }
            return false;
        };
	var ConfirmSubmit=function(confirmMsgKey, confirmIdTextOrHasRows) {
		if(!confirmMsgKey)confirmMsgKey='ConfirmDelete';
	    if(angular.isString(confirmIdTextOrHasRows))
	        return pbui.Confirm(confirmMsgKey,confirmIdTextOrHasRows);
		else if(!confirmIdTextOrHasRows){
			return pbui.Alert('NoRowSelected').then(function(){return false;});
	    }else{
	        return pbui.Confirm('ConfirmDeleteMulti');
	    }
	};
    var MergeQS = function(qs1, qs2) {//用第二个参数覆盖第一个
        if (typeof(qs1) == 'undefined') {
            if (typeof(qs2) == 'undefined') {
                return '';
            }
            return qs2;
        } else {
            if (typeof(qs2) == 'undefined') {
                return qs1;
            }
        }

        if (qs1 == '' || qs2 == '') return qs1 + qs2;

        var pairs1 = qs1.split('&');
        var pairs2 = qs2.split('&');
        var newqs = '';
        var qs1new = '';
        var qs2new = '';
        for (var i = 0; i < pairs2.length; i++) {
            var a = pairs2[i].split('=');
            var found = false;
            for (var j = 0; j < pairs1.length; j++) {
                if (pairs1[j].split('=')[0].toLowerCase() == a[0].toLowerCase()) {
                    pairs1[j] = pairs2[i];
                    found = true;
                    break;
                }
            }
            if (!found && pairs2[i] != '') qs2new = qs2new + pairs2[i] + '&';
        }
        for (var i = 0; i < pairs1.length; i++) {
            qs1new = qs1new + pairs1[i] + '&';
        }
        if (qs1new.endWith('&')) {
            newqs = qs1new;
        }
        if (qs2new.endWith('&')) {
            newqs = newqs + qs2new;
        }
        if (newqs.endWith('&')) {
            newqs = newqs.substr(0, newqs.length - 1);
        }
        return newqs;
    };
    //begin---------AjaxNav-------------------------------------------------------------------
    var AjaxNavRegister = function (transObj,onleaving) {
    		//console.log('reg:'+fromState.name+'--'+toState.name+'--'+(onleaving?'leave':'enter'));
    		var toState=transObj.to();
    		var toParams=transObj.params();
    		var fromState=transObj.from();
    		var fromParams=transObj.params('from');
    	
            if(fromState==toState||toState.name==AjaxNavRoot.PrevState) return;
            //进入已记录路径中任一个,栈中已经被AjaxNav弹出后面的因而最后一个就是当前进入的，不需要再重复记录
	        if(AjaxNavData.Stack.length>0 && toState==AjaxNavData.Stack[AjaxNavData.Stack.length-1].State)return;
	        
            var tonav,map={};
            if (toParams && toParams['ajax-nav']) {
                tonav = angular.lowercase(toParams['ajax-nav']);
            }
            var tonavback= angular.lowercase(toParams['ajax-nav-back']);
            if(onleaving){//from是当前所在，根据去向决定是否需要将数据保存
                if (tonav == 'forward') { 
                    if(AjaxNavData.Stack.length==1&&AjaxNavData.Stack[0].State==null) {
                        AjaxNavData.Stack[0].State = fromState;
                    }
                    var lastnav = AjaxNavData.Stack[AjaxNavData.Stack.length - 1];
                    if (fromState == lastnav.State) {//should always be true
                    	var st = lastnav.State;
                        do {
                        	if(st.views){
                        		angular.forEach(st.views,function(config,viewname){
                        			var name=st.name+".views_"+viewname;
                        			if(AjaxNavData.ControllerDataMapForCache[name])map[name]=AjaxNavData.ControllerDataMapForCache[name].vm;
                        		});
                        	}else{
                        		if(AjaxNavData.ControllerDataMapForCache[st.name])map[st.name] = AjaxNavData.ControllerDataMapForCache[st.name].vm;
                        	}
                            st = $state.get(st.parent);
                        } while (!st.name.startWith('/Shared/'));
                    	lastnav.StateData=map;
                    	//console.log('remember:'+fromState.name);
                    }
                }
                AjaxNavData.ControllerDataMapForCache={};
                //console.log('leaving:'+fromState.name);
            }else { //to是当前所在，要记录上一
            	if(tonavback)return;//从路径上返回时不需要注册
                tonav=tonav||'root';
                var navData = { PrevState: fromState, PrevStateParams: fromParams, State: toState,StateParams: toParams, StateData: null};
                if (tonav == 'root') {
                    AjaxNavData.Stack = [AjaxNavRoot];
                    AjaxNavData.Stack[0].State = toState;
                    AjaxNavData.Stack[0].StateParams = toParams;
                } else if (tonav == 'forward') {
                    AjaxNavData.Stack.push(navData);
                } else {
                    alert('nav has invalid value!');
                }
                //console.log('entered:'+toState.name+'--'+AjaxNavData.Stack.length);
            } 
    };
    var AjaxNavRefresh = function () {
    	var state=AjaxNavData.Stack[AjaxNavData.Stack.length-1].State;
    	var stateParams=AjaxNavData.Stack[AjaxNavData.Stack.length-1].StateParams;
    	$state.go(state,stateParams,{inherit:false,reload:true});
    };
    var AjaxNavTo = function (state,stateParams) {
        while(AjaxNavData.Stack.length>0 && state!=AjaxNavData.Stack[AjaxNavData.Stack.length-1].State){
        	AjaxNavData.Stack.pop();
        }
        $state.go(state,stateParams,{inherit:false,reload:true});
    };
    var  AjaxNavBack = function () {
        var last =AjaxNavData.Stack.pop();
        var params;
        if (last.PrevStateParams){
        	params=angular.copy(last.PrevStateParams);
        	params['ajax-nav-back'] = 'back:' + $state.current.name;
        }
        $state.transitionTo(last.PrevState, params, {
            reload: false,
            inherit: false,
            notify: true
        });
    }; 
    var AjaxNavGetBackSrc = function (stateName,stateParams) {
        var source = stateParams ? stateParams['ajax-nav-back'] : undefined;
        var isBack = source ? source.startWith('back:') : false;
        if(source&&isBack) {
            var t = CreateRcResult();
            t.data.ViewModel = AjaxNavData.Stack[AjaxNavData.Stack.length - 1].StateData[stateName];
            return {
                IsBack: isBack,
                BackFrom: isBack ? source.substring(5, source.length) : undefined,
                LastData:t 
            };
        }else {
            return false;
        }
    };
    var CreateRcResult = function(result,command,data) {
        if(!command&&!data) {
            command = ProjectBaseGlobalVar.Command_AjaxNavVM;
            data = { ViewModel: null };
        }
        return {
            result: result,
            command: command,
            data: data
        };
    };
    var Super = function(contoller,serverVm,scope) {
    	contoller.vm = serverVm;
    	contoller.tmp={};
    	
    	//初始化controller时记住对数据的引用，以state+view名为键
    	var ctrlname=contoller.__proto__._DefinedBy;
    	if(!ctrlname.startWith('/Shared/')){
    		var dataOfController =contoller;// {vm:contoller.vm,tmp:contoller.tmp};
        	AjaxNavData.ControllerDataMapForCache[ctrlname] = dataOfController;
        	for(var prop in contoller.__proto__._UsedBy){
        		AjaxNavData.ControllerDataMapForCache[prop] = dataOfController;
        	};
        	//console.log('cache:'+ctrlname);
        }

    	var fn=contoller.__proto__._NavBackRefresh;
    	if(fn){
			scope.$on('$viewContentLoaded', function(event){//返回会自动callback执行动作以刷新页面数据
				if(serverVm._FromResult&&serverVm._FromResult.command==ProjectBaseGlobalVar.Command_AjaxNavVM){
					$timeout(function(){$injector.invoke(fn);});
				}
			});
    	}
        return contoller;
    };

    //end---------AjaxNav-------------------------------------------------------------------
    return {
            AjaxSubmit: AjaxSubmit,
            CallAction: CallAction,
            ExecuteResult: PBPlugIn.ExecuteResult,
            AjaxNavRegister: AjaxNavRegister,
            AjaxNavGetBackSrc:AjaxNavGetBackSrc,
            AjaxNavBack:AjaxNavBack,
            AjaxNavRefresh:AjaxNavRefresh,
            AjaxNavTo:AjaxNavTo,
            CreateRcResult:CreateRcResult,
            AjaxNavData:AjaxNavData,
            ParentForm:parentForm,
            ElementById:$,
            Super:Super,
            ValidateUtil:{
            	SetPristine:SetPristine,
            	Validate:Validate
            }
   };
};
pbFn.$inject = ['validationManager','$http','$state','$parse','$window','$injector','$timeout','$q','pbui','PBPlugIn'];
var pbm=angular.module('projectbase');
pbm.provider('pb',function(){
	var me=this;
	me.$get = pbFn;
});
//--------------------------project base extension point---------------------------------
pbm.factory('PBPlugIn', ['PBPlugIn_Default',function (PBPlugIn_Default) {
    return PBPlugIn_Default;
} ]); 
pbm.factory('PBPlugIn_Default', ['$window','$translate','$state','pbui',function ($window,$translate,$state,pbui) {
    var RefListContainItem = function (list, item) {
        return false;
    };
    var GetMoreOptionsDefault = function () {
        return {"全部":-1};
    };
	var ExecuteResult=function(rcJsonResult,bindCtrl){
		if(bindCtrl&&rcJsonResult.IsVM){
			bindCtrl.vm=rcJsonResult.data.ViewModel;
    	}else if(rcJsonResult.IsMsg){
			this.ShowResultMessage(rcJsonResult.data);//use 'this' to ensuring using the runtime object's function,that is ,the extended object from default
		}else if(rcJsonResult.IsRedirect) {
		    $state.go(rcJsonResult.data);
		}else if(rcJsonResult.IsAppPage) {
			var url=rcJsonResult.data;
		    $window.location=ProjectBaseGlobalVar.UrlMappingPrefix+url;
		}
	};
	var ExecuteErrorResult=function(rcJsonResult){
		if(rcJsonResult.IsMsg){
			this.ShowErrorResultMessage(rcJsonResult.data);
		}else if(rcJsonResult.IsRedirect) {
			 $state.go(rcJsonResult.data);
		}else if(rcJsonResult.IsAppPage) {
			var url=rcJsonResult.data;
		    $window.location=ProjectBaseGlobalVar.UrlMappingPrefix+url;
		}else{
			pbui.ShowCommand(rcJsonResult.command,rcJsonResult.data);
		}
	};
	var ExecuteHttpError=function(data, status, headers, config){
		pbui.Alert(status);
	};	
	var ShowResultMessage=function(msgKey){
		$translate(msgKey).then(function (msg) {
		    $window.alert(msg);
		},function (notTranslatedMsgKey) {
		    $window.alert(notTranslatedMsgKey);
		});
	};
	var ShowErrorResultMessage=function(msgKey){
		pbui.Alert(msgKey);
	};
    return {
        RefListContainItem: RefListContainItem,
        GetMoreOptionsDefault:GetMoreOptionsDefault,
        ExecuteResult:ExecuteResult,
        ExecuteErrorResult:ExecuteErrorResult,
        ExecuteHttpError:ExecuteHttpError,
        ShowResultMessage:ShowResultMessage,
        ShowErrorResultMessage:ShowErrorResultMessage
    };
} ]); 
//---------------------------------------------------------------------------------------------

//pbm.factory('ResponseCheckerForUiRouter', function($q,$window,$location) {
//    return {
//      'response': function(response) {
//        return response;
//      }
//    };
//  });
//begin--------------------------------------------------------------------------------------------
pbm.directive("pbInitvm", function () {//create an vm property for the element's controller instance
	 var directiveDefinitionObject = {
		      priority: 0,
		      restrict: 'A',
		      compile: function (tElement, tAttrs, transclude) {
			    return {
			    	pre:function(scope, element, attrs) {
				    	element.controller().vm = { };
				        if (attrs["pbInitvm"] != "") {
				            angular.copy(angular.fromJson(attrs["pbInitvm"]), element.controller().vm);
				        }
			    	},
			    	post: function postLink(scope, iElement, iAttrs, controller) {
			    		
			    	}
			    };
		      }
	 };
    return directiveDefinitionObject;
});
//begin--------------------------------------------------------------------------------------------
pbm.directive("pbInitData", function () {//create an vm property for the element's controller instance
	 var directiveDefinitionObject = {
		      priority: 0,
		      restrict: 'A',
		      compile: function (tElement, tAttrs, transclude) {
			    return {
			    	pre:function(scope, element, attrs) {
				        if (attrs["pbInitData"] != "" && attrs["pbInitVar"]) {
				        	scope[attrs["pbInitVar"]]._initData=angular.fromJson(attrs["pbInitData"]);
				        }
			    	},
			    	post: function postLink(scope, iElement, iAttrs, controller) {
			    		
			    	}
			    };
		      }
	 };
    return directiveDefinitionObject;
});
//begin-----------------------缺省情况下ajax-url属性将form,a,button元素自动绑定点击事件为ajax提交---------------------------------------------------------------------
pbm.directive("ajaxUrl", ['pb','App_FuncTree',function factory(pb,App_FuncTree) {
	    var directiveDefinitionObject = {
	      priority: 0,
	      restrict: 'A',
	      compile: function compile(tElement, tAttrs, transclude) {
	          return {
	              pre: function preLink(scope, iElement, iAttrs, controller) {
	              },
	              post: function postLink(scope, iElement, iAttrs, controller) {
	              		if(iAttrs['pbFunc']||iElement.prop('tagName')=='FORM') {
	              		}else{
	                        var funccode=iAttrs['ajaxUrl'];
	              		    if(funccode=='') {
	              		        var f = pb.ParentForm(iElement);
	              		       funccode=f.attr('ajax-url');
	              		    } 
	              		    if(funccode && funccode!='') {
	              		        var pos0 = funccode.lastIndexOf('?');
	              		        if (pos0 > 0) funccode = funccode.substring(0, pos0);
	              		        var keys = funccode.split('/');
	              		        var l = keys.length;
	              		        funccode = keys[l - 2] + '.' + keys[l - 1];
	              		        //iElement.attr('pb-func',funccode);can't be this way
	              		        App_FuncTree.SetElementStatusByFunc(iElement, funccode);
	              		    }
	              		}
              	
	              		
	                  if (iAttrs["ngClick"] || iAttrs["ngSubmit"]) return;
	                  var submitFunc = function() {
	                      pb.AjaxSubmit(iElement);
	                  };
	                  if (iElement.prop('tagName')=='FORM') {
	                      iElement.bind('submit',submitFunc);
	                  } else if (iElement.prop('tagName')=='BUTTON'||iElement.prop('tagName')=='A'){
	                      iElement.bind('click',submitFunc);
	                  }
	              }
	          };
	      }
	    };
	    return directiveDefinitionObject;
}]);
 //begin-------func onto element-------------------------------------------------------------------------------------
pbm.directive("pbFunc", ['App_FuncTree',function (App_FuncTree) {
    var directiveDefinitionObject = {
        priority: 100,//after ajax-url
        restrict: 'A',
        link: function (scope, iElement, iAttrs) {
        	App_FuncTree.SetElementStatusByFunc(iElement,iAttrs['pbFunc']);
        }
    };
    return directiveDefinitionObject;
} ]); 
pbm.directive("uiSref", ['pb','App_FuncTree',function factory(pb,App_FuncTree) {
	    var directiveDefinitionObject = {
	      priority: 0,
	      restrict: 'A',
	      compile: function compile(tElement, tAttrs, transclude) {
	          return {
	              pre: function preLink(scope, iElement, iAttrs, controller) {
	              },
	              post: function postLink(scope, iElement, iAttrs, controller) {
	                  if(iAttrs['pbFunc']) {
                      }else {
	                      var funccode = iAttrs['uiSref'];
	                      if (funccode && funccode != '') {
	                          var pos0 = funccode.lastIndexOf('(');
	                          if (pos0 > 0) funccode = funccode.substring(0, pos0);
	                          var keys = funccode.split('/');
	                          var l = keys.length;
	                          funccode = keys[l - 2] + '.' + keys[l - 1];
	                          App_FuncTree.SetElementStatusByFunc(iElement, funccode);
	                      }
	                  }
	              }
	          };
	      }
	    };
	    return directiveDefinitionObject;
}]);
//end-------func onto element---------------------------------------------------------------
pbm.directive("pbGroup", ['pb', function (pb) {
    var directiveDefinitionObject = {
        priority: 0,
        restrict: "A",
        require: ["^form","ngModel"],
        link: function (scope, ele, attrs, ctrls) {
        	if(!attrs.name)return;
        	var formController=ctrls[0],ngModelController=ctrls[1];
        	formController.PB_Group=formController.PB_Group||{};
        	formController.PB_Group[attrs["pbGroup"]]=formController.PB_Group[attrs["pbGroup"]]||{};
        	formController.PB_Group[attrs["pbGroup"]][attrs.name]=ele;
        }
    };
    return directiveDefinitionObject;
}]);

}(String, angular));//end pack
