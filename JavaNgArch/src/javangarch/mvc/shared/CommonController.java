package javangarch.mvc.shared;

import java.nio.file.Files;
import java.nio.file.Paths;

import javangarch.domain.bdinterface.IAdminBD;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.HandlerMethod;

import projectbase.data.UtilQuery;
import projectbase.mvc.AjaxHelperExtension;
import projectbase.mvc.PreHandleResultException;
import projectbase.mvc.result.ActionResult;
import projectbase.mvc.result.HttpStatusCodeResult;
import projectbase.utils.Util;
    public class CommonController extends AppBaseController
    {
        @Resource public IAdminBD AdminBD;
        
        public ActionResult FuncList()
        {
        	String html=Util.FuncTree();
            return ForView("/Shared/Directive/FuncList",html);
        }
        public ActionResult AuthFailure()
        {
            return super.AuthFailure();
        }
        public ActionResult NgControllerJs(String webroot)
        {
            return JavaScript(Util.NgControllerJs(webroot));
        }
        public ActionResult CheckUnique(String name,String value)
        {
            return Json(name.equalsIgnoreCase(value));
        }
}

