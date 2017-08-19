using System;
using System.Web.Mvc;

namespace ProjectBase.Web.Mvc.Angular
{
    using System.Collections;
    using System.Collections.Generic;
    using System.Diagnostics.CodeAnalysis;
    using System.Globalization;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Text;
    using System.Web;

    public static class SelectExtensions
    {
        private static string LabelProp = "RefText";
        private static string ValueProp = "Id";
        public static MvcHtmlString NgSelectFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression)
        {
            return NgSelectFor(htmlHelper, expression, null, null /* htmlAttributes */);
        }
        public static MvcHtmlString NgSelectFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, String ngSelectList)
        {
            return NgSelectFor(htmlHelper, expression, ngSelectList, null /* htmlAttributes */);
        }
        public static MvcHtmlString NgSelectFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, object htmlAttributes)
        {
            return NgSelectFor(htmlHelper, expression, null, htmlAttributes);
        }
        public static MvcHtmlString NgSelectFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, String ngSelectList, object htmlAttributes)
        {
            return NgSelectFor(htmlHelper, expression, ngSelectList, null /* optionLabel */,null, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }
        public static MvcHtmlString NgSelectFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, String ngSelectList, string optionLabel)
        {
            return NgSelectFor(htmlHelper, expression, ngSelectList, optionLabel, null, null /* htmlAttributes */);
        }
        public static MvcHtmlString NgSelectFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, String ngSelectList, string optionLabel,string cssClass)
        {
            return NgSelectFor(htmlHelper, expression, ngSelectList, optionLabel, cssClass, null /* htmlAttributes */);
        }

        public static MvcHtmlString NgSelectFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, String ngSelectList, string optionLabel, string cssClass, object htmlAttributes)
        {
            return NgSelectFor(htmlHelper, expression, ngSelectList, optionLabel, cssClass, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static MvcHtmlString NgSelectFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, String ngSelectList, string optionLabel, string cssClass, IDictionary<string, object> htmlAttributes)
        {
            return NgSelectHelper(htmlHelper, expression, ngSelectList, optionLabel, cssClass, htmlAttributes);
        }
        private static MvcHtmlString NgSelectHelper<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, String ngSelectList, string optionLabel, string cssClass,IDictionary<string, object> htmlAttributes)
        {
            if (expression == null)
            {
                throw new ArgumentNullException("expression");
            }
            var ngModelExplicit = htmlAttributes != null && htmlAttributes.ContainsKey("ng-model");
            var name = ExpressionHelper.GetExpressionText(expression);
            string fullName = htmlHelper.ViewContext.ViewData.TemplateInfo.GetFullHtmlFieldName(name);
            if (String.IsNullOrEmpty(fullName))
            {
                throw new ArgumentException("MvcResources.Common_NullOrEmpty", "name");
            }
            TagBuilder opnBuilder=null;
            var opns = "";
            var ptype = typeof (TProperty);
            if (ptype.IsEnum || Nullable.GetUnderlyingType(ptype)!=null)
            {
                if (!ptype.IsEnum) ptype = Nullable.GetUnderlyingType(ptype);
                htmlAttributes = HtmlHelperExtension.AddClientValidation(htmlHelper, expression, htmlAttributes);
                if (ngSelectList != null || optionLabel != null)
                {
                    opnBuilder = new TagBuilder("option")
                                     {
                                         InnerHtml = HttpUtility.HtmlEncode(ngSelectList ?? optionLabel)
                                     };
                    opnBuilder.MergeAttribute("value","");
                    opnBuilder.MergeAttribute("translate", "");
                    opns = opnBuilder.ToString(TagRenderMode.Normal);
                }
                ngSelectList = HtmlHelperExtension.DictJsName + "." + ptype.FullName.Substring(ptype.FullName.LastIndexOf(".") + 1).Replace("+", "_");
            }
            else if (ngSelectList == null && (htmlAttributes==null || !htmlAttributes.ContainsKey("ng-options")))
            {
                throw new ArgumentNullException("ng-options or ngSelectList");
            }
            else
            {
                htmlAttributes=HtmlHelperExtension.AddNgModel(htmlHelper, name + ".Id", htmlAttributes);
                htmlAttributes = HtmlHelperExtension.AddClientValidation(htmlHelper, expression, htmlAttributes);
                if(!ngModelExplicit)fullName = fullName + ".Id";
                if (optionLabel != null)
                {
                    opnBuilder = new TagBuilder("option")
                                     {
                                         InnerHtml = HttpUtility.HtmlEncode(optionLabel)
                                     };
                    opnBuilder.MergeAttribute("value", "");
                    opnBuilder.MergeAttribute("translate", "");
                    opns = opnBuilder.ToString(TagRenderMode.Normal);
                }
            }
            if (!String.IsNullOrEmpty(cssClass)) htmlAttributes["class"] = cssClass;
            TagBuilder tagBuilder = new TagBuilder("select");
            if (opns != "")
            {
                tagBuilder.InnerHtml = opns;
            };
            tagBuilder.MergeAttributes(htmlAttributes);
            tagBuilder.MergeAttribute("name", fullName, true /* replaceExisting */);
            tagBuilder.GenerateId(fullName);
            if(ptype.IsEnum)
            {
                var ngoptions = "value as label for (label, value) in " + ngSelectList;
                if (htmlAttributes.ContainsKey("pb-AddOptions")) ngoptions = ngoptions + "|SelectAddOptions:" + ((String)htmlAttributes["pb-AddOptions"]);
                tagBuilder.MergeAttribute("ng-options", ngoptions);
            }else if (!htmlAttributes.ContainsKey("ng-options"))
            {
                if (ngSelectList.Contains(" for "))
                {
                    tagBuilder.MergeAttribute("ng-options", ngSelectList);
                }
                else
                {
                    var ngoptions = "item.Id as item.RefText for item in " + ngSelectList;
                    if (htmlAttributes.ContainsKey("pb-EnforceMatch")) ngoptions = ngoptions + "|RefSelectEnforceMatch:" + ((String)htmlAttributes["ng-model"]).Replace(".Id", "");// +" track by item.Id";
                    if (htmlAttributes.ContainsKey("pb-AddOptions")) ngoptions = ngoptions + "|SelectAddOptions:" + ((String)htmlAttributes["pb-AddOptions"]);
                    tagBuilder.MergeAttribute("ng-options", ngoptions);
                }
            }

            return HtmlHelperExtension.AdjustName(new MvcHtmlString(tagBuilder.ToString(TagRenderMode.Normal)), htmlAttributes, ngModelExplicit);
        }


    }
}
