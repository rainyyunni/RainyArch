package projectbase.mvc.taglib.angular;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.springframework.web.servlet.tags.form.TagWriter;

import projectbase.mvc.GlobalConstant;


@SuppressWarnings("serial")
public class PasswordInputTag extends org.springframework.web.servlet.tags.form.PasswordInputTag implements BaseHtmlTag{
	private String name;
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
		tagWriter.writeAttribute("type", getType());

		if(pageContext.getAttribute(GlobalConstant.Attr_ViewModelJson,PageContext.REQUEST_SCOPE)==null)
			writeValue(tagWriter);
	
		if(this.getDynamicAttributes()==null || this.getDynamicAttributes().get("ng-model")==null){
			tagWriter.writeAttribute("ng-model", GetNgModel(pageContext,getPath()));
		}
		if(this.getDynamicAttributes()==null || this.getDynamicAttributes().get("ng-model-options")==null){
			tagWriter.writeAttribute("ng-model-options","{ updateOn: 'blur' }");
		}
		
		AddClientValidation(pageContext,tagWriter,getPath());
		tagWriter.endTag();
		return SKIP_BODY;
	}
}
