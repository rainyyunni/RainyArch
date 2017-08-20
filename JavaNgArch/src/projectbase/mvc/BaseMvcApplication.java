package projectbase.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import projectbase.bd.AutoMapperProfile;
import projectbase.bd.SpringComponentRegistrar;
import projectbase.data.HibernateExceptionTranslator;
import projectbase.data.HibernateSessionModified;
import projectbase.data.hibernatemapbycode.convention.DOClassAnnotationAppender;
import projectbase.data.hibernatemapbycode.convention.NamingConvention;
import projectbase.practice.serviceLocation.*;
import projectbase.sharparch.hibernate.HibernateConfigurationFileCache;
import projectbase.sharparch.hibernate.HibernateInitializer;
import projectbase.sharparch.hibernate.HibernateSession;
import projectbase.sharparch.hibernate.mvc.WebSessionStorage;
import projectbase.utils.ProjectHierarchy;
import projectbase.utils.Util;
/// <summary>
/// initialize and configure all components, namely hibernate,springIoc and mvc4,log4j,autoMapper
/// </summary>

public abstract class BaseMvcApplication implements WebApplicationInitializer,
		HttpSessionListener, ServletRequestListener,Servlet {
	private static ApplicationContext _springContainer;
	public static String UrlMappingPrefix="/do";
	
	//---------subclass must do the following setting
	protected abstract void InitProjectHierarchy();
	protected abstract Class<?> GetSpringDOScanConfig();
	protected abstract Class<?> GetSpringVMScanConfig();
	protected abstract Class<?> GetSpringDictEnumScanConfig();
	protected abstract Class<?> GetIocConfig();
	protected abstract void AddAnnotationParserHandlerInterceptors(List<AnnotationParserHandlerInterceptor> registry);

	//---------subclass can overide the following events and procedures
	protected void Application_Start(ServletContext servletContext)throws ServletException{
		InitProjectHierarchy();
		InitWebApp(servletContext);
		InitSpring(servletContext);
		InitMvc();
		InitializeServiceLocator();
		InitAutoMapper();
		HibernateInitializer.Instance().InitializeHibernateOnce(
				this::InitialiseHibernateSessions);
		InitOther(servletContext);
	}
	protected void InitWebApp(ServletContext servletContext) {
		servletContext.addListener(RequestContextListener.class);
		servletContext.addListener(this.getClass());
		servletContext.addListener(WebSessionStorage.class);
		javax.servlet.FilterRegistration.Dynamic filter=servletContext.addFilter("characterEncodingFilter", CharacterEncodingFilter.class);
		filter.setInitParameter("encoding","UTF-8");
		filter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, ProjectHierarchy.ProjectName);
		final Dynamic errorhandlerservlet =servletContext.addServlet("BaseWebAppErrorHandler", this);
		errorhandlerservlet.setLoadOnStartup(1);
		errorhandlerservlet.addMapping("/Application_Error.do");
	}

	protected void InitSpring(ServletContext servletContext) {
		final AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
		root.setServletContext(servletContext);
		root.register(GetSpringDOScanConfig());
		root.register(GetSpringVMScanConfig());
		root.register(GetSpringDictEnumScanConfig());
		SpringComponentRegistrar.AddComponentsTo(root, GetIocConfig());
		root.refresh();
		
		final Dynamic servlet = servletContext.addServlet(
				ProjectHierarchy.ProjectName, new DispatcherServlet(root));
		servlet.setLoadOnStartup(1);
		servlet.addMapping(UrlMappingPrefix+"/*");
		String location=servletContext.getInitParameter(GlobalConstant.ContextParam_MultipartLocation);
		String maxsize=servletContext.getInitParameter(GlobalConstant.ContextParam_MultipartMaxFileSize);
		long maxfileszie=-1;
		if(!StringUtils.isEmpty(maxsize)) maxfileszie=Long.valueOf(maxsize);
		MultipartConfigElement config=new MultipartConfigElement(location,maxfileszie,-1,0);
		servlet.setMultipartConfig(config);

		_springContainer = root;
	}

	/**
	 * Provides a globally available access to the instance.
	 */
	public static ApplicationContext getSpringContainer() {
		return _springContainer;
	}
	protected void InitMvc()
	 {
	// 服务器端转换类型时错误信息和客户端类型验证
		AngularModelMetadataProvider.setCurrent(new AngularModelMetadataProvider());
		AngularModelMetadataProvider.getCurrent().CreateMetaDatas();
		AngularClientValidationProvider.setCurrent(new AngularClientValidationProvider());
	 
		List<AnnotationParserHandlerInterceptor> registry=new ArrayList<AnnotationParserHandlerInterceptor>();
		AddAnnotationParserHandlerInterceptors(registry);
		CustomRequestMappingHandlerMapping.setAnnotationParsers(registry.toArray(new AnnotationParserHandlerInterceptor[]{} ));
	 }

	 protected void InitAutoMapper()
	 {
		 AutoMapperProfile.Configure();
	 }
	protected void InitOther(ServletContext servletContext) {
		HibernateExceptionTranslator.InitStorage(new WebApplicationStorage(servletContext));
		Util.DictMap();
	}

	//

	// /// <summary>
	// /// Instantiate the container and add all Controllers that derive from
	// /// WindsorController to the container. Also associate the Controller
	// /// with the WindsorContainer ControllerFactory.
	// /// </summary>
	protected void InitializeServiceLocator() {
		ServiceLocator.SetLocatorProvider(() -> new SpringServiceLocator(
				_springContainer));
	}

	private void InitialiseHibernateSessions() {
		HibernateSession
				.setConfigurationCache(new HibernateConfigurationFileCache());
		NamingConvention.setNamespaceMapToTablePrefix(ProjectHierarchy
				.getNamespaceMapToTablePrefix());
		// NHibernateSessionModified.AutoMappingOverride = something;
		try {
			HibernateSessionModified.Init(new WebSessionStorage(),
					DOClassAnnotationAppender.getAnnotatedDoClasses(),
					"/hibernate.cfg.xml",
					HibernateSessionModified.getCfgProperties(), null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	protected void Application_Error(ServletRequest req, ServletResponse res) throws ServletException,IOException{
		Throwable e=(Throwable)req.getAttribute(GlobalConstant.Attr_Exception);
		res.setContentType("text/html; charset=utf-8");
		String debug=req.getServletContext().getInitParameter(GlobalConstant.ContextParam_Debug);
	    if(!debug.equalsIgnoreCase("true"))
	    {
	    	Util.AddLog("Application_Error", e);
	    	String html= "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body><h2>系统运行错误："+e.getClass()+"</h2></body></htm>";
	        res.getWriter().write(html);
	    }else{
	    	StringBuffer sb=new StringBuffer();
	    	sb.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body><p>");
	    	sb.append("<h1>"+e.getMessage()+"</h1>");
	    	for(StackTraceElement item :e.getStackTrace()){
	    		sb.append("at ");
	    		sb.append(item.toString());
	    		sb.append("<br/>");
	    	}
	    	sb.append("</p></body></htm>");
	    	res.getWriter().write(sb.toString());
	    }
	    res.getWriter().flush();
	}
	protected void Session_Start(HttpSessionEvent se) {
	}
	protected void Session_End(HttpSessionEvent se) {
	}
	protected void Application_BeginRequest(ServletRequestEvent sre) {
	}
	protected void Application_EndRequest(ServletRequestEvent sre) {
	}

	//--------------following are all sealed
	@Override
	public final void onStartup(ServletContext servletContext)
			throws ServletException {
		Application_Start(servletContext);
	}
	@Override
	public final void requestDestroyed(ServletRequestEvent sre) {
		Application_EndRequest(sre);
	}
	@Override
	public final void requestInitialized(ServletRequestEvent sre) {
		Application_BeginRequest(sre);
	}
	@Override
	public final void sessionCreated(HttpSessionEvent se) {
		Session_Start(se);
	}
	@Override
	public final void sessionDestroyed(HttpSessionEvent se) {
		Session_End(se);
	}

	@Override
	public final void init(ServletConfig config) throws ServletException {
		Util.InitStorage(new WebApplicationStorage(config.getServletContext()));
	}
	@Override
	public final ServletConfig getServletConfig() {
		return null;
	}
	/**
	 * MvcApplication class also serves as webapp error handler by being a servlet which is mapped when any error occurs
	 */
	@Override
	public final void service(ServletRequest req, ServletResponse res) throws ServletException, IOException{
		Application_Error(req,res);
	}
	@Override
	public final String getServletInfo() {
		return null;
	}
	@Override
	public final void destroy() {
	}
}
