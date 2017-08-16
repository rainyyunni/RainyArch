package projectbase.mvc.taglib;

import javax.servlet.jsp.JspException;


@SuppressWarnings("serial")
public class CheckboxTag extends org.springframework.web.servlet.tags.form.CheckboxTag implements BaseHtmlTag{

	protected String autogenerateId() throws JspException {
		return autogenerateId(super.autogenerateId());
	}

}
