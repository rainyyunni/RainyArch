using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web.Mvc;
using System.Web.Mvc.Html;
using System.Web.Routing;
using System.Web.Script.Serialization;
using System.Web.UI;

namespace ProjectBase.Web.Mvc.Angular
{
    public static class HtmlHelperExtension
    {
        public static string Key_For_MessageClass = "Messages";
        public static string Attr_VMPrefix = "NetNgArch_Attr_VMPrefix";
        public static string DictJsName = "Dict";
        public static string UnSubmitNameMarker = "_";
        public static String vmJson(this HtmlHelper htmlHelper)
        {
            var data = htmlHelper.ViewContext.ViewData.Model;
            return data==null?"":new JavaScriptSerializer().Serialize(data);
        }
        private static String GetNgModel(this HtmlHelper htmlHelper,String name) {
		    return htmlHelper.ViewContext.ViewData[Attr_VMPrefix] + "."+name;;
	    }
        public static IDictionary<string, object> AddClientValidation<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, IDictionary<string, object> htmlAttributes)
        {
            htmlHelper.EnableUnobtrusiveJavaScript(false);
            if (htmlAttributes == null) htmlAttributes = new Dictionary<string, object>() { };
            if (!htmlAttributes.ContainsKey("ng-model")) htmlAttributes["ng-model"] = GetNgModel(htmlHelper, ExpressionHelper.GetExpressionText(expression)); 
            ModelMetadata meta = ModelMetadata.FromLambdaExpression(expression, htmlHelper.ViewData);
            if (meta == null) return htmlAttributes;
            var rules = ModelValidatorProviders.Providers.GetValidators(meta, htmlHelper.ViewContext).SelectMany(v => v.GetClientValidationRules());
            if (rules.Count() > 0)
            {
                htmlAttributes["translatekey"] = meta.DisplayName;
            }
            foreach (var rule in rules)
            {
                switch(rule.ValidationType){
                case
                    "required":
                        htmlAttributes["required"] = "required";
                        break;
                case
                    "min":
                        htmlAttributes["min"] = rule.ValidationParameters["min"];
                        break;
                case
                    "max":
                        htmlAttributes["max"] = rule.ValidationParameters["max"];
                        break;
                case
                    "range":
                        if(rule.ValidationParameters.ContainsKey("min")){
                            htmlAttributes["min"] = rule.ValidationParameters["min"];
                        }
                        if(rule.ValidationParameters.ContainsKey("max")){
                            htmlAttributes["max"] = rule.ValidationParameters["max"];
                        }
                        break;
                case
                    "number":
                        htmlAttributes["type"] = "number";
                        break;
                case
                    "length":
                        if(rule.ValidationParameters.ContainsKey("min")){
                            htmlAttributes["ng-minlength"] = rule.ValidationParameters["min"];
                        }
                        if(rule.ValidationParameters.ContainsKey("max")){
                            htmlAttributes["ng-maxlength"] = rule.ValidationParameters["max"];
                        }
                        break;
                case
                    "regex":
                        var pattern =(string) rule.ValidationParameters["pattern"];
                        if (!pattern.StartsWith("/")) pattern = "/" + pattern + "/";
                        htmlAttributes["ng-pattern"] = pattern;
                        break;
                case
                    "date":
                        htmlAttributes["type"] = "date";
                        break;
                }
            }
            return htmlAttributes;
        }

        public static IDictionary<string, object> AddNgModel(this HtmlHelper htmlHelper, string name, IDictionary<string, object> htmlAttributes)
        {
            if (htmlAttributes == null) htmlAttributes = new Dictionary<string, object>() { };
            if (!htmlAttributes.ContainsKey("ng-model")) htmlAttributes["ng-model"] = GetNgModel(htmlHelper, name);
            return htmlAttributes;
        }
        public static IDictionary<string, object> AddNgModel<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, IDictionary<string, object> htmlAttributes)
        {
            if (htmlAttributes == null) htmlAttributes = new Dictionary<string, object>() { };
            if (!htmlAttributes.ContainsKey("ng-model")) htmlAttributes["ng-model"] = GetNgModel(htmlHelper, ExpressionHelper.GetExpressionText(expression)); 
            return htmlAttributes;
        }
        public static MvcHtmlString AdjustName(MvcHtmlString mvcHtmlString, IDictionary<string, object> htmlAttributes,bool ngModelExplicit)
        {
            String explicitname = htmlAttributes.ContainsKey("name")?(String)htmlAttributes["name"]:null;
            String nosubmit = explicitname == "" ? UnSubmitNameMarker : "";
            if (String.IsNullOrEmpty(explicitname) && ngModelExplicit)
            {
                String nameExpr = (String)htmlAttributes["ng-model"];
                if (nameExpr.IndexOf("[") > 0)
                {
                    String arrayName = nameExpr.Substring(0, nameExpr.IndexOf("["));
                    nameExpr = nameExpr.Replace("[", "[{{").Replace("]", "|ReIndex:" + arrayName + "}}]");
                }
                String nameprefix = htmlAttributes.ContainsKey("ng-name-prefix") ? (String)htmlAttributes["ng-name-prefix"] : null;
                if (nameprefix == null) nameprefix = "c.vm";
                if (!nameprefix.EndsWith(".")) nameprefix = nameprefix + ".";
                explicitname=nosubmit + nameExpr.Substring(nameprefix.Length);
            };
            var h= mvcHtmlString.ToHtmlString();
            int pos0 = h.IndexOf(" id=\"");
            int pos1 = h.IndexOf("\"", pos0+5);
            h = h.Remove(pos0, pos1 - pos0+1);
            if (!String.IsNullOrEmpty(explicitname))
            {
                pos0 = h.IndexOf(" name=\"");
                pos1 = h.IndexOf("\"", pos0 + 7);
                h = h.Remove(pos0, pos1 - pos0 + 1);
                h = h.Insert(pos0, "name=\"" + explicitname + "\"");
            }
            return new MvcHtmlString(h);
        }
        public static MvcForm NgForm(this HtmlHelper htmlHelper, string name)
        {
            return NgForm(htmlHelper, name, null, null,  FormMethod.Post, null);
        }
        public static MvcForm NgForm(this HtmlHelper htmlHelper, string name, string actionName)
        {
            return NgForm(htmlHelper, name, actionName, null,  FormMethod.Post, null);
        }
        public static MvcForm NgForm(this HtmlHelper htmlHelper, string name, string actionName, string cssClass)
        {
            return NgForm(htmlHelper, name, actionName, cssClass,  FormMethod.Post, null);
        }
        public static MvcForm NgForm(this HtmlHelper htmlHelper, string name, string actionName, string cssClass, object htmlAttributes)
        {
            return NgForm(htmlHelper, name, actionName, cssClass, null, htmlAttributes);
        }
        private static MvcForm NgForm(this HtmlHelper htmlHelper, string name, string actionName, string cssClass, FormMethod? method, object htmlAttributes)
        {
            RouteValueDictionary attrs = HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes);
            if (method == null && !attrs.ContainsKey("method")) 
                method = FormMethod.Post;
            else if (method == null)
            {
                method = (FormMethod)attrs["method"];
                //method = attrs["method");
            }
            if (!string.IsNullOrEmpty(cssClass)) attrs["class"] = cssClass;
            string controllerName = null;
            if (actionName != null && actionName.Contains("/"))
            {
                var ac = actionName.Split('/');
                actionName = ac[0];
                controllerName = ac[1];
            }
            var url =actionName == null?null: UrlHelper.GenerateUrl(null, actionName, controllerName, null, htmlHelper.RouteCollection,
                                                      htmlHelper.ViewContext.RequestContext, true);
            if (!String.IsNullOrEmpty(url)) attrs["ajax-url"] = url;
            if(string.IsNullOrEmpty(name))
                name = (String) attrs["name"];
            else
            {
                attrs["name"] = name;
            }
            var s = String.IsNullOrEmpty(name) ? "" : (name.Contains(".") ? name.Split('.')[0] : "");
            htmlHelper.ViewContext.ViewData[Attr_VMPrefix]= (String.IsNullOrEmpty(s) ? "c" : s)+".vm";
            return FormExtensions.BeginForm(htmlHelper, actionName, controllerName, new RouteValueDictionary(), method.Value, attrs);
        }

    }
}
