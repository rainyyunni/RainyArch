package projectbase.mvc.taglib;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.form.TagWriter;


@SuppressWarnings("serial")
public class HiddenInputTag extends org.springframework.web.servlet.tags.form.HiddenInputTag implements BaseHtmlTag{

	protected String autogenerateId() throws JspException {
		return autogenerateId(super.autogenerateId());
	}
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		tagWriter.startTag("input");
		writeDefaultAttributes(tagWriter);
		tagWriter.writeAttribute("type", "hidden");
		if (isDisabled()) {
			tagWriter.writeAttribute(DISABLED_ATTRIBUTE, "disabled");
		}
		String value = getDisplayString(getBoundValue(), getPropertyEditor());
		tagWriter.writeAttribute("value", processFieldValue(getName(), value, "hidden"));
		
		AddClientValidation(pageContext,tagWriter,getPath());
		tagWriter.endTag();
		return SKIP_BODY;
	}
}
