package projectbase.domain;

import java.util.Hashtable;
import java.util.Map;

import projectbase.utils.JavaArchException;

public class DictEnum {
	private static Map<String,Map<Integer,String>> dictMap;
	private static Map<String,Integer> enumMap;
      
    public static Map<String, Map<Integer, String>> getDictMap() {
		return dictMap;
	}
    public static Map<String,Integer> getEnumMap() {
		return enumMap;
	}
	public static void setDictMap(Map<String, Map<Integer, String>> dictMap) {
		DictEnum.dictMap = dictMap;
	}
	public static void setEnumMap(Map<String,Integer> enumMap) {
		DictEnum.enumMap = enumMap;
	}
		private Integer value;
        
        public DictEnum setIntValue(Integer value) {
			this.value = value;
			return this;
		}

		public Integer getIntValue(){
        	return value;
        }
        public DictEnum(){

        }     
        public DictEnum(Integer value){
        	this.value=value;
        }
        
        public String getText(){
        	if(value==null)return "";
        	String text=null;
        	Map<Integer,String> dict=dictMap.get(this.getClass().getSimpleName());
        	if(dict!=null) {
        		text=dict.get(value);
        	}
        	return text==null?this.getClass().getSimpleName()+value.toString():text;
        } 
        
        public static <T extends DictEnum> T ValueOf(Class<? extends DictEnum> clazz,Integer value)  {
        	T obj;
			try {
				obj = (T) clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new JavaArchException(e);
			}
        	obj.setIntValue(value);
        	return obj;

    	}
       public static boolean EqualConst(DictEnum dictEnum,String constantName){
    	   return dictEnum.getIntValue()==enumMap.get(constantName);
       }

}