
package projectbase.mvc.result;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import projectbase.utils.ArgumentNullException;

   public class JavaScriptResult  extends  ActionResult
    {

        protected String script;

        public String getScript() {
			return script;
		}
		public void setScript(String value) {
			this.script = value;
		}
		public JavaScriptResult()
        {
			this.setView(this);
        }
		public JavaScriptResult(String script)
        {
			this.setView(this);
			this.script = script;
        }
		
		@Override
		public String getContentType() {
			return "application/x-javascript";
		}
		@Override
        public void ExecuteResult(ControllerContext context) throws IOException {
            if (context == null) {
                throw new ArgumentNullException("context");
            }

            HttpServletResponse response = context.getResponse();
            response.setContentType("application/x-javascript");
            response.setCharacterEncoding("UTF-8");

            if (script != null) {
            	response.getWriter().write(script);
            	response.getWriter().flush();
            }
        }
    }


