package projectbase.mvc.taglib.angular;

import javax.servlet.jsp.JspException;
import org.springframework.web.servlet.tags.form.TagWriter;

import projectbase.mvc.ModelMetadata;


@SuppressWarnings("serial")
public class LabelTag extends org.springframework.web.servlet.tags.form.LabelTag implements BaseHtmlTag{

	private String text;
	public String getText() {
		return text;
	}

	public void setText(String value) {
		this.text = value;
	}

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		super.writeTagContent(tagWriter);
		if(text!=null)
			tagWriter.appendValue(text);
		ModelMetadata meta=GetModelMetadata(pageContext,getPath());
		if(meta!=null){
			tagWriter.appendValue("{{'" + meta.getDisplayName() + "'|translate}}");
		}
		else
			tagWriter.appendValue("{{'" + getPath() + "'|translate}}");
		return EVAL_BODY_INCLUDE;
	}

	@Override
	protected String autogenerateFor() throws JspException {
		return autogenerateId(super.autogenerateFor());
	}
	@Override
	protected void writeDefaultAttributes(TagWriter tagWriter) throws JspException {
		super.writeDefaultAttributes(tagWriter);
		writeOptionalAttributes(tagWriter);
		ModelMetadata meta=GetModelMetadata(pageContext,getPath());
		if(meta!=null && meta.getIsRequired()) tagWriter.writeAttribute("pb-required","label");

	}
}
