package projectbase.mvc.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import projectbase.mvc.GlobalConstant;

public class ViewDataTag extends SimpleTagSupport {
	private String model;
	private String pageTitle;
	private String uiTitle;

	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String value) {
		this.pageTitle = value;
	}
	public String getUiTitle() {
		return uiTitle;
	}
	public void setUiTitle(String value) {
		this.uiTitle = value;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String value) {
		this.model = value;
	}

	
	public void doTag() throws JspException, IOException {
		
		JspContext ct=getJspContext();
		Object viewmodel=ct.getAttribute(GlobalConstant.Attr_ViewModel,PageContext.REQUEST_SCOPE);
		if(viewmodel!=null) {
			String modelClassName =viewmodel.getClass().getSimpleName();
			if(!model.equals(modelClassName)){
				throw new JspException("viewdata tag error : This page declares view model class as ["+model+"] but got a object of class [" +modelClassName+"]");
			}
		}
		if(pageTitle!=null || uiTitle!=null)
			ct.getOut().println("<script>RegisterHtmlLoad(function(){App_Common.SetPageUITitle('"+getPageTitle()+"','"+getUiTitle()+"');});</script>");
	}

}
