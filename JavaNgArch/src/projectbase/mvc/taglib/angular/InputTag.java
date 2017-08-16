package projectbase.mvc.taglib.angular;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;
import projectbase.mvc.GlobalConstant;

@SuppressWarnings("serial")
public class InputTag extends org.springframework.web.servlet.tags.form.InputTag implements BaseHtmlTag{

	private String name;
	
	@Override
	protected String autogenerateId() throws JspException {
		return autogenerateId(super.autogenerateId());
	}
	@Override
	protected String getName() throws JspException {
		return getName(name,getPropertyPath(),this.getDynamicAttributes());
	}
	public void setName(String value)  {
		name=value;
	}
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		tagWriter.startTag("input");

		writeDefaultAttributes(tagWriter);

		if(pageContext.getAttribute(GlobalConstant.Attr_ViewModelJson,PageContext.REQUEST_SCOPE)==null)
			writeValue(tagWriter);

		if(this.getDynamicAttributes()==null || this.getDynamicAttributes().get("ng-model")==null){
			tagWriter.writeAttribute("ng-model", GetNgModel(pageContext,getPath()));
		}

		String type=AddClientValidation(pageContext,tagWriter,getPath());
		if(type!=null && !type.equalsIgnoreCase("number")){
			tagWriter.writeAttribute("type", getType());
		}
		if(type!=null && type.equalsIgnoreCase("date")){
			tagWriter.writeAttribute("uib-datepicker-popup", "yyyy-MM-dd");
			if(this.getDynamicAttributes()==null || this.getDynamicAttributes().get("pb-datepicker-button")==null){
				tagWriter.writeAttribute("pb-datepicker-button","yes");
			}		
		}else if(this.getDynamicAttributes()==null || this.getDynamicAttributes().get("ng-model-options")==null){
			tagWriter.writeAttribute("ng-model-options","{ updateOn: 'blur' }");
		}
		tagWriter.endTag();
		return SKIP_BODY;
	}


}
