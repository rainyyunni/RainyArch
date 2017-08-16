package projectbase.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

public class QuerySelectorBuilder {
    private static char[] _caption=new char[]{
            'A','B','C','D','E','F','G','H','I','J','K','L',
            'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
};
    
	public static String BuildSelector(Class<?> daoClass,Class<?>dtoClass){
		return BuildSelector(daoClass,dtoClass,null);
	}
	
    public static String BuildSelector(Class<?> daoClass,Class<?>dtoClass,Map<String,String> propNameMap){
		    String selector = "";
      	PropertyDescriptor[] props=PropertyUtils.getPropertyDescriptors(dtoClass);
      	String parameterExpr = "this";
      	for(PropertyDescriptor prop : props){
      		Method setter=prop.getWriteMethod();
      		if(setter==null||setter.getDeclaringClass()==Object.class)continue;
          	String propName=prop.getName();
          	Method getter=prop.getReadMethod();

          	String entityPropExpr=null; 
          	if (propNameMap != null && (propNameMap.containsKey(StringUtils.capitalize(propName))||propNameMap.containsKey(propName))){
          		String propMappedName=propNameMap.get(StringUtils.capitalize(propName));
          		if(propMappedName==null)propMappedName=propNameMap.get(propName);
          		entityPropExpr=ChainedPropExprByMap(parameterExpr, propMappedName, daoClass);
          	}else
          		entityPropExpr=ChainedPropExpr(parameterExpr, getter.getName().substring(3), daoClass);
              if (entityPropExpr == null) throw new InvalidOperationException("can't figure out " + propName);
              selector=selector+","+entityPropExpr+" as "+propName;
          }
          return selector.substring(1);
		}
      private static Method BeanClassReadMethod(Class<?> beanClass,String propName){
      	try{
      		Method m=beanClass.getMethod("get"+propName);
      		return m;
      	}catch(NoSuchMethodException e){
      		return null;
      	}
      }
      private static String ChainedPropExpr(String expr,String propName,Class<?> containerType)
      {
          if (propName==null) return null;
          if (BeanClassReadMethod(containerType,propName)!=null) return expr+"."+StringUtils.uncapitalize(propName);
          int posb=-1;
          while(true){
	          int firstWordEnd = StringUtils.indexOfAny(propName.substring(posb+2),_caption);
	          if(firstWordEnd<=0) break;
	          posb=posb+2+firstWordEnd;
	          String firstWord = propName.substring(0, posb);
	          Method entityProp=BeanClassReadMethod(containerType,firstWord);
	          if (entityProp==null) {
	        	  continue;
	          }
	          String firstPart = expr+"."+StringUtils.uncapitalize(firstWord);
	          String sencondPart = propName.substring(posb);
	          return ChainedPropExpr(firstPart, sencondPart, entityProp.getReturnType());
          }
          return null;
     }

      private static String ChainedPropExprByMap(String expr, String propMappedName, Class<?> containerType)
      {
          if (propMappedName == "") return null;
          if (BeanClassReadMethod(containerType,propMappedName)!=null) return expr+"."+StringUtils.uncapitalize(propMappedName);
          int firstWordEnd = propMappedName.indexOf("|");
          if (firstWordEnd <= 0) return null;
          String firstWord = propMappedName.substring(0, firstWordEnd);
          Method entityProp=BeanClassReadMethod(containerType,firstWord);
          if (entityProp==null) return null;
          String firstPart = expr+"."+StringUtils.uncapitalize(firstWord);
          String sencondPart = propMappedName.substring(firstWordEnd+1);
         return ChainedPropExprByMap(firstPart, sencondPart, entityProp.getReturnType());
 
      }
}
