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

namespace ProjectBase.Web.Mvc
{
    public static class HtmlHelperExtension
    {
        public static string Key_For_MessageClass = "Messages";

        public static MvcHtmlString RadioButtonFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper,
                                                                      Expression<Func<TModel, TProperty>> expression,
                                                                      object value, bool bindNull)
        {
            return RadioButtonFor(htmlHelper, expression, value, null /* htmlAttributes */, bindNull);
        }

        public static MvcHtmlString RadioButtonFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper,
                                                                      Expression<Func<TModel, TProperty>> expression,
                                                                      object value, object htmlAttributes, bool bindNull)
        {
            return RadioButtonFor(htmlHelper, expression, value,
                                  HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes), bindNull);
        }

        public static MvcHtmlString RadioButtonFor<TModel, TProperty>(this HtmlHelper<TModel> htmlHelper,
                                                                      Expression<Func<TModel, TProperty>> expression,
                                                                      object value,
                                                                      IDictionary<string, object> htmlAttributes,
                                                                      bool bindNull)
        {
            MvcHtmlString mvcHtmlString;
            if (value == null && bindNull)
            {
                mvcHtmlString = InputExtensions.RadioButtonFor(htmlHelper, expression, "", htmlAttributes);
                var metadata = ModelMetadata.FromLambdaExpression(expression, htmlHelper.ViewData);
                if (metadata.Model == null && mvcHtmlString.ToHtmlString().IndexOf(" checked=\"checked\" ") < 0)
                {
                    var index = mvcHtmlString.ToHtmlString().IndexOf(" id=");
                    return new MvcHtmlString(mvcHtmlString.ToHtmlString().Insert(index, " checked=\"checked\""));
                }
            }
            else
                mvcHtmlString = InputExtensions.RadioButtonFor(htmlHelper, expression, value, htmlAttributes);

            return mvcHtmlString;

        }

        public static MvcHtmlString RadioButton(this HtmlHelper htmlHelper, string name, object value, bool bindNull)
        {
            return RadioButton(htmlHelper, name, value, (object) null /* htmlAttributes */, bindNull);
        }

        public static MvcHtmlString RadioButton(this HtmlHelper htmlHelper, string name, object value,
                                                object htmlAttributes, bool bindNull)
        {
            return RadioButton(htmlHelper, name, value, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes),
                               bindNull);
        }

        public static MvcHtmlString RadioButton(this HtmlHelper htmlHelper, string name, object value,
                                                IDictionary<string, object> htmlAttributes, bool bindNull)
        {
            MvcHtmlString mvcHtmlString;
            if (bindNull)
            {
                mvcHtmlString = InputExtensions.RadioButton(htmlHelper, name, value ?? "", htmlAttributes);
                if ((value == null && htmlHelper.ViewData.Model == null ||
                     value != null && htmlHelper.ViewData.Model != null &&
                     (bool) htmlHelper.ViewData.Model == (bool) value) &&
                    mvcHtmlString.ToHtmlString().IndexOf(" checked=\"checked\" ") < 0)
                {
                    var index = mvcHtmlString.ToHtmlString().IndexOf(" id=");
                    return new MvcHtmlString(mvcHtmlString.ToHtmlString().Insert(index, " checked=\"checked\""));
                }
            }
            else
                mvcHtmlString = InputExtensions.RadioButton(htmlHelper, name, value, htmlAttributes);

            return mvcHtmlString;
        }

        /// <summary>
        /// use the param useEnumTemplate to chose this method which will use Enum template for Enum Type
        /// </summary>
        /// <typeparam name="TModel"></typeparam>
        /// <typeparam name="TValue"></typeparam>
        /// <param name="html"></param>
        /// <param name="expression"></param>
        /// <param name="useEnumTemplate"></param>
        /// <returns></returns>
        public static MvcHtmlString EditorFor<TModel, TValue>(this HtmlHelper<TModel> html,
                                                              Expression<Func<TModel, TValue>> expression,
                                                              bool useEnumTemplate)
        {
            if (typeof (TValue).IsEnum)
                return EditorExtensions.EditorFor(html, expression, "Enum", new {EnumType = typeof (TValue)});
            else if (Nullable.GetUnderlyingType(typeof (TValue)) != null &&
                     Nullable.GetUnderlyingType(typeof (TValue)).IsEnum)
                return EditorExtensions.EditorFor(html, expression, "Enum",
                                                  new {EnumType = Nullable.GetUnderlyingType(typeof (TValue))});

            return EditorExtensions.EditorFor(html, expression);
        }
        public static MvcHtmlString EditorFor<TModel, TValue>(this HtmlHelper<TModel> html,
                                                              Expression<Func<TModel, TValue>> expression,
                                                              bool useEnumTemplate,string templatename)
        {
            if (typeof (TValue).IsEnum)
                return EditorExtensions.EditorFor(html, expression, templatename, new { EnumType = typeof(TValue) });
            else if (Nullable.GetUnderlyingType(typeof (TValue)) != null &&
                     Nullable.GetUnderlyingType(typeof (TValue)).IsEnum)
                return EditorExtensions.EditorFor(html, expression, templatename,
                                                  new {EnumType = Nullable.GetUnderlyingType(typeof (TValue))});

            return EditorExtensions.EditorFor(html, expression, templatename);
        }
        public static MvcHtmlString EditorFor<TModel, TValue>(this HtmlHelper<TModel> html,
                                                      Expression<Func<TModel, TValue>> expression,
                                                      bool useEnumTemplate, string templatename,object additionalViewData)
        {
            RouteValueDictionary vd=null;
            var style = "";
            if (additionalViewData != null)
            {
                vd = new RouteValueDictionary(additionalViewData);
                style = (string)vd["Style"];
            }
            if (typeof(TValue).IsEnum)
                return EditorExtensions.EditorFor(html, expression, templatename, new { EnumType = typeof(TValue),Style=style, AdditionalRVD = vd });
            else if (Nullable.GetUnderlyingType(typeof(TValue)) != null &&
                     Nullable.GetUnderlyingType(typeof(TValue)).IsEnum)
                return EditorExtensions.EditorFor(html, expression, templatename,
                                                  new { EnumType = Nullable.GetUnderlyingType(typeof(TValue)), Style = style, AdditionalRVD = vd });

            return EditorExtensions.EditorFor(html, expression, templatename, additionalViewData);
        }

        public static MvcHtmlString DisplayerFor<TModel, TValue>(this HtmlHelper<TModel> html,
                                                      Expression<Func<TModel, TValue>> expression)
        {
            if (typeof(TValue).IsEnum)
                return DisplayExtensions.DisplayFor(html, expression, "Enum", new { EnumType = typeof(TValue) });
            else if (Nullable.GetUnderlyingType(typeof(TValue)) != null &&
                     Nullable.GetUnderlyingType(typeof(TValue)).IsEnum)
                return DisplayExtensions.DisplayFor(html, expression, "Enum",
                                                  new { EnumType = Nullable.GetUnderlyingType(typeof(TValue)) });

            //this is for view templates to use ,make it consistent as GetDisplayDictionary to viewModelDisplay
            if (expression.Body.NodeType == ExpressionType.MemberAccess)
            {
                var body = (MemberExpression) expression.Body;
                var containerExp = (MemberExpression) body.Expression;
                var prop = body.Member;
                var cotainerType = prop.DeclaringType;
                var displayprop = prop.Name + DisplayExtension.SurfixForDisplay;
                if (cotainerType.GetProperty(displayprop) != null)
                {
                    var displayExp =
                        Expression.Lambda<Func<TModel, string>>(Expression.Property(containerExp, displayprop),expression.Parameters);
                    return DisplayExtensions.DisplayFor<TModel, string>(html, displayExp);
                }
            }
            return DisplayExtensions.DisplayFor<TModel, TValue>(html, expression);
        }

        public static string ToDORefAutoCompleteSourceJs(this HtmlHelper html, SelectList sourcelist, string varname)
        {
            var jstext=" var "+varname+"= [";
            var jsid = " var " + varname + "_Values= [";
            foreach(var item in sourcelist.Items)
            {
                var id = DataBinder.Eval(item, "Id");
                var text = DataBinder.Eval(item, "RefText");
                //js += "{value:\"" + id + "\",label:\"" + text + "\"},";
                jstext += "'"+text + "',";
                jsid += id + ",";
            }
            if (jstext.Last() == '[') return jstext + "];" + jsid + "];";
            return jstext.Remove(jstext.Length - 1) + "];" + jsid.Remove(jsid.Length - 1) + "];";
            
        }

    }
}
