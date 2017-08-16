
package projectbase.mvc;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import projectbase.domain.BaseDomainObjectWithTypedId;
import projectbase.domain.DORef;
import projectbase.domain.DictEnum;


    public final class DisplayExtension
    {
        private static String surfixForDisplay = "_Display";
        public static String getSurfixForDisplay(){
        	return surfixForDisplay;
        }
        public static String Display( Boolean value)
        {
            if (value == null) return "";
            return value ? "是" : "否";
        }
        public static String Display(Date value){
        	if (value == null) return "";
        	DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        	return df.format(value);
        }
        public static String Display(Date value,String format){
        	if (value == null) return "";
        	DateFormat df=new SimpleDateFormat(format);
        	return format == null ? value.toString()  : df.format(value);
        }

        public static String Display(BigDecimal value)
        {
            if (value == null) return "";
            return value.toString();
        }
        public static String Display(BigDecimal value, String format)
        {
            if (value == null) return "";
            DecimalFormat df=new DecimalFormat(format);
            return format == null ? value.toString()  :  df.format(value);
        }
        public static <T extends BaseDomainObjectWithTypedId<Integer>>String Display(DORef<T> value)
        {
            if (value == null) return "";
            return value.getRefText();
        }
        public static String Display(DictEnum dictEnum)
        {
            if (dictEnum == null || dictEnum.getIntValue()==null) return "";
            return dictEnum.getText();
        }
        public static String Display(Integer value,String dictEnumTypeName)
        {
        	if(value==null) return "";
            return DictEnum.getDictMap().get(dictEnumTypeName).get(value);
        }
        public static Object Display(Object value, String format)
        {
            if (value == null) return "";
            if (value instanceof DORef<?>) return Display((DORef<?>)value);
            if (value instanceof DictEnum) return Display((DictEnum)value);
            if (value instanceof Integer && format!=null) return Display((Integer)value,format);
            
            if (value instanceof Boolean) return Display((Boolean)value);

            if (format == null)
            {
                if (value instanceof BigDecimal) return Display((BigDecimal)value);
                if (value instanceof Date) return Display((Date)value);
            }
            else
            {
                if (value instanceof BigDecimal) return Display((BigDecimal) value, format);
                if (value instanceof Date) return Display((Date) value, format);
            }
            if (value.getClass().isPrimitive() || value instanceof String
            		 || value instanceof Number
            		 || value instanceof Boolean
            		 || value instanceof  Character)
                return value.toString();
            return GetDisplayDictionary(value);
        }

        public static Object Display(Object value)
        {
            return Display(value, null);
        }

        private static Map<String, Object> GetDisplayDictionary(Object obj)
        {
        	Map<String, Object> map=new HashMap<String, Object>();
        	Class<?> clazz=obj.getClass();
        	PropertyDescriptor[] pds=PropertyUtils.getPropertyDescriptors(clazz);
        	for(PropertyDescriptor pd : pds){
        		Method getter=pd.getReadMethod();
        		if(getter==null||getter.getDeclaringClass()==Object.class)continue;
            	String prop=StringUtils.capitalize(pd.getName());

            	try{
            		Method displaygetter=clazz.getMethod(getter.getName().substring(3)+ surfixForDisplay,(Class<?>[])null);
            		map.put(prop, displaygetter.invoke(obj,(Object[])null));
            	}catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
            		DateTimeFormat format1=getter.getAnnotation(DateTimeFormat.class);
            		NumberFormat format2=getter.getAnnotation(NumberFormat.class);
            		try{
	            		if(format1==null && format2==null){
	            			map.put(prop, Display(getter.invoke(obj,(Object[])null)));
	            		}else if(format1!=null){
	            			map.put(prop, Display(getter.invoke(obj,(Object[])null),format1.pattern()));
	            		}else{
	            			map.put(prop, Display(getter.invoke(obj,(Object[])null),format2.pattern()));
	            		}
            		}catch(Exception ex){
            			throw new RuntimeException(ex);
            		}
          		
            	}
            }
            return map;
        }

    }

