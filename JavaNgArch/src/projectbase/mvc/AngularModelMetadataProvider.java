package projectbase.mvc;

import java.util.Map;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import projectbase.domain.DORef;
import projectbase.domain.DictEnum;
import projectbase.utils.JavaArchException;
import projectbase.utils.ProjectHierarchy;
import projectbase.utils.Res;

public class AngularModelMetadataProvider implements TypeFilter { 
	//types that can be annotated for validation(client),basically primitive types and non collection and DictEnum
    private static List<Class<?>> LeafModelTypes=Arrays.asList(
    		int.class,long.class,String.class,Date.class,java.util.Date.class,BigDecimal.class,boolean.class,short.class,byte.class,char.class,
    		Integer.class,Long.class,Boolean.class,Short.class,Byte.class,Object.class,DORef.class);
    //types that will be related to client validation
    private static List<Class<?>> DefaultClientValidationAnnotations=Arrays.asList(
    			NotNull.class,Size.class,Pattern.class,Max.class,Range.class,Min.class,Digits.class
    		);
	private static Map<Class<?>,ModelMetadata> metadatas=new HashMap<Class<?>,ModelMetadata>();;
	private static AngularModelMetadataProvider current;
    private static final List<Class<?>> primitiveNotNullTypes = Arrays.asList(
    		byte.class,
    		short.class,
    		int.class,
    		long.class,
    		boolean.class,
    		char.class
    	);
    public static String DisplayNameResourceClassKey = ProjectHierarchy.DisplayNameResourceClassKey;
    
	public static Map<Class<?>, ModelMetadata> getMetadatas() {
		return metadatas;
	}

	public static AngularModelMetadataProvider getCurrent() {
		return current;
	}
	public static AngularModelMetadataProvider setCurrent(AngularModelMetadataProvider value) {
		return current=value;
	}
	
	@Override
	public boolean match(MetadataReader metadataReader,
			MetadataReaderFactory metadataReaderFactory) throws IOException {
		/*auto scan and find all viewmodel class 
		*/
		String qclassname = metadataReader.getClassMetadata().getClassName();
		if(!qclassname.endsWith(GlobalConstant.ViewModel_ClassSuffix)&&!qclassname.endsWith(GlobalConstant.ViewModel_ClassSuffix2)) return false;
		try{
			metadatas.put(Class.forName(qclassname), null);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return false;
	}	
		

	public void CreateMetaDatas(){
		Class<?>[] keys=new Class<?>[1];
		keys=metadatas.keySet().toArray(keys);
		for(Class<?> clazz:keys){
			if(metadatas.get(clazz)==null){
				CreateMetadata(null,clazz,null,null,null);
			}
		}
	}
	protected ModelMetadata CreateMetadata(Class<?> containerType, Class<?> modelType, String propertyName,Method propertyGetter,Field field) {
		ModelMetadata meta=null;
        if(containerType==null){//the class itself
        	//1.metadata for the class itself
        	meta=metadatas.get(modelType);
        	if(meta!=null) return meta;
        	meta=new ModelMetadata();
        	meta.setModelType(modelType);
        	meta.setRequestValidationEnabled(false);
        	String displayName=DisplayName(null,modelType,null,null,null);
        	meta.setDisplayName(displayName);
        	meta.setProperties(new HashMap<String,ModelMetadata>());
        	metadatas.put(modelType, meta);
        	//2.metadata for  inner class
        	for(Class<?> innerClass : modelType.getDeclaredClasses()){
    			if(metadatas.get(innerClass)==null){
    				CreateMetadata(null,innerClass,null,null,null);
    			}
            }
        	//3.metadata for properties
        	PropertyDescriptor[] pds=PropertyUtils.getPropertyDescriptors(modelType);
        	for(PropertyDescriptor pd : pds){
        		Method getter=pd.getReadMethod();
        		if(getter==null||getter.getDeclaringClass()==Object.class)continue;
            	String prop=pd.getName();
            	Field f=null;
				try {
					f = modelType.getDeclaredField(prop);
				} catch ( SecurityException e) {
					throw new RuntimeException(e);
				}catch (NoSuchFieldException e) {
					//no field;
				}
            	meta.getProperties().put(prop, CreateMetadata(modelType,getter.getReturnType(),prop,getter,f));
            }
            return meta;
        }else{//is a property
        	if(!LeafModelTypes.contains(modelType) && !modelType.isArray() && !java.util.Collection.class.isAssignableFrom(modelType)  && !DictEnum.class.isAssignableFrom(modelType)){
        		ModelMetadata propertytypemeta=metadatas.get(modelType);
        		if(propertytypemeta==null){
        			propertytypemeta=CreateMetadata(null,modelType,null,null,null);
        		}
        		return propertytypemeta;
        	}
        	meta=new ModelMetadata();
        	meta.setField(field);
        	meta.setPropertyGetter(propertyGetter);
        	meta.setContainerType(containerType);
        	meta.setModelType(modelType);
        	String displayName=DisplayName(containerType,modelType,propertyName,propertyGetter,field);
        	meta.setDisplayName(displayName);
        	Class<?> returnType=propertyGetter.getReturnType();
        	if(DictEnum.class.isAssignableFrom(returnType)){
        			meta.setIsDictEnum(true);
        			meta.setDictEnumTypeName(returnType.getSimpleName());
        	}
        	if(returnType==String.class ){
        		Size size=field==null?null:field.getAnnotation(Size.class);
        		if(size==null)size=propertyGetter.getAnnotation(Size.class);
        		if(size!=null)meta.setMaxLength(size.max());
        	}
        	if(primitiveNotNullTypes.contains(returnType) || propertyGetter.isAnnotationPresent(NotNull.class)||(field!=null&&field.isAnnotationPresent(NotNull.class))){
        		meta.setIsRequired(true);
        	}
//        	if(meta.getIsRequired())
//        		meta.setRequestValidationEnabled(true);
//        	else
//        		meta.setRequestValidationEnabled(
//    				Arrays.stream(propertyGetter.getAnnotations()).anyMatch(o->DefaultClientValidationAnnotations.contains(o.getClass()))
//    				);
        	try {
				Method displayMethod=containerType.getDeclaredMethod(propertyGetter.getName().substring(3)+DisplayExtension.getSurfixForDisplay(), null);
				meta.setDisplayMethod(displayMethod);
			} catch (Exception e) {
			}
        	return meta;
        }


    }
	
    public ModelMetadata GetMetadataForProperty(Class<?> containerType, String propertyName){
    	ModelMetadata meta= metadatas.get(containerType).getProperties().get(propertyName);
    	if(meta!=null) return meta;
    	throw new JavaArchException("No metadata found for class: "+containerType.getName()+", property "+propertyName);
    }

    public ModelMetadata GetMetadataForType(Class<?> modelType){
    	ModelMetadata meta= metadatas.get(modelType);
    	if(meta!=null) return meta;
    	throw new JavaArchException("No metadata found for class: "+modelType.getName());
    }

    public ModelMetadata GetMetadataForPropertyPath(Object model, String propertyPath) throws IntrospectionException{
    	
    	PropertyDescriptor prop=PropertyAccessorFactory.forBeanPropertyAccess(model).getPropertyDescriptor(propertyPath);
    	
    	ModelMetadata meta=metadatas.get(prop.getReadMethod().getDeclaringClass());
    	if(meta==null) throw new JavaArchException("No metadata found for class: "+prop.getReadMethod().getDeclaringClass().getName());
    	meta= meta.getProperties().get(prop.getName());
    	if(meta!=null) return meta;
    	throw new JavaArchException("No metadata found for propertyPath: "+propertyPath);
    }
    

    protected String DisplayName(Class<?> containerType, Class<?> modelType, String propertyName,Method propertyGetter,Field field)
    {
    	DisplayName nameAttr=null;
    	if(containerType==null){
    		nameAttr=modelType.getAnnotation(DisplayName.class);
    		if(nameAttr!=null) 
    			return nameAttr.value();
    		else
    			return null;
    	}
    	nameAttr=field==null?null:field.getAnnotation(DisplayName.class);
    	if(nameAttr==null)nameAttr=propertyGetter.getAnnotation(DisplayName.class);
    	if(nameAttr!=null) return nameAttr.value();

        String displayName= DisplayNameForProp(containerType, propertyName,propertyGetter,field);

        if (StringUtils.isEmpty(displayName) && propertyName != null && propertyName.endsWith(DisplayExtension.getSurfixForDisplay()) && containerType != null)
        {
        	Method soureMethod=null;
			try {
				soureMethod = containerType.getMethod(propertyGetter.getName().replace(DisplayExtension.getSurfixForDisplay(),""));
			} catch (Exception e) {
				e.printStackTrace();
			} 
        	displayName = DisplayNameForProp(containerType, propertyName.replace(DisplayExtension.getSurfixForDisplay(), ""),soureMethod,field);
        }
        if (StringUtils.isEmpty(displayName))
        	displayName = null;//important!don't let DisplayName be empty,make it null insteadof empty.Otherwise ValidationContext.set_DisplayName would throw exception
        return displayName;
    }

    private String DisplayNameForProp(Class<?> containerType ,String propertyName,Method propertyGetter,Field field)
    {
        String resourceName = "";
        DisplayNameKey keyattr =field==null?null:field.getAnnotation(DisplayNameKey.class);
        if(keyattr==null) keyattr =propertyGetter.getAnnotation(DisplayNameKey.class);
        if(keyattr==null)
        {
            keyattr = containerType.getAnnotation(DisplayNameKey.class);
        }
        
        if (keyattr != null)
        {
            resourceName = keyattr.value();
            if (!StringUtils.isEmpty(resourceName)) return resourceName;
        }
        DisplayName nameAttr=null;
        if (containerType != null)
        {
        	nameAttr =containerType.getAnnotation(DisplayName.class);
            if (nameAttr != null)
                resourceName = nameAttr.value() + "_";
        }
        String pname=StringUtils.capitalize(propertyName);
        if (pname != null) resourceName = resourceName + pname;

        return resourceName;
    }
}


