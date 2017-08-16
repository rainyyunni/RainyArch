package projectbase.mvc.result;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import projectbase.mvc.GlobalConstant;
import projectbase.utils.ArgumentNullException;

public class FileResult extends ActionResult {

	private Object content;

	private ContentDisposition disposition;
	private String downLoadFileName;
	private String fileExt;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getDownLoadFileName() {
		return downLoadFileName;
	}

	public void seDownLoadFileName(String value) {
		this.downLoadFileName = value;
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

	private String contentEncoding = GlobalConstant.Encoding_Default;

	private String contentType = "";

	public FileResult() {
		this.setView(this);
	}

	public FileResult(Object content, String fileExt, String downLoadFileName) {
		this.setView(this);
		this.content=content;
		this.setFileExt(fileExt);
		this.downLoadFileName = downLoadFileName;
	}

	@Override
	public void ExecuteResult(ControllerContext context) throws IOException {
		if (context == null) {
			throw new ArgumentNullException("context");
		}
		HttpServletResponse response = context.getResponse();
		HttpServletRequest request = context.getRequest();
		response.reset();
		
		if (StringUtils.isEmpty(contentType)) {
			if (fileExt == ".pdf")
				contentType = "application/pdf";
			else if (fileExt == ".doc")
				contentType = "application/ms-word";
	        else if (fileExt == "")
	        	contentType = "application/octet-stream";
			else
				contentType = "image/" + fileExt.substring(1);
		} 
		response.setContentType(contentType);
		if (contentEncoding != null) {
			response.setCharacterEncoding(contentEncoding);
		}

		if (request.getHeader("User-Agent").toLowerCase().indexOf("msie") > -1) {
			downLoadFileName = URLEncoder.encode(downLoadFileName, "UTF-8");
		}
        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > -1)
        {
        	response.addHeader("Content-Disposition", "inline;filename=\"" + downLoadFileName + fileExt + "\"");
        }
        else
        {
        	response.addHeader("Content-Disposition", "inline;filename=" + downLoadFileName + fileExt);
        }
		if (content instanceof byte[]) {
			response.setContentLength(((byte[]) content).length);
			response.getOutputStream().write((byte[]) content);
			response.getOutputStream().flush();
		} else if (content instanceof String) {
			response.setContentLength(((String) content).getBytes().length);
			response.getWriter().write((String) content);
			response.getWriter().flush();
		}
	}

	public ContentDisposition getDisposition() {
		return disposition;
	}

	public void setDisposition(ContentDisposition disposition) {
		this.disposition = disposition;
	}

	public enum ContentDisposition {
		DownLoad, Open
	}
}
