package projectbase.mvc.result;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.tags.form.TagWriter;

import projectbase.mvc.GlobalConstant;
import projectbase.utils.ArgumentNullException;
import projectbase.utils.Res;
/**
 * 配合ajax框架通过返回脚本将结果在客户端显示，成功或错误的消息。
 * 服务器响应回到客户端后不改变页面显示，但执行javascript
 * @author tudoubaby
 *
 */
public class AjaxScriptResult extends JavaScriptResult {

	private String messageTargetId;
	private String message;
	private BindException validationException;

	public String getMessageTargetId() {
		return messageTargetId;
	}

	public void setMessageTargetId(String value) {
		this.messageTargetId = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String value) {
		this.message = value;
	}

	public AjaxScriptResult() {
		super();
		messageTargetId = "MessageLine";
	}

	public AjaxScriptResult(String message) {
		this();
		this.message = message;
	}

	@Override
	public void ExecuteResult(ControllerContext context) throws IOException {
		if (context == null) {
			throw new ArgumentNullException("context");
		}

		if (script == null) {

			if (StringUtils.isEmpty(message))
				message = (String) context.getRequest().getAttribute(
						GlobalConstant.Attr_ViewMessage);
			if (StringUtils.isEmpty(message)) message="";
			//script = "$('#" + messageTargetId + "').show();$('#"
			//		+ messageTargetId + "').html('" + message
			//		+ ValidationSummary(context) + "');";
			script ="ProjectBase_WebClientAppRoot.pb.ShowMessage(null,'"+message
					+ ValidationSummary(context)+ "');";
		}
		super.ExecuteResult(context);
	}

	public String ValidationSummary(ControllerContext context) {
		if(validationException==null) return "";
		List<ObjectError> errors = validationException.getAllErrors();
		StringWriter htmlSummary = new StringWriter();
		TagWriter tagWriter = new TagWriter(htmlSummary);
		try {
			tagWriter.startTag("div");
			tagWriter.writeAttribute("class", "testcss");
			tagWriter.startTag("ul");
			for (ObjectError modelError : errors) {
				String errorText =Res.ValidationMessages(modelError.getCode(),modelError.getArguments());
				if (!StringUtils.isEmpty(errorText)) {
					tagWriter.startTag("li");
					tagWriter.appendValue(errorText);
					if(errorText.startsWith("ValidationMessages:")){
						tagWriter.appendValue(modelError.getDefaultMessage());
					}
				}
			}
			tagWriter.endTag();
			tagWriter.endTag();
			tagWriter.endTag();
		} catch (Exception e) {

		}

		return htmlSummary.toString();

	}

	public BindException getValidationException() {
		return validationException;
	}

	public void setValidationException(BindException value) {
		this.validationException = value;
	}
}
