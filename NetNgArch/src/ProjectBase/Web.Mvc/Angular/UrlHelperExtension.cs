using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;
using System.Web.Mvc;
using System.Web.Mvc.Html;
using System.Web.Routing;
using System.Web.Script.Serialization;
using System.Web.UI;

namespace ProjectBase.Web.Mvc.Angular
{
    public static class UrlHelperExtension
    {
        public static String Deprefix(String stateName)
        {
            if (HttpContext.Current.Request.ApplicationPath.Length>1 && stateName.StartsWith(HttpContext.Current.Request.ApplicationPath))
            {
                stateName = stateName.Substring(HttpContext.Current.Request.ApplicationPath.Length);
            }
            return stateName;
        }
        public static String State(this UrlHelper urlHelper,String sref)
        {
            return State(urlHelper, sref, null /* controllerName */, null);
        }

        public static String State(this UrlHelper urlHelper, String sref, Object routeValues)
        {
            return State(urlHelper, sref, null /* controllerName */, routeValues);
        }

        public static String State(this UrlHelper urlHelper, String sref, RouteValueDictionary routeValues)
        {
            return State(urlHelper, sref, null /* controllerName */, routeValues);
        }

        public static String State(this UrlHelper urlHelper, String sref, String controllerName)
        {
            return State(urlHelper, sref, controllerName, (RouteValueDictionary)null /* routeValues */);
        }
        public static String State(this UrlHelper urlHelper, String sref, String controllerName, Object routeValues)
        {
            return State(urlHelper, sref, controllerName, new RouteValueDictionary(routeValues));
        }
        public static String State(this UrlHelper urlHelper, String sref, String controllerName, RouteValueDictionary routeValues)
        {
            String nav = null;
            String stateName = null;
            String param = "";
            int pos = 0;
            if (sref.StartsWith("root:") || sref.StartsWith("forward:"))
            {
                pos = sref.IndexOf(":");
                nav = sref.Substring(0, pos);
                sref = sref.Substring(pos + 1);
            }
            pos = sref.IndexOf("(");
            if (pos > 0)
            {
                stateName = sref.Substring(0, pos);
                param = sref.Substring(pos + 1, sref.Length - (pos + 1)-1);
            }
            else
            {
                stateName = sref;
            }
            if (!stateName.StartsWith("/"))
            {
                stateName = urlHelper.Action(stateName, controllerName, routeValues);
                stateName = Deprefix(stateName);
            }
            if (nav != null)
                param = "({" + param + (param == "" ? "" : ",") + "'ajax-nav':'" + nav + "'})";
            else if (param!="")
            {
                param = "({" + param + "})";
            }
            var attr = "ui-sref=" + stateName + param + "" + " ui-sref-opts={reload:true,inherit:false}";
            return attr;
        }

    }
}
