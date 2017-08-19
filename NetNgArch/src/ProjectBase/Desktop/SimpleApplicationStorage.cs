using System;
using System.Collections.Generic;
using System.Configuration;
using ProjectBase.BusinessDelegate;
using System.Windows.Forms;
namespace ProjectBase.Desktop
{


    public class SimpleApplicationStorage : IApplicationStorage
    {
        private readonly Dictionary<string, object> storage = new Dictionary<string, object>();


        public Object Get(string key)
        {
            Object obj;

            if (!this.storage.TryGetValue(key, out obj))
            {
                return null;
            }

            return obj;
        }


        public void Set(string key, Object obj)
        {
            this.storage[key] = obj;
        }
        public String GetAppSetting(String key)
        {
            return ConfigurationManager.AppSettings[key];
        }
        public String GetRealPath(String relativePath)
        {
            return  Application.StartupPath + relativePath;
        }
    }
}