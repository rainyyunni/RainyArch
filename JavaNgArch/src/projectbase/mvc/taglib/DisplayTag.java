package projectbase.mvc.taglib;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.support.BindStatus;

import projectbase.mvc.ModelMetadata;
import projectbase.utils.InvalidOperationException;


@SuppressWarnings("serial")
public class DisplayTag extends BaseTemplateTag{

	
	@Override
	protected String GetTemplateFolderName(){
		return "DisplayTemplates";
	}
	@Override
	protected void setTemplateViewData(Map<String,Object> templateviewdata) {
		ModelMetadata meta=(ModelMetadata)templateviewdata.get(Key_ModelMetadata);
		if(meta.getDisplayMethod()==null) return;
			
		BindStatus bs;
		try {
			bs = new BindStatus(getRequestContext(), getPath().substring(0, getPath().lastIndexOf(".")), false);
		} catch (IllegalStateException | JspException ex) {
			throw new InvalidOperationException("Path failure due to exception:"+ex.getMessage());
		}
		if(bs.getActualValue()==null) return;
		String displayText;
		try {
			displayText = (String)meta.getDisplayMethod().invoke(bs.getActualValue());
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new InvalidOperationException("Display method was executed unsuccessfully due to exception:"+e.getMessage());
		}
		templateviewdata.put(Key_Model, displayText);
		templateviewdata.put(Key_ModelType, String.class);
	}
}


	


