

package projectbase.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import projectbase.mvc.result.ActionResult;
import projectbase.mvc.result.HttpStatusCodeResult;
import projectbase.mvc.result.JsonResult;
import projectbase.mvc.result.RichClientJsonResult;
import projectbase.mvc.result.ViewResult;

    /// <summary>
    /// check user's priviledges to accessing actions.
    /// you can assign funccode to each action,or the default value for funccode is controllername.actionname 
    /// </summary>
    public abstract class BaseAuthParser extends AnnotationParserHandlerInterceptor implements Ordered
    {

        private ActionResult _failureResult;

		public BaseAuthParser()
        {
        }      
		protected abstract boolean IsAuthEnabled();

        @Override
		public boolean preHandle(HttpServletRequest request,
				HttpServletResponse response, Object handler) throws Exception {
        	if (!IsAuthEnabled()) return true;
        	String actioncode= getControllerActionName(request,response,handler);

        	ActionResult action = CheckLogin(request,response,handler,actioncode);
            if (action!=null){
                _failureResult = action;
                String qs=request.getQueryString();
                int pos=qs.indexOf(AjaxHelperExtension.Key_For_ForViewModelOnly);
                if(pos>=0){
                	int pos1=qs.indexOf("&",pos);
                	if(pos1>=0){
                		qs=qs.substring(0, pos)+qs.substring(pos1);
                	}else{
                		qs=qs.substring(0, pos);
                	}
                }
                request.getSession().setAttribute(GlobalConstant.Attr_LastRequestBeforeLogin, request.getPathInfo()+(qs.isEmpty()?"":"?"+qs));
                HandleUnauthorizedRequest(request,response,handler,actioncode);
            }

            if (CanAccess(request,response,handler,actioncode))
                WriteUserLog(request,response,handler,actioncode);
            else
            {
                _failureResult=new RichClientJsonResult(false,"AuthFailure","AuthFailure");
                HandleUnauthorizedRequest(request,response,handler,actioncode);
            }
            return true;
        }

        protected void HandleUnauthorizedRequest(HttpServletRequest request,
				HttpServletResponse response, Object handler,String actioncode) throws PreHandleResultException
        {
            	throw new PreHandleResultException(_failureResult);
        }

		protected ActionResult CheckLogin(HttpServletRequest request,
				HttpServletResponse response, Object handler,String actioncode)
        {
            return null;
        }

        protected abstract boolean CanAccess(HttpServletRequest request,
				HttpServletResponse response, Object handler,String actioncode);

        protected void WriteUserLog(HttpServletRequest request,
				HttpServletResponse response, Object handler,String actioncode)
        {
        }


		@Override
		public int getOrder() {
			return 0;
		}
       
    }

