using System.Web.Mvc;

namespace ProjectBase.Web.Mvc.Angular
{
    using System;
    using System.Collections.Generic;
    using System.Linq.Expressions;

    public static class TextAreaExtensions {

        public static MvcHtmlString NgTextAreaFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression) {
            return NgTextAreaFor(htmlHelper, expression, null,null);
        }
        public static MvcHtmlString NgTextAreaFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression,string cssClass)
        {
            return NgTextAreaFor(htmlHelper, expression, cssClass, null);
        }
        public static MvcHtmlString NgTextAreaFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string cssClass, object htmlAttributes)
        {
            return NgTextAreaFor(htmlHelper, expression,cssClass, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static MvcHtmlString NgTextAreaFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string cssClass, IDictionary<string, object> htmlAttributes)
        {
            if (expression == null) {
                throw new ArgumentNullException("expression");
            }
            var ngModelExplicit = htmlAttributes != null && htmlAttributes.ContainsKey("ng-model");
            htmlAttributes = HtmlHelperExtension.AddClientValidation(htmlHelper, expression, htmlAttributes);
            if (!String.IsNullOrEmpty(cssClass)) htmlAttributes["class"] = cssClass;
            if (!htmlAttributes.ContainsKey("ng-model-options"))
            {
                htmlAttributes["ng-model-options"] = "{ updateOn: 'blur' }";
            }
            return HtmlHelperExtension.AdjustName(System.Web.Mvc.Html.TextAreaExtensions.TextAreaFor(htmlHelper, expression, htmlAttributes), htmlAttributes, ngModelExplicit);
        }

        public static MvcHtmlString NgTextAreaFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, int rows, int columns, object htmlAttributes)
        {
            return NgTextAreaFor(htmlHelper, expression, null, rows, columns, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }
        public static MvcHtmlString NgTextAreaFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string cssClass, int rows, int columns, object htmlAttributes)
        {
            return NgTextAreaFor(htmlHelper, expression, cssClass, rows, columns, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static MvcHtmlString NgTextAreaFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string cssClass,int rows, int columns, IDictionary<string, object> htmlAttributes) {
            if (expression == null) {
                throw new ArgumentNullException("expression");
            }
            var ngModelExplicit = htmlAttributes != null && htmlAttributes.ContainsKey("ng-model");
            htmlAttributes = HtmlHelperExtension.AddClientValidation(htmlHelper, expression, htmlAttributes);
            if (!String.IsNullOrEmpty(cssClass)) htmlAttributes["class"] = cssClass;
            if (!htmlAttributes.ContainsKey("ng-model-options"))
            {
                htmlAttributes["ng-model-options"] = "{ updateOn: 'blur' }";
            }
            return HtmlHelperExtension.AdjustName(System.Web.Mvc.Html.TextAreaExtensions.TextAreaFor(htmlHelper, expression, rows, columns, htmlAttributes), htmlAttributes, ngModelExplicit);
        }

    }
}
