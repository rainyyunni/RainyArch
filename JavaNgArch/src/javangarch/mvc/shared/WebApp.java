package javangarch.mvc.shared;

import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javangarch.domain.hibernatemapping.MyInterceptor;
import javangarch.mvc.home.LoginInfoViewModel;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;

import projectbase.data.HibernateSessionModified;
import projectbase.data.UtilQuery;
import projectbase.mvc.AnnotationParserHandlerInterceptor;
import projectbase.mvc.BaseMvcApplication;
import projectbase.sharparch.hibernate.HibernateSession;
import projectbase.utils.ProjectHierarchy;
import projectbase.utils.Util;

    public class WebApp extends BaseMvcApplication
    {
    	public static Logger log=Logger.getLogger(WebApp.class);
    	@Override
		protected void InitProjectHierarchy()
        {
        	Hashtable<String,String> t=new Hashtable<String,String>();
            t.put("ta", "ta_");
            t.put("gn", "gn_");
            ProjectHierarchy.Init("javangarch", t);
        }
		@Override
		protected Class<?> GetIocConfig(){
			return SpringIocConfig.class;
		}
		@Override
		protected Class<?> GetSpringDOScanConfig(){
			return SpringDOScanConfig.class;
		}
		@Override
		protected Class<?> GetSpringVMScanConfig(){
			return SpringVMScanConfig.class;
		}
		@Override
		protected Class<?> GetSpringDictEnumScanConfig(){
			return SpringDictEnumScanConfig.class;
		}
        @Override
		protected void AddAnnotationParserHandlerInterceptors(
				List<AnnotationParserHandlerInterceptor> registry) {
        	registry.add(new AuthParser());
		}
		@Override
        protected void Application_Start(ServletContext servletContext) throws ServletException{
        	
			Properties hCfgProperties = new Properties();
			HibernateSessionModified.setCfgProperties(hCfgProperties);
            HibernateSession.RegisterInterceptor(new MyInterceptor());
            HibernateSessionModified.AfterInit = this::AfterHInit;
            super.Application_Start(servletContext);

		}
        private void AfterHInit()
        {
            //所有登录标志清除
            String sql = "update GN_User set LoginMark=null";
           UtilQuery.StatelessExecuteSql(sql);
        }

    	@Override
    	protected void Session_End(HttpSessionEvent se) {
    		LoginInfoViewModel loginInfo=(LoginInfoViewModel)se.getSession().getAttribute("LoginInfo");
    		if (loginInfo == null ) return;
    		if(loginInfo.getLoginUser() == null)
            {
                Util.AddLog("Session End:loginInfo.LoginUser == null");
                return;
            }
    		String sql = "update GN_User set LoginMark=null where ID=" + loginInfo.getLoginUser().getId();
            UtilQuery.StatelessExecuteSql(sql);

    	}

    }
