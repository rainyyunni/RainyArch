
package projectbase.mvc.result;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import projectbase.mvc.GlobalConstant;
import projectbase.utils.ArgumentNullException;

   public class ContentResult  extends  ActionResult
    {
		
		private String content;

		public String getContent() {
			return content;
		}
		public void setContent(String value) {
			this.content = value;
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

		private String contentType="text/html; charset=utf-8";
		
		public ContentResult(){
			this.setView(this);
		}
		public ContentResult(String content){
			this.setView(this);
			this.content=content;
		}
		@Override
        public void ExecuteResult(ControllerContext context) throws IOException {
        	if (context == null)
            {
                throw new ArgumentNullException("context");
            }

            HttpServletResponse response = context.getResponse();

            if (!StringUtils.isEmpty(contentType)) {
                response.setContentType(contentType);
            }
            if (contentEncoding != null) {
                response.setCharacterEncoding(contentEncoding);
            }
            if (content != null) {
            	response.getWriter().write(content);
            	response.getWriter().flush();
            }
        }
    }


