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

namespace ProjectBase.Web.Mvc.Angular{

    public static class LabelExtensions {

         public static MvcHtmlString NgLabelFor<TModel, TValue>(this HtmlHelper<TModel> html, Expression<Func<TModel, TValue>> expression,  string cssClass=null,string labelText=null) {
            return LabelHelper(html,
                               ModelMetadata.FromLambdaExpression(expression, html.ViewData),
                               ExpressionHelper.GetExpressionText(expression),
                               cssClass,
                               labelText);
        }

        internal static MvcHtmlString LabelHelper(HtmlHelper html, ModelMetadata metadata, string htmlFieldName, string cssClass = null, string labelText = null) {
            TagBuilder tag = new TagBuilder("label");
            tag.Attributes.Add("for", TagBuilder.CreateSanitizedId(html.ViewContext.ViewData.TemplateInfo.GetFullHtmlFieldName(htmlFieldName)));
            tag.InnerHtml = "{{'" + metadata.DisplayName + "'|translate}}";
            if (metadata.IsRequired) tag.Attributes.Add("pb-required", "label");
            tag.Attributes.Add("class", cssClass);
            return new MvcHtmlString(tag.ToString(TagRenderMode.Normal));
        }
    }
}
