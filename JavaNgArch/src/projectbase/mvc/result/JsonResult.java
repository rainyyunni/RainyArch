package projectbase.mvc.result;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import projectbase.mvc.GlobalConstant;
import projectbase.utils.ArgumentNullException;
import projectbase.utils.InvalidOperationException;

public class JsonResult extends ActionResult {

	private Object data;

	public Object getData() {
		return data;
	}
	public void setData(Object value) {
		this.data = value;
	}
	public String getContentEncoding() {
		return contentEncoding;
	}
	public void setContentEncoding(String value) {
		this.contentEncoding = value;
	}
	@Override
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String value) {
		this.contentType = value;
	}
	private String contentEncoding=GlobalConstant.Encoding_Default;

	private String contentType="application/json";
    private JsonRequestBehavior jsonRequestBehavior;

    public JsonRequestBehavior getJsonRequestBehavior() {
		return jsonRequestBehavior;
	}
	public void setJsonRequestBehavior(JsonRequestBehavior value) {
		this.jsonRequestBehavior = value;
	}	
    public JsonResult() {
    	this.setView(this);
        this.jsonRequestBehavior = JsonRequestBehavior.DenyGet;
    }
    public JsonResult(Object data) {
    	this.setView(this);
        this.jsonRequestBehavior = JsonRequestBehavior.DenyGet;
        this.data =data;
    }

    @Override
	public  void ExecuteResult(ControllerContext context) throws IOException {
        if (context == null) {
            throw new ArgumentNullException("context");
        }
        if (jsonRequestBehavior == JsonRequestBehavior.DenyGet &&
            context.getRequest().getMethod().equalsIgnoreCase("GET")) {
            throw new InvalidOperationException("MvcResources.JsonRequest_GetNotAllowed");
        }

        HttpServletResponse response = context.getResponse();

        if (!StringUtils.isEmpty(contentType)) {
            response.setContentType(contentType);
        }
        else {
        	response.setContentType("application/json");
        }
        if (contentEncoding != null) {
            response.setCharacterEncoding(contentEncoding);
        }
        if (data != null) {
            JavaScriptSerializer serializer = new JavaScriptSerializer();
            response.getWriter().write(serializer.Serialize(data));
            response.getWriter().flush();
        }
    }
}
