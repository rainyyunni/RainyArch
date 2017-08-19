using System;
using System.Globalization;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Mvc.Ajax;
using AutoMapper;
using ProjectBase.Domain;

namespace ProjectBase.Web.Mvc
{
    //响应回到客户端后，原页面清除后执行javascript
   public class ClientScriptResult : ActionResult
    {

        public string Script
        {
            get;
            set;
        }
        public ClientScriptResult(string script)
        {
            Script = "<script type='text/javascript' language='javascript'>" + script + "</script>";
        }
        public override void ExecuteResult(ControllerContext context)
        {
            if (context == null)
            {
                throw new ArgumentNullException("context");
            }

            HttpResponseBase response = context.HttpContext.Response;

            if (Script != null)
            {
                response.Write(Script);
            }
        }
    }
}

