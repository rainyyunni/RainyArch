using System.Web.Mvc;
using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Globalization;
using System.Linq.Expressions;
using System.Text;
//using System.Web.Mvc.Resources;
using System.Web.Routing;
namespace ProjectBase.Web.Mvc.Angular{


    public static class InputExtensions {

        // Hidden
        public static MvcHtmlString NgHiddenFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression)
        {
            return NgHiddenFor(htmlHelper, expression, null /* htmlAttributes */);
        }

        public static MvcHtmlString NgHiddenFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, object htmlAttributes)
        {
            return NgHiddenFor(htmlHelper, expression, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static MvcHtmlString NgHiddenFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, IDictionary<string, object> htmlAttributes)
        {
            if (expression == null)
            {
                throw new ArgumentNullException("expression");
            }
            var ngModelExplicit = htmlAttributes != null && htmlAttributes.ContainsKey("ng-model");
            htmlAttributes = HtmlHelperExtension.AddClientValidation(htmlHelper, expression, htmlAttributes);
            var value = "{{" + htmlAttributes["ng-model"] + "}}";
            htmlAttributes.Remove("ng-model");
            htmlAttributes.Remove("type");
            return HtmlHelperExtension.AdjustName(System.Web.Mvc.Html.InputExtensions.Hidden(htmlHelper, ExpressionHelper.GetExpressionText(expression), value, htmlAttributes), htmlAttributes, ngModelExplicit);
        }

  // CheckBox

        public static MvcHtmlString NgCheckBoxFor<TModel>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, bool>> expression)
        {
            return NgCheckBoxFor(htmlHelper, expression,null,null, null /* htmlAttributes */);
        }
        public static MvcHtmlString NgCheckBoxFor<TModel>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, bool>> expression, string cssClass)
        {
            return NgCheckBoxFor(htmlHelper, expression, cssClass,null,null /* htmlAttributes */);
        }
        public static MvcHtmlString NgCheckBoxFor<TModel>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, bool>> expression, string cssClass, string value, object htmlAttributes)
        {
            return NgCheckBoxFor(htmlHelper, expression, cssClass, value, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static MvcHtmlString NgCheckBoxFor<TModel>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, bool>> expression, string cssClass, string value, IDictionary<string, object> htmlAttributes)
        {
            if (expression == null)
            {
                throw new ArgumentNullException("expression");
            }
             var ngModelExplicit = htmlAttributes!=null&&htmlAttributes.ContainsKey("ng-model");
            htmlAttributes = HtmlHelperExtension.AddNgModel(htmlHelper, expression, htmlAttributes);
            if (!String.IsNullOrEmpty(cssClass)) htmlAttributes["class"] = cssClass;
            if (value != null)
            {
                htmlAttributes["ng-true-value"] = "'" + value + "'";
            }
            return HtmlHelperExtension.AdjustName(System.Web.Mvc.Html.InputExtensions.CheckBoxFor(htmlHelper, expression, htmlAttributes), htmlAttributes, ngModelExplicit);
        }

        // Password

        public static MvcHtmlString NgPasswordFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression)
        {
            return NgPasswordFor(htmlHelper, expression,null, null /* htmlAttributes */);
        }
        public static MvcHtmlString NgPasswordFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string cssClass)
        {
            return NgPasswordFor(htmlHelper, expression, cssClass, null /* htmlAttributes */);
        }
        public static MvcHtmlString NgPasswordFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string cssClass, object htmlAttributes)
        {
            return NgPasswordFor(htmlHelper, expression,cssClass, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static MvcHtmlString NgPasswordFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string cssClass, IDictionary<string, object> htmlAttributes)
        {
            if (expression == null)
            {
                throw new ArgumentNullException("expression");
            }
            var ngModelExplicit = htmlAttributes != null && htmlAttributes.ContainsKey("ng-model");
            htmlAttributes=HtmlHelperExtension.AddClientValidation(htmlHelper, expression, htmlAttributes);
            if (!String.IsNullOrEmpty(cssClass)) htmlAttributes["class"] = cssClass;
            if (!htmlAttributes.ContainsKey("ng-model-options"))
            {
                htmlAttributes["ng-model-options"] = "{ updateOn: 'blur' }";
            }
            return HtmlHelperExtension.AdjustName(System.Web.Mvc.Html.InputExtensions.PasswordFor(htmlHelper, expression, htmlAttributes), htmlAttributes, ngModelExplicit);
        }


        // TextBox

        public static MvcHtmlString NgTextBoxFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression)
        {
            return htmlHelper.NgTextBoxFor(expression,null, null);
        }
        public static MvcHtmlString NgTextBoxFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string cssClass)
        {
            return htmlHelper.NgTextBoxFor(expression,cssClass, null);
        }

         public static MvcHtmlString NgTextBoxFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string cssClass, object htmlAttributes)
         {
            return htmlHelper.NgTextBoxFor(expression,cssClass, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static MvcHtmlString NgTextBoxFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string cssClass, IDictionary<string, object> htmlAttributes)
        {
            var ngModelExplicit = htmlAttributes!=null&&htmlAttributes.ContainsKey("ng-model");
            htmlAttributes=HtmlHelperExtension.AddClientValidation(htmlHelper, expression, htmlAttributes);
            if (!String.IsNullOrEmpty(cssClass)) htmlAttributes["class"] = cssClass;
            if (htmlAttributes.ContainsKey("type") && (String)htmlAttributes["type"]=="date")
            {
                htmlAttributes.Remove("type");
                htmlAttributes["uib-datepicker-popup"]="yyyy-MM-dd";
                if (!htmlAttributes.ContainsKey("pb-datepicker-button"))
                {
                   htmlAttributes["pb-datepicker-button"]= "yes";
                }
            }else
            if (!htmlAttributes.ContainsKey("ng-model-options"))
            {
                htmlAttributes["ng-model-options"]="{ updateOn: 'blur' }";
            }
            return HtmlHelperExtension.AdjustName(System.Web.Mvc.Html.InputExtensions.TextBoxFor(htmlHelper, expression, htmlAttributes), htmlAttributes, ngModelExplicit);
        }
    }
}
