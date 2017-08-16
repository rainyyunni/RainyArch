
package projectbase.mvc.result;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import projectbase.utils.ArgumentNullException;

	/**
	 * 响应回到客户端后，原页面清除后执行javascript
	 * @author test
	 *
	 */
   public class ClientScriptResult  extends  ActionResult
    {

        private String script;

        public String getScript() {
			return script;
		}
		public void setScript(String value) {
			this.script = value;
		}
		public ClientScriptResult(String script)
        {
			this.setView(this);
            this.script = "<script type='text/javascript' language='javascript'>" + script + "</script>";
        }
		@Override
        public  void ExecuteResult(ControllerContext context) throws IOException
        {
            if (context == null)
            {
                throw new ArgumentNullException("context");
            }

            HttpServletResponse response = context.getResponse();

            if (script != null)
            {
            	response.getWriter().write(script);
            	response.getWriter().flush();
            }
        }
    }


