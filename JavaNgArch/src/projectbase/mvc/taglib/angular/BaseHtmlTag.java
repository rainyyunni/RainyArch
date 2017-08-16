package projectbase.mvc.taglib.angular;

import java.beans.IntrospectionException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.AbstractFormTag;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import projectbase.mvc.AngularClientValidationProvider;
import projectbase.mvc.AngularModelMetadataProvider;
import projectbase.mvc.GlobalConstant;
import projectbase.mvc.ModelMetadata;
import projectbase.mvc.ModelMetadataProvider;
import projectbase.mvc.validation.angular.ModelClientValidationRule;
import projectbase.utils.JavaArchException;

/**
 * html tag to use instead of spring form tags with differences such as
 * 1.id is like xx_yy_zz
 * 2.only value-related attributes are accept as server attributes,otherwise just be treated as a client attribute which is directly output as it is.
 * 3.access to Object related to request such as Model,metadata,helper,etc
 * @author tudoubaby
 */
@SuppressWarnings("serial")
public interface BaseHtmlTag {
	default public String autogenerateId(String baseString) throws JspException {
		return null;
//		if(baseString==null)return null;
//		return baseString.replace('.', '_');
	}
	default public String getName(String baseString,String propertyPath,Map<String,Object> tagDynamicAttributes) throws JspException {
		if(StringUtils.isNotEmpty(baseString))return baseString;
		String nosubmit=baseString==null?"":GlobalConstant.UnSubmitNameMarker;
		if(tagDynamicAttributes!=null &&tagDynamicAttributes.get("ng-model")!=null){
			String nameExpr=(String)tagDynamicAttributes.get("ng-model");
			if(nameExpr.indexOf("[")>0){
				String arrayName=nameExpr.substring(0,nameExpr.indexOf("["));
				nameExpr=nameExpr.replace("[", "[{{").replace("]", "|ReIndex:"+arrayName+"}}]");
			}
			String nameprefix=(String)tagDynamicAttributes.get("pb-name-prefix");
			if(nameprefix==null)nameprefix="c.vm";
			if(!nameprefix.endsWith("."))nameprefix=nameprefix+".";
			return nosubmit+nameExpr.substring(nameprefix.length());
		}
		return nosubmit+propertyPath;
	}
	
	default public String GetNgModel(PageContext context,String path) throws JspException {
		return context.getAttribute(GlobalConstant.Attr_VMPrefix,PageContext.PAGE_SCOPE)+"."+path;
	}	

	default public ModelMetadata GetModelMetadata(PageContext context,String propertyPath) throws JspException {
		Object viewmodel=context.getRequest().getAttribute(GlobalConstant.Attr_ViewModel);
		if(propertyPath==GlobalConstant.Attr_ViewModel){
			Class<?> modelClass=context.getRequest().getAttribute(GlobalConstant.Attr_ViewModel).getClass();
			return AngularModelMetadataProvider.getCurrent().GetMetadataForType(modelClass);
		}
		if(propertyPath.startsWith(GlobalConstant.Attr_ViewModel+'.')){
			propertyPath=StringUtils.remove(propertyPath, GlobalConstant.Attr_ViewModel+'.');
			try {
				return AngularModelMetadataProvider.getCurrent().GetMetadataForPropertyPath(viewmodel,propertyPath);
			} catch (JavaArchException e) {
				//throw new JspException("No metadata found for path : "+propertyPath);
			}catch(IntrospectionException ex){
				throw new JspException(ex);
			}
		}
		return null;
	}
	
	default public String AddClientValidation(PageContext context,TagWriter tagWriter,String propertyPath) throws JspException {
		ModelMetadata meta=GetModelMetadata(context,propertyPath);
		if(meta==null)return null;
		//if(!meta.getRequestValidationEnabled()) return;

		Iterable<ModelClientValidationRule> rules=AngularClientValidationProvider.getCurrent().GetValidationRules(meta);
		if(rules.iterator().hasNext()){
			tagWriter.writeAttribute("translatekey", meta.getDisplayName());
		}
		String type=null;
		for(ModelClientValidationRule rule:rules){
			if(rule.getValidationType().equalsIgnoreCase("date")){
				type="date";
				continue;
			}
			type=rule.getValidationType().equalsIgnoreCase("number")?"number":type;
			tagWriter.writeAttribute(rule.getValidationType(), rule.getValidationParameter().toString());
		}
		return type;
	}

}
