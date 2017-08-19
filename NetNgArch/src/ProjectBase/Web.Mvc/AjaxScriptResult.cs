using System;
using System.Collections.Generic;
using System.Text;
using System.Web.Mvc;
using System.Web.Mvc.Html;

namespace ProjectBase.Web.Mvc
{
    //服务器响应回到客户端后不改变页面显示，但执行javascript
   public class AjaxScriptResult : JavaScriptResult
   {

       public string MessageTargetId { get; set; }

       public string Message { get; set; }

       public AjaxScriptResult()
       {
           MessageTargetId = "MessageLine";
       }
       public AjaxScriptResult(string message):this()
       {
           Message = message;
       }

       public override void ExecuteResult(ControllerContext context)
       {
           if (context == null)
            {
                throw new ArgumentNullException("context");
            }

           if (Script == null)
           {

               if (string.IsNullOrEmpty(Message)) Message = context.Controller.ViewBag.Message;
               Script = "$('#" + MessageTargetId + "').show();$('#"+MessageTargetId+"').html('" + Message
                    + ValidationSummary(context)
                   + "');";
           }
           base.ExecuteResult(context);
       }

       public String ValidationSummary(ControllerContext context)
       {
           string messageSpan=null;
           StringBuilder htmlSummary = new StringBuilder();
           TagBuilder unorderedList = new TagBuilder("ul");

           IEnumerable<ModelState> modelStates = null;

           modelStates = context.Controller.ViewData.ModelState.Values;

            foreach (ModelState modelState in modelStates)
            {
                foreach (ModelError modelError in modelState.Errors)
                {
                    string errorText = modelError.ErrorMessage;
                    if (String.IsNullOrEmpty(errorText) && modelError.Exception!=null)
                    {
                            errorText = modelError.Exception.InnerException == null?
                                modelError.Exception.Message:
                                modelError.Exception.InnerException.Message;
                    }
                    if (!String.IsNullOrEmpty(errorText))
                    {
                        TagBuilder listItem = new TagBuilder("li");
                        listItem.SetInnerText(errorText);
                        htmlSummary.Append(listItem.ToString(TagRenderMode.Normal));
                    }
                }
            }


           if (htmlSummary.Length == 0)
           {
               return "";
           }

           unorderedList.InnerHtml = htmlSummary.ToString();

           TagBuilder divBuilder = new TagBuilder("div");
           divBuilder.AddCssClass((context.Controller.ViewData.ModelState.IsValid) ? HtmlHelper.ValidationSummaryValidCssClassName : HtmlHelper.ValidationSummaryCssClassName);
           divBuilder.InnerHtml = messageSpan + unorderedList.ToString(TagRenderMode.Normal);

           return divBuilder.ToString();
       }
    }

}

