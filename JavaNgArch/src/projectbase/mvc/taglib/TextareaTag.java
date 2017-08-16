package projectbase.mvc.taglib;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.form.TagWriter;


@SuppressWarnings("serial")
public class TextareaTag extends org.springframework.web.servlet.tags.form.TextareaTag implements BaseHtmlTag{

	protected String autogenerateId() throws JspException {
		return autogenerateId(super.autogenerateId());
	}
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		tagWriter.startTag("textarea");
		writeDefaultAttributes(tagWriter);
		writeOptionalAttribute(tagWriter, ROWS_ATTRIBUTE, getRows());
		writeOptionalAttribute(tagWriter, COLS_ATTRIBUTE, getCols());
		writeOptionalAttribute(tagWriter, ONSELECT_ATTRIBUTE, getOnselect());
		String value = getDisplayString(getBoundValue(), getPropertyEditor());
		tagWriter.appendValue(processFieldValue(getName(), value, "textarea"));
		AddClientValidation(pageContext,tagWriter,getPath());
		tagWriter.endTag();
		return SKIP_BODY;
	}
}
