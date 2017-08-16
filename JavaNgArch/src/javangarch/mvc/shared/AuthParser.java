

package javangarch.mvc.shared;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import javangarch.domain.bdinterface.IAdminBD;
import javangarch.domain.domainmodel.gn.Func;
import javangarch.domain.domainmodel.gn.User;
import javangarch.mvc.home.LoginInfoViewModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import projectbase.domain.customcollection.IDomainList;
import projectbase.mvc.Auth;
import projectbase.mvc.BaseAuthParser;
import projectbase.mvc.result.ActionResult;
import projectbase.mvc.result.AjaxScriptResult;
import projectbase.mvc.result.ClientScriptResult;
import projectbase.mvc.result.RichClientJsonResult;
import projectbase.practice.serviceLocation.ServiceLocator;
import projectbase.utils.Util;

    public class AuthParser extends BaseAuthParser
    {
    	private Auth auth;
        public Auth getAuth() {
			return auth;
		}
        
        
        @Override
		public boolean ShouldParseControllerDefault() {
			return true;
		}


		@Override
		public Annotation getAnnotaion() {
			return auth;
		}

		@Override
		public void setAnnotaion(Annotation an) {
			auth=(Auth)an;
		}
		@Override
		public Class<? extends Annotation> getAnnotaionClass() {
			return Auth.class;
		}
		@Override
		protected boolean IsAuthEnabled(){
			return auth.AuthEnabled();
		}
        public AuthParser(){ 
        	super();
        }
        
        public AuthParser(Auth auth){ 
        	this.auth=auth;
        }
        @Override
        protected ActionResult CheckLogin(HttpServletRequest request,
				HttpServletResponse response, Object handler,String actioncode)
        {

            if (request.getSession().getAttribute("LoginInfo") == null)
            {

              // return new RichClientJsonResult(false,RichClientJsonResult.Command_Redirect,"/home/Home/ShowLogin");

                //for test conveniece,auto login
              AutoLoginForTest(request,response,handler,actioncode);


            }
 
            return null;
        }

        @Override
        protected boolean CanAccess(HttpServletRequest request,
				HttpServletResponse response, Object handler,String actioncode)
        {
        	String funcCode=actioncode;
        	if(!StringUtils.isEmpty(auth.value())) funcCode=auth.value();
        	if(!StringUtils.isEmpty(auth.FuncCode())) funcCode=auth.FuncCode();
        	
            if (!Util.FuncMap().containsKey(funcCode)) return true;
            LoginInfoViewModel loginInfo = (LoginInfoViewModel) request.getSession().getAttribute("LoginInfo");
            return loginInfo.CanAccess(funcCode);
        }

        protected void AutoLoginForTest(HttpServletRequest request,
				HttpServletResponse response, Object handler,String actioncode)
        {
        	IAdminBD AdminBD =(IAdminBD)ServiceLocator.getCurrent().GetService(IAdminBD.class);
            User loginUser = AdminBD.GetLoginUser("1","admin","c4ca4238a0b923820dcc509a6f75849b");// AppBaseController.DefaultPassword);
            LoginInfoViewModel loginInfo = new LoginInfoViewModel();
            loginInfo.setLoginUser(loginUser);
            loginInfo.setLoginCorp(loginUser.getDept().getCorp());
            String tmp = loginInfo.getLoginCorp().getName();//make proxy load the real entity
            
            int i = 0;
    		for (Func func : loginInfo.getLoginCorp().getFuncs()) {
    			loginInfo.AddCorpFuncCode(func.getCode());
    		}
    		for (Func func : loginUser.getDept().getFuncs()) {
    			loginInfo.AddDeptFuncCode(func.getCode());
    		}
    		for (Func func : loginUser.getFuncs()) {
    			loginInfo.AddUserFuncCode(func.getCode());
    		}

            request.getSession().setAttribute("LoginInfo",loginInfo);
        }




    }

