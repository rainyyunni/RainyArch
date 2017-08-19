using System;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Mvc;
using System.Web.Mvc.Html;
using System.Web.Script.Serialization;

namespace ProjectBase.Web.Mvc.Angular
{
    //服务器响应回到客户端后指示客户端应完成的动作
    public class RichClientJsonResult : JsonResult
    {
        public static string Key_For_ClientInfo = "_ClientInfo";
        public static String Command_Noop = "Noop";
        public static String Command_Message ="Message";
        public static String Command_Redirect="Redirect";
        public static String Command_AppPage = "AppPage";
        public static String Command_ServerVM ="ServerVM";
        public static String Command_ServerData="ServerData";
        //日期时间在Json中统一序列化为字符串，统一格式
        //public static String DateAsStringFormat = "yyyy-MM-dd HH:mm:ss";

        private bool isRcResult;
        private String command;
        private Object resultdata;
        public RichClientJsonResult(bool isRcResult, String command, Object resultdata)
        {
            this.isRcResult = isRcResult;
            this.command = command;
            this.resultdata = resultdata;
            Data = new { isRcResult, command = command ?? Command_Noop, data = resultdata };
            this.JsonRequestBehavior=JsonRequestBehavior.AllowGet;
        }
        public bool IsErrorResult
        {
            get { return !isRcResult; }
        }
        public override void ExecuteResult(ControllerContext context)
        {
            if (context == null)
            {
                throw new ArgumentNullException("context");
            }
            if (JsonRequestBehavior == JsonRequestBehavior.DenyGet &&
                String.Equals(context.HttpContext.Request.HttpMethod, "GET", StringComparison.OrdinalIgnoreCase))
            {
                throw new InvalidOperationException("JsonRequest_GetNotAllowed");
            }

            HttpResponseBase response = context.HttpContext.Response;

            if (!String.IsNullOrEmpty(ContentType))
            {
                response.ContentType = ContentType;
            }
            else
            {
                response.ContentType = "application/json";
            }
            if (ContentEncoding != null)
            {
                response.ContentEncoding = ContentEncoding;
            }
            if (command == Command_Message && resultdata == null)
            {
                resultdata = ValidationSummary(context);
                Data = new { isRcResult, command, data = resultdata };
            }
            if (Data != null)
            {
               
                string str; 
                if (context.HttpContext.Request.Params[Key_For_ClientInfo] == null)
                {
                    JavaScriptSerializer serializer = new JavaScriptSerializer();
                    str = serializer.Serialize(Data);
                    str = Regex.Replace(str, @"""\\/Date\((\d+)\)\\/""", match =>
                                                                             {
                                                                                 return match.Groups[1].Value;
                                                                             });
                }
                else
                {
                    str = Newtonsoft.Json.JsonConvert.SerializeObject(Data);
                }
                response.Write(str);
            }
        }

        public String ValidationSummary(ControllerContext context)
        {
            string messageSpan = null;
            StringBuilder htmlSummary = new StringBuilder();
            TagBuilder unorderedList = new TagBuilder("ul");

            IEnumerable<ModelState> modelStates = null;

            modelStates = context.Controller.ViewData.ModelState.Values;

            foreach (ModelState modelState in modelStates)
            {
                foreach (ModelError modelError in modelState.Errors)
                {
                    string errorText = modelError.ErrorMessage;
                    if (String.IsNullOrEmpty(errorText) && modelError.Exception != null)
                    {
                        errorText = modelError.Exception.InnerException == null ?
                            modelError.Exception.Message :
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
