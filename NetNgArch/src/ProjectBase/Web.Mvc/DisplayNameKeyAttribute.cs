using System;

namespace ProjectBase.Web.Mvc
{
    [AttributeUsage(AttributeTargets.Property,AllowMultiple= false,Inherited= true)]
    public class DisplayNameKeyAttribute :Attribute 
    {
        public string Key { get; set; }
        public DisplayNameKeyAttribute(string key)
        {
            Key = key;
        }

    }
}
