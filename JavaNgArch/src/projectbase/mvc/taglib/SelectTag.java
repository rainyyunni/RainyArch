package projectbase.mvc.taglib;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;


@SuppressWarnings("serial")
public class SelectTag extends org.springframework.web.servlet.tags.form.SelectTag implements BaseHtmlTag{

	private String name;
	
	protected String autogenerateId() throws JspException {
		return autogenerateId(super.autogenerateId());
	}
	@Override
	public String getName() throws JspException {
		
		return StringUtils.isEmpty(name)?super.getName():name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
