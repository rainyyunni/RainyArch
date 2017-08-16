package projectbase.mvc.taglib.angular;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import projectbase.mvc.Action;
import projectbase.mvc.GlobalConstant;
import projectbase.mvc.result.JavaScriptSerializer;
import projectbase.utils.ProjectHierarchy;

public class ViewDataTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String model;
	private String pageTitle;
	private String uiTitle;
	private boolean serialize;
	
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
	public boolean isSerialize() {
		return serialize;
	}
	public void setSerialize(boolean value) {
		this.serialize = value;
	}
	@Override
    public int doStartTag() throws JspException {
		
		PageContext ct=this.pageContext;
		Object viewmodel=ct.getAttribute(GlobalConstant.Attr_ViewModel,PageContext.REQUEST_SCOPE);
		if(viewmodel!=null) {
			String modelClassName =viewmodel.getClass().getSimpleName();
			if(!model.equals(modelClassName)){
				throw new JspException("viewdata tag error : This page declares view model class as ["+model+"] but got a object of class [" +modelClassName+"]");
			}
		}else{
			String fullclassname=model;
			if(!fullclassname.contains(".")){
				Action action=(Action)ct.getAttribute(GlobalConstant.Attr_RequestAction,PageContext.REQUEST_SCOPE);
				fullclassname=ProjectHierarchy.getMvcNS()+"."+action.getAreaName()+"."+model;
			}
			try {
				viewmodel=Class.forName(fullclassname).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new JspException("viewdata tag error : This page declares an invalid view model class as ["+model+"]",e);
			}

			ct.setAttribute(GlobalConstant.Attr_ViewModel,viewmodel,PageContext.REQUEST_SCOPE);
		}
		if(viewmodel!=null&&isSerialize()){
			ct.setAttribute(GlobalConstant.Attr_ViewModelJson, new JavaScriptSerializer().Serialize(viewmodel),PageContext.REQUEST_SCOPE);
		}
        return SKIP_BODY;
	}


}
