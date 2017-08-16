package projectbase.mvc.taglib;

import javax.servlet.jsp.JspException;


@SuppressWarnings("serial")
public class RadioButtonTag extends org.springframework.web.servlet.tags.form.RadioButtonTag implements BaseHtmlTag{

	protected String autogenerateId() throws JspException {
		return autogenerateId(super.autogenerateId());
	}

}
