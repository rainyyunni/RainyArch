package projectbase.mvc.taglib.angular;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.springframework.web.servlet.tags.form.TagWriter;

import projectbase.mvc.GlobalConstant;


@SuppressWarnings("serial")
public class HiddenInputTag extends org.springframework.web.servlet.tags.form.HiddenInputTag implements BaseHtmlTag{
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
		tagWriter.writeAttribute("type", "hidden");
		if (isDisabled()) {
			tagWriter.writeAttribute(DISABLED_ATTRIBUTE, "disabled");
		}
		
		if(this.getDynamicAttributes()==null || this.getDynamicAttributes().get("ng-model")==null){
			tagWriter.writeAttribute("value","{{"+ GetNgModel(pageContext,getPath())+"}}");
		}else{
			tagWriter.writeAttribute("value","{{"+ this.getDynamicAttributes().get("ng-model")+"}}");
		}
		AddClientValidation(pageContext,tagWriter,getPath());
		tagWriter.endTag();
		return SKIP_BODY;
	}
}
