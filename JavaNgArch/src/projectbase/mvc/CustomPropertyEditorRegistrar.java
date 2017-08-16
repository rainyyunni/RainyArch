package projectbase.mvc;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;



public final class CustomPropertyEditorRegistrar implements
		PropertyEditorRegistrar {
	
	public void registerCustomEditors(PropertyEditorRegistry registry) {
		//registry.registerCustomEditor(Date.class, new CustomDateEditor(
		//		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		registry.registerCustomEditor(String.class, new StringTrimmerEditor(
				true));

	}

}

