package projectbase.mvc.taglib;

import javax.servlet.jsp.JspException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import projectbase.mvc.AjaxHelperExtension;
import projectbase.mvc.GlobalConstant;
import projectbase.mvc.UrlHelper;


@SuppressWarnings("serial")
public class AjaxFormTag extends org.springframework.web.servlet.tags.form.AbstractHtmlElementTag{
	private static final String FORM_TAG = "form";
	private TagWriter tagWriter;
	private String ajaxAction;
	private String ajaxController;
	private String updateTargetId;
	private Boolean forViewModelOnly;
	public Boolean getForViewModelOnly() {
		return forViewModelOnly;
	}
	public void setForViewModelOnly(Boolean value) {
		this.forViewModelOnly = value;
	}
	public String getUpdateTargetId() {
		return updateTargetId;
	}
	public void setUpdateTargetId(String value) {
		this.updateTargetId = value;
	}
	public String getAjaxController() {
		return ajaxController;
	}
	public void setAjaxController(String value) {
		this.ajaxController = value;
	}
	public String getAjaxAction() {
		return ajaxAction;
	}
	public void setAjaxAction(String value) {
		this.ajaxAction = value;
	}
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {

		this.tagWriter = tagWriter;

		tagWriter.startTag(FORM_TAG);
		
		String id=getId();
		tagWriter.writeOptionalAttributeValue("id", id);
		UrlHelper urlHelper=(UrlHelper)pageContext.getRequest().getAttribute(GlobalConstant.Attr_UrlHelper);
		AjaxHelperExtension.AjaxOptionsForAction(urlHelper, tagWriter,ajaxAction,ajaxController,updateTargetId,forViewModelOnly);	

		tagWriter.writeOptionalAttributeValue("method", "post");
		if (!CollectionUtils.isEmpty(getDynamicAttributes())) {
			for (String attr : getDynamicAttributes().keySet()) {
				tagWriter.writeOptionalAttributeValue(attr, getDisplayString(getDynamicAttributes().get(attr)));
			}
		}
		
		tagWriter.forceBlock();

	
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		this.tagWriter.endTag();
		return EVAL_PAGE;
	}
}
