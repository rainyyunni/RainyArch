package projectbase.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.fasterxml.jackson.databind.ObjectMapper;

import projectbase.bd.IApplicationStorage;
import projectbase.data.UtilQuery;
import projectbase.domain.BaseDomainObject;
import projectbase.domain.DORef;
import projectbase.domain.DictEnum;
import projectbase.mvc.DisplayExtension;
import projectbase.mvc.GlobalConstant;
import projectbase.mvc.WebApplicationStorage;
import projectbase.sharparch.hibernate.HibernateQuery;

    public class Util extends HibernateQuery
    {
    	private static ObjectMapper om=new ObjectMapper();
        private static IApplicationStorage _appState;
        private static String logRoot;
        private static String _key_For_FuncMap = "Key_For_FuncMap";
        private static String _key_For_FuncTree = "Key_For_FuncTree";

        public static String getLogRoot(){
        	if(logRoot!=null)return logRoot;

			if (logRoot == null){
				String logroot = _appState
						.GetInitParameter(GlobalConstant.ContextParam_LogRoot);
				if(logroot != null && !logroot.isEmpty()) {
					logRoot = _appState.GetRealPath(logroot);
				}
			}
			return logRoot;
        }
        public static void setLogRoot(String lr){
        	logRoot=lr;
        }
        //db Map items map
		public static Map<String, Map<Integer,String>> DictMap ()
        {
            Map<String, Map<Integer,String>> dictMap=DictEnum.getDictMap();
            Map<String,Integer> enumMap=DictEnum.getEnumMap();
               if ( dictMap== null)
                {
                    //load dict items from db and keep the map in memory
                    List<Object[]> list = (List<Object[]>)UtilQuery.StatelessGetBySql("select ifnull(a.ConstName,''),b.ItemId,b.ItemName,b.ConstName as ItemConstName from GN_DictItem b inner join GN_Dict a on b.DictID=a.ID order by a.ConstName,b.ItemId");
                    dictMap=new Hashtable<String,Map<Integer,String>>();
                    enumMap=new Hashtable<String,Integer>();
                    for(int i=0;i<list.size();i++){
                    	String dictName=(String)list.get(i)[0];
                    	Map<Integer,String> itemMap=dictMap.get(dictName);
                    	if(itemMap==null){
                    		itemMap=new TreeMap<Integer,String>();
                    		dictMap.put(dictName, itemMap);
                    	}
                    	Short itemid=(Short)list.get(i)[1];
                    	itemMap.put(itemid.intValue(), (String)list.get(i)[2]);
                    	enumMap.put(dictName+"."+list.get(i)[3], itemid.intValue());
                    }
                    //add any extra mapping 
                    //map.put("ROV","R.O.V");
                    //map.put("Undefined","");
                
               DictEnum.setDictMap(dictMap);
               DictEnum.setEnumMap(enumMap);
                }
                return dictMap;

        }
        //db func map
        public static Map<String, Integer> FuncMap()
        {

                if (_appState == null) throw new JavaArchException("Util has not been initiated yet!");
                if (_appState.Get(_key_For_FuncMap) == null)
                {
                    //load from db and keep the map in memory
                	List<Object[]> list = (List<Object[]>)UtilQuery.StatelessGetBySql("select Code,Id from GN_Func");
                	_appState.Set(_key_For_FuncMap,Util.<String,Integer>ListToMap(list));


                    if (_appState.Get(_key_For_FuncMap) == null)
                    	_appState.Set(_key_For_FuncMap,new HashMap<String, Integer>());
                }
                return (Map<String, Integer>)_appState.Get(_key_For_FuncMap);
            }

        //generate html for func checkboxtree once 
        public static String FuncTree()
        {
                if (_appState == null) throw new JavaArchException("Util has not been initiated yet!");
                if (_appState.Get(_key_For_FuncTree) == null)
                {
                    //load from db and keep the tree in memory
                    List<Object[]> list = (List<Object[]>)UtilQuery.StatelessGetBySql("select Id,Level,Code,Name from GN_Func Order by Level");
                    String html0 = "<div class='row' pb-init-var='d' pb-init-data='{";
                    String html = "";
                    int activelevellength = 2;
                    String ids="";
                    String levels = "";
                    for (int i = 0; i < list.size();i++)
                    {
                    	ids=ids+","+list.get(i)[0];
                        String currentlevel =(String)list.get(i)[1];
                        levels = levels + ",\"" + currentlevel+"\"";
                        String nextlevel="00";
                        if (i + 1 < list.size())
                        {
                            nextlevel = (String) list.get(i + 1)[1];
                        }
                        if(currentlevel.length()==2){
                        	if(i>0 && ((String)list.get(i-1)[1]).length()!=2){
                        		html = html +"</ul></div>";
                        	}
                        	html = html +"<div class='panel panel-default col-md-3'><ul style='list-style:none'>";
                        }
                        
                    	String omg="d.isCollapsed[\"" + currentlevel + "\"]";
                        String lihtml="";
                        if(nextlevel.length()>currentlevel.length()){
                        	lihtml = "<li  style='list-style:none'><span class='glyphicon' ng-class='"+omg+"?\"glyphicon-plus\":\"glyphicon-minus\"' ng-click='"+omg+"=!"+omg+"'></span>";
                        }else{
                        	lihtml = "<li  style='list-style:none'>";
                        }
                        if (currentlevel.length()>2 && currentlevel.length() > activelevellength)
                        {
                        	String parentlevel=currentlevel.substring(0,currentlevel.length()-2);
                            html = html + "<ul  style='list-style:none' uib-collapse='d.isCollapsed[\"" + parentlevel + "\"]'>";
                            html = html +lihtml+ "<input type='checkbox' ng-change='d.chk_change(\"" + currentlevel + "\")' ng-disabled='disabled||d.disableList[" + i + "]' ng-model='d.selectedList[" + i + "]' ><label>" +
                            		list.get(i)[3] + "</label>";
                            activelevellength = activelevellength + 2;
                        }
                        else if (currentlevel.length() == activelevellength || currentlevel.length()==2)
                        {
                            html = html + lihtml+"<input type='checkbox' ng-change='d.chk_change(\"" + currentlevel + "\")' ng-disabled='disabled||d.disableList[" + i + "]' ng-model='d.selectedList[" + i + "]' ><label>" +
                            		  list.get(i)[3] + "</label>";
                        }
                        if (nextlevel.length() < activelevellength)
                        {
                            int times = (activelevellength - nextlevel.length()) / 2;//两位一级
                            if (nextlevel.length() == 2) times = times - 1;
                            for (int j = 0; j <= times;j++ )
                                html = html + "</ul>";
                            activelevellength = activelevellength-(times+1) * 2;
                        }
                    }
                    html = html + "</div>";
                    html = html0 +"\"funcList\":["+ids.substring(1) + "],"+"\"levelList\":["+levels.substring(1) + "]}'>\n" + html;
                    _appState.Set(_key_For_FuncTree,html);
                }

                return (String)_appState.Get(_key_For_FuncTree);
            }

        public static void InitStorage(IApplicationStorage appState)
        {
            _appState = appState;
        }

        public static void AddLog(String psSource, Throwable e)
        {
        	if(e==null) {AddLog(psSource);return;}
            String sMessage = GetExceptionMsg(e);
            String sFileName = getLogRoot() + "\\" + DisplayExtension.Display(new Date()) + ".log";

            try{
            PrintWriter oWriter= new PrintWriter(new BufferedWriter(new FileWriter(sFileName,true))) ;
            oWriter.println("Source:" + psSource);
            oWriter.println("Time:" + DateFormatUtils.format(new Date(), "hh:mm:ss"));
            oWriter.println("Message:" + sMessage);
            oWriter.println("StackTrace:");
            e.printStackTrace(oWriter);
            oWriter.println("----------------------------------");
            oWriter.flush();
            oWriter.close();
            }catch(Exception ex){
            	throw new RuntimeException(ex);
            }
        }
        public static void AddLog(String msg)
        {
            String sFileName = getLogRoot() + "\\" + DisplayExtension.Display(new Date()) + ".log";

            try{
            PrintWriter oWriter= new PrintWriter(new BufferedWriter(new FileWriter(sFileName,true))) ;
            oWriter.println("Time:" + DateFormatUtils.format(new Date(), "hh:mm:ss"));
            oWriter.println("Message:" + msg);
            oWriter.println("----------------------------------");
            oWriter.flush();
            oWriter.close();
            }catch(Exception ex){
            	throw new RuntimeException(ex);
            }
        }
        public static void Log4jLog(String msg){
        	Logger.getLogger("debug").debug(msg);
        }
        public static Session CurrentSession()
        {
            return (new Util()).getSession();
        }

        public static String GetExceptionMsg(Throwable e)
        {
         
        	String sMsg = "\r\n" + e.getMessage();
            return sMsg;
        }
//        public static boolean IsNullableType(Type theType)
//        {
//            return theType.IsGenericType && theType.GetGenericTypeDefinition() == typeof (Nullable<>);
//        }
        /**
         * 将list的第0列作为key,第1列作为value，转换为Map
         * @param list
         * @return
         */
        @SuppressWarnings("unchecked")
		public static <TKey,TValue>  Map<TKey,TValue> ListToMap(List<Object[]> list)
        {
        	if(list.size()==0)return null;
        	Map<TKey,TValue> m=new HashMap<TKey,TValue>();
        	for(Object[] row : list){
        		m.put((TKey)row[0],(TValue)row[1]);
        	}
			return m;
		}
        /**
         * 将list转换为Map,按指定的表达式来对应key和value
         * @param list
         * @param keyFunc
         * @param valueFunc
         * @return
         */
        public static <TKey,TValue>  Map<TKey,TValue> ListToMap(List<Object[]> list,Function<Object[],TKey> keyFunc,Function<Object[],TValue> valueFunc)
        {
        	if(list.size()==0)return null;
        	Map<TKey,TValue> m=new HashMap<TKey,TValue>();
        	for(Object[] row : list){
        		TKey key=keyFunc.apply(row);
        		TValue value=valueFunc.apply(row);
        		m.put(key,value);
        	}
			return m;
		}
        @SuppressWarnings("unchecked")
		public static Map<String,Object> ObjectToMap(Object obj)
        {
        	if(obj instanceof Map) return (Map<String,Object>)obj;
        	return om.convertValue(obj, Map.class);
		}
        public static boolean IsAjaxRequest(HttpServletRequest request) {
    		return request.getHeader("X-Requested-With") != null
    				&& request.getHeader("X-Requested-With").equalsIgnoreCase(
    						"XMLHttpRequest");
    	}
        
        
        /**
         * 将list转换为逗号分隔的文号，方便hql in查询
         * @param list
         * @return
         */
        @SuppressWarnings("rawtypes")
		public static String listToStringWH(List list) {  
            StringBuilder sb = new StringBuilder();  
            if (list != null && list.size() > 0) {  
                for (int i = 0; i < list.size(); i++) {  
                    if (i < list.size() - 1) {  
                        sb.append("?" + ",");  
                    } else {  
                        sb.append("?");  
                    }  
                }  
            }  
            return sb.toString();  
        }  
        
        /**
         * 将数组转换为逗号分隔的文号，方便hql in查询
         * @param list
         * @return
         */
		public static String listToStringWH(Object[] list) {  
           return listToStringWH(Arrays.asList(list));
        }  
        //des加密后base64编码
        public static String DesEncrypt(String pToEncrypt, String desKey)
        {
        	try{
	            IvParameterSpec iv = new IvParameterSpec(desKey.getBytes(StandardCharsets.US_ASCII));
	            // 从原始密匙数据创建DESKeySpec对象  
	            DESKeySpec dks = new DESKeySpec(desKey.getBytes(StandardCharsets.US_ASCII));  
	            // 创建一个密匙工厂，然后用它把DESKeySpec转换成  
	            // 一个SecretKey对象  
	            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
	            SecretKey securekey = keyFactory.generateSecret(dks);  
	            // Cipher对象实际完成加密操作  
	            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");  
	            cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);  
	            byte[] encrypted=cipher.doFinal(pToEncrypt.getBytes(StandardCharsets.UTF_8));  
	            return new String(Base64.getEncoder().encode(encrypted),StandardCharsets.US_ASCII);
        	}catch(Exception e){
        		return "";
        	}
        	
        }
        //base64解码后es解密
        public static String DesDecrypt(String pToDecrypt, String desKey)
        {
        	byte[] inputByteArray = Base64.getDecoder().decode(pToDecrypt.getBytes(StandardCharsets.US_ASCII));
        	try{
        		IvParameterSpec iv = new IvParameterSpec(desKey.getBytes(StandardCharsets.US_ASCII));
	            // 从原始密匙数据创建DESKeySpec对象  
	            DESKeySpec dks = new DESKeySpec(desKey.getBytes(StandardCharsets.US_ASCII));  
	            // 创建一个密匙工厂，然后用它把DESKeySpec转换成  
	            // 一个SecretKey对象  
	            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
	            SecretKey securekey = keyFactory.generateSecret(dks);  
	            // Cipher对象实际完成加密操作  
	            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");  
	            cipher.init(Cipher.DECRYPT_MODE, securekey, iv);  
	            byte[] decrypted=cipher.doFinal(inputByteArray);  
	            return new String(decrypted, StandardCharsets.UTF_8);
        	}catch(Exception e){
        		return "-1";
        	}
        	

        }
        
        /**
         * 获得2个时间中间的月份列表
         * @param start
         * @param end
         * @return
         */
        public static List<String> getMonths(Date start,Date end){
        	List<String> result = new ArrayList<String>();
        	try{
	        	SimpleDateFormat myFormat=new SimpleDateFormat("yyyy-MM");
	    		Date startDate = myFormat.parse(myFormat.format(start));
	    		Date endDate = myFormat.parse(myFormat.format(end));
	    		while (startDate.before(endDate) || startDate.equals(endDate)) {
	    			result.add(myFormat.format(startDate));
	    			startDate = DateUtils.addMonths(startDate, 1);
	        	}
        	}catch(Exception e){
        		
        	}
    		return result;
        }
        
        public static boolean IsNull(BaseDomainObject entity)
        {
            return entity == null || entity.getId() == null;
        }
        public static boolean IsNull(DORef doRef)
        {
            return doRef == null || doRef.getId() == null;
        }
        public static boolean IsNotNull(BaseDomainObject entity)
        {
            return entity != null && entity.getId() != null;
        }
        public static boolean IsNotNull(DORef doRef)
        {
            return doRef != null && doRef.getId() != null;
        }
        //将客户端指定目录下所有后缀为Ctrl.js的脚本文件合并为一个字符串返回
        public static String NgControllerJs(String webroot)
        {
        	String debug=_appState.GetInitParameter(GlobalConstant.ContextParam_Debug);
            if (debug==null || debug.equalsIgnoreCase("false")) return "";
            if(StringUtils.isEmpty(webroot))
            	webroot =_appState.GetInitParameter("WebJsDebug");
            try{
	            Object[] filePaths=Files.find(Paths.get(webroot),5,(p,b)->p.toString().endsWith("Ctrl.js")).toArray();
	            String scripts = "";
	            for(Object filePath:filePaths){
	            	scripts=scripts+StringUtils.join(Files.readAllLines((Path)filePath),"\r\n")+"\r\n";
	            }
	            filePaths=Files.find(Paths.get(webroot+"/Shared/Directive/"),5,(p,b)->p.toString().endsWith(".js")).toArray();
	            for(Object filePath:filePaths){
	            	scripts=scripts+StringUtils.join(Files.readAllLines((Path)filePath),"\r\n")+"\r\n";
	            }
	            return scripts;
            }catch(Exception e){
            	throw new RuntimeException(e);
            }
        }
        
        //比较数据库多行与输入的多行，返回需新增修改删除的行
        //返回值：集合的第一个成员是一个Map<inputObjectType,DomainObjectType>,表示需要修改的，
        //其余是input或DomainObject类型对象，input类型的是要新增的，DomainObject类型的是要删除的，
        public static List<Object> CudList(List<? extends InputObjectWithId> inputList,List<? extends BaseDomainObject> entityList){
			List<Object> rtn=new ArrayList<Object>();
			Set<Object> newordelete=new HashSet<Object>();
			Map<InputObjectWithId,BaseDomainObject> map=new HashMap<InputObjectWithId,BaseDomainObject>();
			newordelete.addAll(entityList);
			for(InputObjectWithId in :inputList){
				boolean foundinput=false;
				for(BaseDomainObject en :entityList){
					if(in.getId()==en.getId()){
						foundinput=true;
						newordelete.remove(en);
						map.put(in, en);
					}
				}
				if(!foundinput){
					newordelete.add(in);
				}
			}
			rtn.add(map);
			rtn.addAll(newordelete);
			return rtn;
        }
    }

