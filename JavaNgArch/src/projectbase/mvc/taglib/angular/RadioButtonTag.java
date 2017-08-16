package projectbase.mvc.taglib.angular;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.form.TagWriter;


@SuppressWarnings("serial")
public class RadioButtonTag extends org.springframework.web.servlet.tags.form.RadioButtonTag implements BaseHtmlTag{

	private String name;
	protected String autogenerateId() throws JspException {
		return autogenerateId(super.autogenerateId());
	}
	@Override
	protected String getName() throws JspException {
		return getName(name,getPropertyPath(),this.getDynamicAttributes());
	}
	public void setName(String value)  {
		name=value;
	}
	@Override
	protected void writeTagDetails(TagWriter tagWriter) throws JspException {
		tagWriter.writeAttribute("type", getInputType());

		Class<?> valueType = getBindStatus().getValueType();
		
		Object resolvedValue = evaluate("value", getValue());
		if(resolvedValue==null || resolvedValue.equals("null")){
			tagWriter.writeAttribute("ng-value", "null");
		}else{ 
			if (Boolean.class.equals(valueType) || boolean.class.equals(valueType)) {
				tagWriter.writeAttribute("ng-value", resolvedValue.toString());
			}else{
				tagWriter.writeAttribute("ng-value", "'"+resolvedValue+"'");
			}
			renderFromValue(resolvedValue, tagWriter);
		}
		if(this.getDynamicAttributes()==null || this.getDynamicAttributes().get("ng-model")==null){
			tagWriter.writeAttribute("ng-model", GetNgModel(pageContext,getPath()));
		}
	}
}
