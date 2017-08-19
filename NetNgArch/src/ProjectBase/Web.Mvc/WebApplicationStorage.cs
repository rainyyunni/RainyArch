using System;
using System.Collections.Generic;
using System.Web;
using System.Web.Configuration;
using ProjectBase.BusinessDelegate;

namespace ProjectBase.Web.Mvc
{


    public class WebApplicationStorage : IApplicationStorage
    {
        private readonly HttpApplicationState storage = null;

        public WebApplicationStorage(HttpApplicationState storage)
        {
            this.storage = storage;
        }
        public Object Get(string key)
        {
            return storage[key];
        }


        public void Set(string key, Object obj)
        {
            storage[key] = obj;
        }
        public String GetAppSetting(String key)
        {
            return WebConfigurationManager.AppSettings[key];
        }
        public String GetRealPath(String relativePath)
        {
            return HttpContext.Current.Server.MapPath(relativePath);
        }
    }
}