package projectbase.mvc;

import java.util.Calendar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;
import org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import projectbase.mvc.converter.StringToDictEnumConverterFactory;

@Configuration
public class SpringMvcConfig extends WebMvcConfigurationSupport {
	 
	@Override
	protected ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer() {
		ConfigurableWebBindingInitializer initailizer=super.getConfigurableWebBindingInitializer();
		initailizer.setPropertyEditorRegistrar(new CustomPropertyEditorRegistrar());
		return initailizer;
	}

	@Bean @Override
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping hm= new CustomRequestMappingHandlerMapping();
		hm.setOrder(0);
		hm.setInterceptors(this.getInterceptors());
		hm.setContentNegotiationManager(mvcContentNegotiationManager());
		return hm;
	}	
	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	/*	replaced by angular
	@Bean
	public RequestToViewNameTranslator viewNameTranslator() {
		return new RequestToSuffixedViewNameTranslator();
	}
	@Bean 
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tc=new TilesConfigurer();
		tc.setUseMutableTilesContainer(true);
		return tc;
	}*/
	@Bean
	@Override
	public FormattingConversionService mvcConversionService() {
		// Use the DefaultFormattingConversionService but do not register defaults
		DefaultFormattingConversionService conversionService = new	DefaultFormattingConversionService(false);
		// Ensure @NumberFormat is still supported
		conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
		
		// just handling JSR-310 specific date and time types
		new DateTimeFormatterRegistrar().registerFormatters(conversionService);
	
		// Register date conversion with a specific global format(default for date field without @DateTimeFormat annotation)
		DateFormatterRegistrar registrar = new DateFormatterRegistrar();
		DateFormatter defaultFormatter=new DateFormatter("yyyy-MM-dd");
		//registrar.setFormatter(defaultFormatter);
		//registrar.registerFormatters(conversionService);
		//the above two lines would result in that the default formatter comes before annotation,so I changed to do the following to make sure 
		//annotation formatter be chosen at first.( to do that,wired enough, addFormatterForFieldAnnotation() has to be called after addFormatter())
		DateFormatterRegistrar.addDateConverters(conversionService);
		conversionService.addFormatter(defaultFormatter);
		conversionService.addFormatterForFieldType(Calendar.class, defaultFormatter);
		conversionService.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());

		conversionService.addConverterFactory(new StringToDictEnumConverterFactory());	
		return conversionService;
	}

	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new BaseController());
		super.addInterceptors(registry);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//registry.addViewController("/Shared/ContentTemplate").setViewName("/Shared/ContentTemplate");
	/*	registry.addViewController("/").setViewName("home/Home/Welcome");
		registry.addViewController("/home").setViewName("home/Home/Welcome");
		registry.addViewController("/home/Home").setViewName("home/Home/Welcome");
		registry.addViewController("/home/").setViewName("home/Home/Welcome");
		registry.addViewController("/home/Home/").setViewName("home/Home/Welcome");
		*/
		//使用angular时，不经服务器controller处理的页面直接用htm扩展名
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
	/*	
		TilesViewResolver resolver = new TilesViewResolver();
		resolver.setOrder(0);
		resolver.setCache(false);
		 registry.viewResolver(resolver);
	*/	 
			InternalResourceViewResolver resolver2 = new InternalResourceViewResolver();
			//resolver2.setOrder(1);
	        resolver2.setPrefix("/");
	        resolver2.setSuffix(".jsp");
	        registry.viewResolver(resolver2);
 	}



	
}
