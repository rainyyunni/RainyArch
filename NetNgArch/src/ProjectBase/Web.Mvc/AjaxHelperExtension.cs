using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Web.Mvc;
using System.Web.Mvc.Ajax;
using System.Web.Mvc.Html;
using System.Web.Routing;

namespace ProjectBase.Web.Mvc
{
    public static class AjaxHelperExtension
    {
        public static string AuthFailure = "_AuthFailure";
        public static string Key_For_ForViewModelOnly = "_ForViewModelOnly";
        public static string Key_For_ForAjaxFormIdentifier = "_ajaxformidentifier";
        public static int Seed = 10;//seed to generate random for ajaxformidentifier

        public static AjaxOptions AjaxOptionsForAction(this AjaxHelper ajaxHelper)
        {
            return AjaxOptionsForAction(ajaxHelper, null, null);
        }
        public static AjaxOptions AjaxOptionsForAction(this AjaxHelper ajaxHelper, string action)
        {
            return AjaxOptionsForAction(ajaxHelper, action, null);
        }
        public static AjaxOptions AjaxOptionsForAction(this AjaxHelper ajaxHelper, string action, string updateTargetId)
        {
            var forViewModelOnly=string.IsNullOrEmpty(updateTargetId);
            return AjaxOptionsForAction(ajaxHelper, action, updateTargetId, forViewModelOnly);
        }
        public static AjaxOptions AjaxOptionsForAction(this AjaxHelper ajaxHelper, string action, string updateTargetId, bool forViewModelOnly)
        {
            var options = new AjaxOptions { UpdateTargetId = updateTargetId, LoadingElementId = "divProcessing" };
            options.HttpMethod = "Post";
           
            var dict = new Dictionary<string, object>();
           // ajaxHelper.ViewContext.HttpContext.Request.QueryString.CopyTo(dict);
            var routeValues = new RouteValueDictionary(dict);
            if (forViewModelOnly)
            {
                routeValues.Add(Key_For_ForViewModelOnly,"true");
            }
            Seed = (Seed < 1000 ? Seed + 10 : 10);
            var ajaxFormIdentifier = DateTime.Now.Ticks + (updateTargetId ?? "").Replace(".", "") + (new Random(Seed)).Next(1000);
            routeValues.Add(Key_For_ForAjaxFormIdentifier, ajaxFormIdentifier);
            String controllerName = null;
            if(action.Contains("/"))
            {
                var a = action.Split('/');
                controllerName = a[0];
                action = a[1];
            }
            options.Url = UrlHelper.GenerateUrl(null, action, controllerName, routeValues, ajaxHelper.RouteCollection, ajaxHelper.ViewContext.RequestContext, true);

            options.InsertionMode = InsertionMode.InsertBefore;
            options.OnBegin = "CommonOnBeginAjaxForm(xhr,'" + ajaxFormIdentifier + "')";
            options.OnComplete = "CommonOnCompleteAjaxForm(xhr,status,'" + ajaxFormIdentifier + "')";
            return options;
        }
        public static MvcForm BeginFormWithId(this AjaxHelper ajaxHelper,string formid, AjaxOptions ajaxOptions)
        {
            return ajaxHelper.BeginForm(null, null, ajaxOptions, new { id = formid });
        }
        //public static String FunctionCallModelBinded(this AjaxHelper ajaxHelper)
        //{
        //    return "function CallModel_Binded(){Model_Binded($.parseJSON('{\"UIAttr\":" +
        //           ((BaseViewModel)ajaxHelper.ViewData.Model).UIAttrsToJsonString() + "}'));}";
        //}
    }
}
