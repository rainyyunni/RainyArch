package projectbase.mvc.taglib;

import java.beans.IntrospectionException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import projectbase.mvc.ClientValidationProvider;
import projectbase.mvc.GlobalConstant;
import projectbase.mvc.ModelMetadata;
import projectbase.mvc.ModelMetadataProvider;
import projectbase.mvc.validation.ModelClientValidationRule;
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
		if(baseString==null)return null;
		return baseString.replace('.', '_');
	}
	
	default public ModelMetadata GetModelMetadata(PageContext context,String propertyPath) throws JspException {
		Object viewmodel=context.getRequest().getAttribute(GlobalConstant.Attr_ViewModel);
		if(propertyPath==GlobalConstant.Attr_ViewModel){
			Class<?> modelClass=context.getRequest().getAttribute(GlobalConstant.Attr_ViewModel).getClass();
			return ModelMetadataProvider.getCurrent().GetMetadataForType(modelClass);
		}
		if(propertyPath.startsWith(GlobalConstant.Attr_ViewModel+'.')){
			propertyPath=StringUtils.remove(propertyPath, GlobalConstant.Attr_ViewModel+'.');
			try {
				return ModelMetadataProvider.getCurrent().GetMetadataForPropertyPath(viewmodel,propertyPath);
			} catch (JavaArchException e) {
				//throw new JspException("No metadata found for path : "+propertyPath);
			}catch(IntrospectionException ex){
				throw new JspException(ex);
			}
		}
		return null;
		

	}
	
	default public void AddClientValidation(PageContext context,TagWriter tagWriter,String propertyPath) throws JspException {
		ModelMetadata meta=GetModelMetadata(context,propertyPath);
		if(meta==null)return;
		//if(!meta.getRequestValidationEnabled()) return;

		Iterable<ModelClientValidationRule> rules=ClientValidationProvider.getCurrent().GetValidationRules(meta);
		if(rules.iterator().hasNext()){
			tagWriter.writeAttribute("data-val","true");
		}
		for(ModelClientValidationRule rule:rules){
			tagWriter.writeAttribute("data-val-"+rule.getValidationType(), rule.getErrorMessage());
			for(String name:rule.getValidationParameters().keySet()){
				tagWriter.writeAttribute("data-val-"+rule.getValidationType()+"-"+name, rule.getValidationParameters().get(name).toString());
			}
		}
	}

}
