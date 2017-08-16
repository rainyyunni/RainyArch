package projectbase.mvc.taglib;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

public class ErrorsTag extends org.springframework.web.servlet.tags.form.ErrorsTag implements BaseHtmlTag{
	private String forName;
	
	public String getForName() {
		return forName;
	}
	public void setForName(String forName) {
		this.forName = forName;
	}
	@Override
	protected String autogenerateId() throws JspException {
		return null;
	}
	@Override
	protected boolean shouldRender() throws JspException {
		return true;
	}
	@Override
	protected void writeDefaultAttributes(TagWriter tagWriter) throws JspException {
		super.writeDefaultAttributes(tagWriter);
		writeOptionalAttributes(tagWriter);
		if(getPath()=="*"){
			writeOptionalAttribute(tagWriter,"class", "validation-summary-valid");
			writeOptionalAttribute(tagWriter,"data-valmsg-summary", "true");	
		}else{
			writeOptionalAttribute(tagWriter,"class", "field-validation-valid");
			writeOptionalAttribute(tagWriter,"data-valmsg-replace","true");
			String msgFor=forName;
			if(StringUtils.isEmpty(forName))msgFor=StringUtils.removeEndIgnoreCase(super.autogenerateId(), ".errors");
			writeOptionalAttribute(tagWriter,"data-valmsg-for", msgFor);
		}
		
	}
}
