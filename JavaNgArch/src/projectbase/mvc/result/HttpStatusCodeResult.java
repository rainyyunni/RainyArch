package projectbase.mvc.result;

import java.io.IOException;

import projectbase.utils.ArgumentNullException;

public class HttpStatusCodeResult extends ActionResult{
	private int statusCode;
	private String statusDescription;
	
    public HttpStatusCodeResult(int statusCode)     {
    	this(statusCode,null);
}

public HttpStatusCodeResult(int statusCode, String statusDescription) {
	this.setView(this);
    this.setStatusCode(statusCode);
    this.setStatusDescription(statusDescription);
}

public void ExecuteResult(ControllerContext context) {
    if (context == null) {
        throw new ArgumentNullException("context");
    }

    context.getResponse().setStatus(statusCode);
    try {
		context.getResponse().sendError(statusCode, statusDescription);
	} catch (IOException e) {
	}
}

public int getStatusCode() {
	return statusCode;
}

public void setStatusCode(int statusCode) {
	this.statusCode = statusCode;
}

public String getStatusDescription() {
	return statusDescription;
}

public void setStatusDescription(String statusDescription) {
	this.statusDescription = statusDescription;
}
}
