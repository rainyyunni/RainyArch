using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Mvc;
using System.Web.Script.Serialization;

namespace ProjectBase.Web.Mvc
{
    public class BaseViewModel
    {
        public string ToJsonString()
        {
            return (new JavaScriptSerializer()).Serialize(this);
        }
        public string ToJsonString(object data)
        {
            return (new JavaScriptSerializer()).Serialize(data);
        }
    }

  

}
