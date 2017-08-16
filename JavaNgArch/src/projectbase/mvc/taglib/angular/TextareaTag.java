package projectbase.mvc.taglib.angular;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.springframework.web.servlet.tags.form.TagWriter;

import projectbase.mvc.GlobalConstant;


@SuppressWarnings("serial")
public class TextareaTag extends org.springframework.web.servlet.tags.form.TextareaTag implements BaseHtmlTag{

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
		tagWriter.startTag("textarea");
		writeDefaultAttributes(tagWriter);
		writeOptionalAttribute(tagWriter, ROWS_ATTRIBUTE, getRows());
		writeOptionalAttribute(tagWriter, COLS_ATTRIBUTE, getCols());
		writeOptionalAttribute(tagWriter, ONSELECT_ATTRIBUTE, getOnselect());
		
		if(pageContext.getAttribute(GlobalConstant.Attr_ViewModelJson,PageContext.REQUEST_SCOPE)==null){
			String value = getDisplayString(getBoundValue(), getPropertyEditor());
			tagWriter.appendValue(processFieldValue(getName(), value, "textarea"));
		}
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
