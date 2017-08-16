package projectbase.mvc.taglib.angular;

import javax.servlet.jsp.JspException;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.tags.form.TagWriter;


@SuppressWarnings("serial")
public class CheckboxTag extends org.springframework.web.servlet.tags.form.CheckboxTag implements BaseHtmlTag{

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

		Object boundValue = getBoundValue();
		Class<?> valueType = getBindStatus().getValueType();

		if (Boolean.class.equals(valueType) || boolean.class.equals(valueType)) {
			// the concrete type may not be a Boolean - can be String
			if (boundValue instanceof String) {
				boundValue = Boolean.valueOf((String) boundValue);
			}
			Boolean booleanValue = (boundValue != null ? (Boolean) boundValue : Boolean.FALSE);
			renderFromBoolean(booleanValue, tagWriter);
		}else {
			Object value = getValue();
			if (value == null) {
				throw new IllegalArgumentException("Attribute 'value' is required when binding to non-boolean values");
			}
			Object resolvedValue = (value instanceof String ? evaluate("value", value) : value);
			renderFromValue(resolvedValue, tagWriter);
			tagWriter.writeAttribute("ng-true-value", "'"+convertToDisplayString(resolvedValue)+"'");
		}
		if(this.getDynamicAttributes()==null || this.getDynamicAttributes().get("ng-model")==null){
			tagWriter.writeAttribute("ng-model", GetNgModel(pageContext,getPath()));
		}
	}
}
