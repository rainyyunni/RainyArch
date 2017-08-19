using System;
using System.Collections.Generic;

namespace ProjectBase.BusinessDelegate
{
    public interface IApplicationStorage
    {

        Object Get(string key);

        void Set(string key, Object obj);
        String GetAppSetting(String key);
        String GetRealPath(String relativePath);
    }
}