
package projectbase.mvc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.jsp.JspException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

    public  class AjaxHelperExtension
    {
    	//这里的常量与客户端ProjectBase_Ajax一致以互相配合使用
        public static String ProjectBaseJsPackageNamePrefix = "pb_";
        public static String AuthFailure = "_AuthFailure";
        public static String Key_For_ForViewModelOnly = "_ForViewModelOnly";
        public static String Key_For_ForAjaxFormIdentifier = "_ajaxformidentifier";
        public static int Seed = 10;//seed to generate random for ajaxformidentifier

        public static void AjaxOptionsForAction(UrlHelper urlHelper,TagWriter tagWriter, String action,String controller, String updateTargetId, Boolean forViewModelOnly) throws JspException
        {
        	if(action==null) action="";
        	if(forViewModelOnly==null) forViewModelOnly=StringUtils.isEmpty(updateTargetId);
        	
        	tagWriter.writeAttribute("data-ajax-method", "Post");
        	tagWriter.writeAttribute("data-ajax-loading", "#divProcessing");
        	tagWriter.writeAttribute("data-ajax-mode", "before");
        	tagWriter.writeAttribute("data-ajax", "true");
        	if(!StringUtils.isEmpty(updateTargetId)) {
        		tagWriter.writeAttribute("data-ajax-update", "#"+updateTargetId);
        	}

           
        	Map<String, Object> routeValues = new HashMap<String, Object>();
        	// ajaxHelper.ViewContext.HttpContext.Request.QueryString.CopyTo(dict);
            if (forViewModelOnly)
            {
                routeValues.put(Key_For_ForViewModelOnly,"true");
            }
            Seed = (Seed < 1000 ? Seed + 10  :  10);
            String ajaxFormIdentifier = new Date().getTime()+"";
            if(!StringUtils.isEmpty(updateTargetId) ){
            	ajaxFormIdentifier=(ajaxFormIdentifier+updateTargetId).replace(".", "") + (new Random(Seed)).nextInt(1000);
            }
            routeValues.put(Key_For_ForAjaxFormIdentifier, ajaxFormIdentifier);
            String url=urlHelper.GenerateUrl(action, controller, routeValues);

            tagWriter.writeAttribute("data-ajax-url", url);
            tagWriter.writeAttribute("data-ajax-begin", ProjectBaseJsPackageNamePrefix+"CommonOnBeginAjaxForm(xhr,'" + ajaxFormIdentifier + "')");
            tagWriter.writeAttribute("data-ajax-complete", ProjectBaseJsPackageNamePrefix+"CommonOnCompleteAjaxForm(xhr,status,'" + ajaxFormIdentifier + "')");
            
        }

//        public static String FunctionCallModelBinded(this AjaxHelper ajaxHelper)
//        {
//            return "function CallModel_Binded(){Model_Binded($.parseJSON('{\"UIAttr\" extends " +
//                   ((BaseViewModel)ajaxHelper.ViewData.Model).UIAttrsToJsonString() + "}'));}";
//        }
    }

