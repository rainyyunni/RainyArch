package projectbase.bd;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javassist.NotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import projectbase.data.*;
import projectbase.data.hibernatemapbycode.convention.DOClassAnnotationAppender;
import projectbase.mvc.AnnotatingConvention;
import projectbase.mvc.SpringMvcConfig;
import projectbase.sharparch.hibernate.*;
import projectbase.sharparch.hibernate.mvc.WebSessionStorage;
import projectbase.sharparch.mvc.ControllerExtensions;
import projectbase.utils.JavaArchException;

public class SpringComponentRegistrar implements TypeFilter,BeanFactoryPostProcessor {
	public static Logger log = Logger.getLogger(SpringComponentRegistrar.class);

	public static void AddComponentsTo(
			AnnotationConfigWebApplicationContext container,Class<?> componentScanConfigClass) {

		container.register(SpringComponentRegistrar.class);
		container.register(componentScanConfigClass);
		container.register(SpringMvcConfig.class);
		container.register(DefaultSessionFactoryKeyProvider.class);
		container.register(HibernateExceptionTranslator.class);
		container.register(WebSessionStorage.class);
		container.register(UtilQuery.class);
	}
	public static void AddComponentsTo(
			AnnotationConfigApplicationContext container,Class<?> componentScanConfigClass) {

		container.register(SpringComponentRegistrar.class);
		container.register(componentScanConfigClass);
		container.register(SpringMvcConfig.class);
		container.register(DefaultSessionFactoryKeyProvider.class);
		container.register(HibernateExceptionTranslator.class);
		container.register(WebSessionStorage.class);
		container.register(UtilQuery.class);
	}
	@Override
	public boolean match(MetadataReader metadataReader,
			MetadataReaderFactory metadataReaderFactory) throws IOException {
		/*auto scan and register all controller/BD/Dao/Query classes
		*/
		String qclassname = metadataReader.getClassMetadata().getClassName();
		if (ControllerExtensions.IsController(qclassname)) {
			try{
				new AnnotatingConvention().GetAnnotatedClass(qclassname);
			}catch(NotFoundException e){
				throw new RuntimeException(e);
			}
			return true;
		}

		if (qclassname.endsWith("BD") || qclassname.endsWith("Dao")
				|| qclassname.endsWith("Query")) {
			return true;
		}
		return false;
	}


	public static void RegisterGenericDao(DefaultListableBeanFactory beanFactory) throws BeansException {
		//auto register all constructed classes(from generic CommonBD<T> and BaseHibernateDao<T>) with each Domain Object class 
		
		for(Class<?> doClazz:DOClassAnnotationAppender.getAnnotatedDoClasses()){
			String qclassname=doClazz.getName();
			String classname=StringHelper.unqualify(qclassname);
			String daoBeanName=classname+"Dao";
			if (beanFactory.containsBean(daoBeanName)) continue;
			BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(BaseHibernateDao.class);
			bdb.addConstructorArgValue(doClazz);
			String formula;
			try {
				formula = (String)doClazz.getMethod("GetRefTextFormula").invoke(null);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				throw new JavaArchException(e);
			}
			bdb.addConstructorArgValue(formula);
			bdb.setScope("singleton");
			beanFactory.registerBeanDefinition(daoBeanName, bdb.getBeanDefinition());
			
			String bdBeanName=classname+"BD";
			String beanNameRegisteredByTypeFilter=StringUtils.uncapitalize(bdBeanName);
			if (beanFactory.containsBean(beanNameRegisteredByTypeFilter)){
				bdb = BeanDefinitionBuilder.rootBeanDefinition(beanFactory.getBeanDefinition(StringUtils.uncapitalize(beanNameRegisteredByTypeFilter)).getBeanClassName());
				bdb.addPropertyReference("dao", daoBeanName);
				beanFactory.removeBeanDefinition(beanNameRegisteredByTypeFilter);
			}
			else{
				bdb = BeanDefinitionBuilder.rootBeanDefinition(CommonBD.class);
				bdb.addConstructorArgReference(daoBeanName);
			}
			bdb.setScope("singleton");
			beanFactory.registerBeanDefinition(bdBeanName, bdb.getBeanDefinition());
		}
	}

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		((DefaultListableBeanFactory) beanFactory).setAllowEagerClassLoading(false);
		RegisterGenericDao((DefaultListableBeanFactory)beanFactory);
		
	}


}
