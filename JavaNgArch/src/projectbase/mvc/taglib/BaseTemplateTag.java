package projectbase.mvc.taglib;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import projectbase.domain.DictEnum;
import projectbase.mvc.Action;
import projectbase.mvc.GlobalConstant;
import projectbase.utils.JavaArchException;


@SuppressWarnings("serial")
public abstract class BaseTemplateTag extends AbstractDataBoundFormElementTag implements BaseHtmlTag{

	public static final String GlobalFolderName="Shared";
	public static final String Key_Model="model";
	public static final String Key_ModelMetadata="modelMetadata";
	public static final String Key_ModelType="modelType";
	
	private static final Map<Class<?>,String> typeToTemplateNameMap=new Hashtable<Class<?>,String>();
	
	private static final String DefaultTemplateName_String= "String";
	private static final String DefaultTemplateName_Enum= "Enum";
	private static final String DefaultTemplateName_Boolean= "Boolean";
	private static final String DefaultTemplateName_Date= "Date";
	private static final String DefaultTemplateName_Decimal= "Decimal";
	private static final String DefaultTemplateName_Object= "String";
	private static final String DefaultTemplateName_DictEnum= "DictEnum";
	static{
		typeToTemplateNameMap.put(boolean.class, DefaultTemplateName_Boolean);
		typeToTemplateNameMap.put(Boolean.class, DefaultTemplateName_Boolean);
		typeToTemplateNameMap.put(BigDecimal.class, DefaultTemplateName_Decimal);
		typeToTemplateNameMap.put(Date.class, DefaultTemplateName_Date);
		typeToTemplateNameMap.put(String.class, DefaultTemplateName_String);
		typeToTemplateNameMap.put(Object.class, DefaultTemplateName_Object);
		typeToTemplateNameMap.put(DictEnum.class, DefaultTemplateName_DictEnum);
	}
	//element value is the virtual path for the template file
	private static List<String> templateFilesCache=new ArrayList<String>();
	//key is try path and value is element index in templateFilesCache
	private static Map<String,Integer> controllerTemplateCache=new Hashtable<String,Integer>();

	private String template;
	
	public String getTemplate() {
		return template;
	}
	/**
	 * value can be absolute path or just a filename without path and 
	 * extension which will be search under controller or shared folder and appended ".jsp"
	 * @param value
	 */
	public void setTemplate(String value) {
		this.template = value;
	}
	
	private Object additionalViewData;
	public Object getAdditionalViewData() {
		return additionalViewData;
	}

	public void setAdditionalViewData(Object value) {
		this.additionalViewData = value;
	}

	protected abstract String GetTemplateFolderName() ;
	
	protected String autogenerateId() throws JspException {
		return autogenerateId(super.autogenerateId());
	}
	@Override
	protected void writeDefaultAttributes(TagWriter tagWriter) throws JspException {
	}
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		Class<?> modelclass4template=getBindStatus().getValueType();
		Map<String,Object> templateviewdata=new HashMap<String,Object>();
		templateviewdata.put("htmlFieldId", autogenerateId());
		templateviewdata.put("htmlFieldName", getName());
		templateviewdata.put("modelPropertyPath", getPath());
		templateviewdata.put("modelType", modelclass4template);
		templateviewdata.put("model", getBindStatus().getActualValue());
		templateviewdata.put("modelMetadata", GetModelMetadata(pageContext, getPath()));
		templateviewdata.put("additionalViewData", additionalViewData);
		setTemplateViewData(templateviewdata);//give subclass a chance to set TemplateViewData
		pageContext.setAttribute("TemplateViewData", templateviewdata,PageContext.REQUEST_SCOPE);
		
		try {
			String templatePath=SearchTemplateFile(modelclass4template);
			pageContext.include(templatePath);
		} catch (Exception e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
	protected void setTemplateViewData(Map<String,Object> templateviewdata){
		
	}
	private String SearchTemplateFile(Class<?> modelclass4template) throws JspException{
		Integer cachedIndex;
		
		if(!StringUtils.isEmpty(template) && template.startsWith("/")) {
			cachedIndex=FindFile(template);
			if(cachedIndex!=null) return templateFilesCache.get(cachedIndex);
			throw new JspException("Template not found: searched "+template);
		}

		if(!StringUtils.isEmpty(template)){
			return SearchByController(template);
		}
		Action action=(Action)pageContext.getAttribute(GlobalConstant.Attr_RequestAction,PageContext.REQUEST_SCOPE);
		String pathByController=action.getAreaName()+"/"+action.getControllerName();
		String templatePath=SearchTemplateFileByType(modelclass4template,"/"+pathByController+"/"+GetTemplateFolderName(),"/"+pathByController+"/"+GetTemplateFolderName(),null);
		if(templatePath!=null) return templatePath;
		templatePath= SearchTemplateFileByType(modelclass4template,"/"+GlobalFolderName+"/"+GetTemplateFolderName(),"/"+pathByController+"/"+GetTemplateFolderName(),null);
		if(templatePath!=null) return templatePath;
		throw new JspException("Template not found: searched by type"+modelclass4template.getName()+templatePath);
	}

	private String SearchTemplateFileByType(Class<?> modelclass4template,String folder,String keyFolder,String keyTemplateNameByType){
		if(modelclass4template==null) return null;
		String templateName="";
		if(typeToTemplateNameMap.containsKey(modelclass4template)){
			templateName=typeToTemplateNameMap.get(modelclass4template);
		}else if(modelclass4template.isEnum()){
			templateName=DefaultTemplateName_Enum;
		}else if(modelclass4template.isPrimitive()){
			templateName=DefaultTemplateName_String;
		}else{
			templateName=modelclass4template.getSimpleName();
		}
		if(keyTemplateNameByType==null) keyTemplateNameByType=templateName;
		String cacheKey=keyFolder+"/"+keyTemplateNameByType+".jsp";
		Integer cachedIndex=controllerTemplateCache.get(cacheKey);
		if(cachedIndex!=null)return templateFilesCache.get(cachedIndex);
		cachedIndex=FindFile(folder+"/"+templateName+".jsp");
		if(cachedIndex!=null) {
			controllerTemplateCache.put(cacheKey,cachedIndex);
			return templateFilesCache.get(cachedIndex);
		}
		return SearchTemplateFileByType(modelclass4template.getSuperclass(),folder,keyFolder,keyTemplateNameByType);
	}

	private String SearchByController(String templateName) throws JspException{
		Action action=(Action)pageContext.getAttribute(GlobalConstant.Attr_RequestAction,PageContext.REQUEST_SCOPE);
		String pathByController=action.getAreaName()+"/"+action.getControllerName();
		String searchedPath="";
		String tryPath="";
		String keyPath="";
		String templatePath="";
		tryPath="/"+pathByController+"/"+GetTemplateFolderName()+"/"+template+".jsp";
		keyPath=tryPath;
		Integer cachedIndex=controllerTemplateCache.get(keyPath);
		if(cachedIndex!=null)return templateFilesCache.get(cachedIndex);
		cachedIndex=FindFile(tryPath);
		if(cachedIndex!=null) {
			controllerTemplateCache.put(keyPath,cachedIndex);
			return templateFilesCache.get(cachedIndex);
		}
		searchedPath=searchedPath+tryPath+"\n";
		tryPath="/"+GlobalFolderName+"/"+GetTemplateFolderName()+"/"+template+".jsp";
		cachedIndex=FindFile(tryPath);
		if(cachedIndex!=null) {
			controllerTemplateCache.put(keyPath,cachedIndex);
			return templateFilesCache.get(cachedIndex);
		}
		searchedPath=searchedPath+tryPath+"\n";
		throw new JspException("Template not found: searched:\n "+searchedPath);
	}
	private Integer FindFile(String virtualPath){
		if(StringUtils.isEmpty(virtualPath)) throw new JavaArchException("templatePath is empty");
		if(templateFilesCache.contains(virtualPath)) return templateFilesCache.indexOf(virtualPath);
		String realpath=pageContext.getServletContext().getRealPath(virtualPath);
		if(Files.exists(Paths.get(realpath))){
			templateFilesCache.add(virtualPath);
			return templateFilesCache.indexOf(virtualPath);
		}
		return null;
	}

}


	


