package projectbase.mvc.taglib;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.form.TagWriter;


@SuppressWarnings("serial")
public class PasswordInputTag extends org.springframework.web.servlet.tags.form.PasswordInputTag implements BaseHtmlTag{

	protected String autogenerateId() throws JspException {
		return autogenerateId(super.autogenerateId());
	}
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		tagWriter.startTag("input");

		writeDefaultAttributes(tagWriter);
		tagWriter.writeAttribute("type", getType());

		writeValue(tagWriter);

		// custom optional attributes
		writeOptionalAttribute(tagWriter, SIZE_ATTRIBUTE, getSize());
		writeOptionalAttribute(tagWriter, MAXLENGTH_ATTRIBUTE, getMaxlength());
		writeOptionalAttribute(tagWriter, ALT_ATTRIBUTE, getAlt());
		writeOptionalAttribute(tagWriter, ONSELECT_ATTRIBUTE, getOnselect());
		writeOptionalAttribute(tagWriter, AUTOCOMPLETE_ATTRIBUTE, getAutocomplete());

		AddClientValidation(pageContext,tagWriter,getPath());
		tagWriter.endTag();
		return SKIP_BODY;
	}
}
