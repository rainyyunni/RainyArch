package projectbase.mvc.taglib.angular;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import com.mysql.jdbc.StringUtils;

import projectbase.mvc.AjaxHelperExtension;
import projectbase.mvc.GlobalConstant;
import projectbase.mvc.UrlHelper;


@SuppressWarnings("serial")
public class AjaxFormTag extends org.springframework.web.servlet.tags.form.AbstractHtmlElementTag{
	private static final String FORM_TAG = "form";
	private TagWriter tagWriter;
	private String ajaxAction;
	private String ajaxController;
	private String method;
	private String name;
	public String getMethod() {
		return method;
	}
	public void setMethod(String value) {
		this.method = value;
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
	public String getName() {
		return name;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {

		this.tagWriter = tagWriter;
		tagWriter.startTag(FORM_TAG);
		
		tagWriter.writeOptionalAttributeValue("id", getId());
		tagWriter.writeOptionalAttributeValue("name", getName());
		tagWriter.writeOptionalAttributeValue("novalidate", "novalidate");
		if(method==null)
			tagWriter.writeOptionalAttributeValue("method", "post");
		else
			tagWriter.writeOptionalAttributeValue("method", method);
		if(ajaxAction!=null){
			UrlHelper urlHelper=(UrlHelper)pageContext.getRequest().getAttribute(GlobalConstant.Attr_UrlHelper);
			tagWriter.writeOptionalAttributeValue("ajax-url", urlHelper.GenerateUrl(ajaxAction, ajaxController,null));
		}
		if (!CollectionUtils.isEmpty(getDynamicAttributes())) {
			for (String attr : getDynamicAttributes().keySet()) {
				tagWriter.writeOptionalAttributeValue(attr, getDisplayString(getDynamicAttributes().get(attr)));
			}
		}
		
		tagWriter.forceBlock();

		String s=StringUtils.isNullOrEmpty(name)?"":(name.contains(".")?name.split("\\.")[0]:"");
		pageContext.setAttribute(GlobalConstant.Attr_VMPrefix,StringUtils.isNullOrEmpty(s)?"c":s,PageContext.PAGE_SCOPE);

	
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		pageContext.removeAttribute(GlobalConstant.Attr_VMPrefix);
		this.tagWriter.endTag();
		return EVAL_PAGE;
	}



}
