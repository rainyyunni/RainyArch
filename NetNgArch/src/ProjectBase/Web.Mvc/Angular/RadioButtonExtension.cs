using System;
using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Web;
using System.Web.Mvc;
using System.Web.Mvc.Html;
using System.Web.Routing;
using System.Web.UI;

namespace ProjectBase.Web.Mvc.Angular
{
    public static class RadioButtonExtension
    {
        public static MvcHtmlString NgRadioButtonFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, object value)
        {
            return NgRadioButtonFor(htmlHelper, expression, value,null, null /* htmlAttributes */);
        }
        public static MvcHtmlString NgRadioButtonFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, object value,string cssClass)
        {
            return NgRadioButtonFor(htmlHelper, expression, value,cssClass, null /* htmlAttributes */);
        }
        public static MvcHtmlString NgRadioButtonFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, object value, string cssClass, object htmlAttributes)
        {
            return NgRadioButtonFor(htmlHelper, expression, value,cssClass, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static MvcHtmlString NgRadioButtonFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, object value, string cssClass, IDictionary<string, object> htmlAttributes)
        {
            var ngModelExplicit = htmlAttributes != null && htmlAttributes.ContainsKey("ng-model");
            htmlAttributes = HtmlHelperExtension.AddNgModel(htmlHelper, expression, htmlAttributes);
            if (!String.IsNullOrEmpty(cssClass)) htmlAttributes["class"] = cssClass;
            if(value==null)
            {
                value = "";
			    htmlAttributes["ng-value"]= "null";
		    }else if(value is bool)
            {
                htmlAttributes["ng-value"] = value.ToString().ToLower();
            }
            else
            {
                htmlAttributes["ng-value"] = "'" + value + "'";
            }
            return HtmlHelperExtension.AdjustName(System.Web.Mvc.Html.InputExtensions.RadioButtonFor(htmlHelper, expression, value, htmlAttributes), htmlAttributes, ngModelExplicit);
        }
      

    }
}
