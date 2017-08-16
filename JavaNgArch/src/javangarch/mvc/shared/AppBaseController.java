package javangarch.mvc.shared;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;



import projectbase.domain.ICommonBD;
import projectbase.mvc.AjaxHelperExtension;
import projectbase.mvc.AuthFailureException;
import projectbase.mvc.BaseController;
import projectbase.mvc.DisplayExtension;
import projectbase.mvc.ExcludeController;
import projectbase.mvc.WebConfigurationManager;
import projectbase.mvc.result.ActionResult;
import projectbase.mvc.result.HttpStatusCodeResult;
import projectbase.mvc.result.RichClientJsonResult;
import javangarch.domain.bdinterface.IAdminBD;

import javangarch.domain.domainmodel.gn.Corp;
import javangarch.domain.domainmodel.gn.User;
import javangarch.mvc.home.LoginInfoViewModel;

	@ExcludeController
    public class AppBaseController extends BaseController
    {
        @Resource public IAdminBD AdminBD;
        public static String DefaultPassword = "c4ca4238a0b923820dcc509a6f75849b";//默认密码为1
        public static final String AdminCode = "Admin"; //用于管理部门和用户的固定代码
        private static List<String> _relatedFileNames; 

 	 
       protected int GetLoginCorpId()
        {
            Corp corp = ((LoginInfoViewModel)getRequest().getSession().getAttribute("LoginInfo")).getLoginCorp();
            return corp.getId();
        }
       protected Corp GetLoginCorp()
       {
    	   Corp corp = ((LoginInfoViewModel) getRequest().getSession().getAttribute("LoginInfo")).getLoginCorp();
           AdminBD.RefreshCorp(corp);
           return corp;
       }
       protected User GetLoginUser()
       {
           User user = ((LoginInfoViewModel) getRequest().getSession().getAttribute("LoginInfo")).getLoginUser();
           AdminBD.RefreshUser(user);
           return user;
       }
 
        protected boolean CanAccess(String funcCode)
        {
        	LoginInfoViewModel logininfo = ((LoginInfoViewModel)getRequest().getSession().getAttribute("LoginInfo"));
            return logininfo.CanAccess(funcCode);
        }

  
        protected void CheckFuncAuth(String funcCode) 
        {
        	LoginInfoViewModel loginInfo = ((LoginInfoViewModel)getRequest().getSession().getAttribute("LoginInfo"));
            if (loginInfo.CanAccess(funcCode)) 
            	return;
            throw new AuthFailureException();
        }
  
        //登录用户是否为admin
        public boolean IsAdmin()
        {
            return IsAdmin(GetLoginUser());
        }

        //给定用户是否为admin
        public boolean IsAdmin(User user)
        {
            return user!=null && !user.IsTransient() && StringUtils.equalsIgnoreCase(user.getCode(),AdminCode);
        }

        protected ActionResult FileNotFound()
        {
         	return RcJsonError( "FileNotFound", "FileNotFound");
        }
       	@Override
        protected ActionResult OnException(HttpServletRequest req,
    			HttpServletResponse response, Object handler, Exception ex)
        {
            if (ex instanceof AuthFailureException)
            {
                return AuthFailure();
            }
            return super.OnException(req,response,handler,ex);
        }
 

    }

