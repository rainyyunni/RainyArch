using System;
using System.Globalization;
using System.Web;


namespace ProjectBase.Utils
{
    public class Res
    {
        public static string M(string resourceName)
        {
            var s = Messages(resourceName);
            if (s == null || s.Contains("{0}"))
                s = DisplayName(resourceName);

            return s ?? resourceName;
        }
        public static string M(string resourceName, string displayResourceName)
        {
            return Messages(resourceName, DisplayName(displayResourceName));
        }
        public static string M(string resourceName, string displayResourceName0, string displayResourceName1)
        {
            return Messages(resourceName, DisplayName(displayResourceName0), DisplayName(displayResourceName1));
        }
        public static string M(string resourceName, string displayResourceName0, string displayResourceName1, string displayResourceName2)
        {
            return Messages(resourceName, DisplayName(displayResourceName0), DisplayName(displayResourceName1),DisplayName(displayResourceName1));
        }
        public static string V(string resourceName)
        {
            var s = ValidationMessages(resourceName);
            if (s == null || s.Contains("{0}"))
                s = DisplayName(resourceName);

            return s ?? resourceName;
        }
        public static string V(string resourceName, string displayResourceName)
        {
            return ValidationMessages(resourceName, DisplayName(displayResourceName));
        }
        public static string V(string resourceName, string displayResourceName0, string displayResourceName1)
        {
            return ValidationMessages(resourceName, DisplayName(displayResourceName0), DisplayName(displayResourceName1));
        }
        public static string V(string resourceName, string displayResourceName0, string displayResourceName1, string displayResourceName2)
        {
            return ValidationMessages(resourceName, DisplayName(displayResourceName0), DisplayName(displayResourceName1), DisplayName(displayResourceName1));
        }
        public static string D(string resourceName)
        {
            return DisplayName(resourceName);
        }
        public static string DisplayName(string resourceName)
        {
            var value=HttpContext.GetGlobalResourceObject(ProjectHierarchy.DisplayNameResourceClassKey, resourceName,
                                                    CultureInfo.CurrentCulture) as string;
            if (value == null)
                value = resourceName;
            return value;
        }
        public static string Messages(string resourceName, params object[] args)
        {
            var s=HttpContext.GetGlobalResourceObject(ProjectHierarchy.MessagesResourceClassKey, resourceName,
                                                    CultureInfo.CurrentCulture) as string;
            if (s == null) s = "Messages:" + resourceName;//throw new NetArchException("Need resource file: " + ProjectHierarchy.MessagesResourceClassKey + " and resourceName: " + resourceName);
            return String.Format(s,args);
        }
        public static string ValidationMessages(string resourceName, params object[] args)
        {
            var s = HttpContext.GetGlobalResourceObject(ProjectHierarchy.ValidationMessagesResourceClassKey, resourceName,
                                        CultureInfo.CurrentCulture) as string;
            if (s == null) s = "ValidationMessages:"+resourceName;//throw new NetArchException("Need resource file: " + ProjectHierarchy.ValidationMessagesResourceClassKey + " and resourceName: " + resourceName);
            return String.Format(s, args);
        }
    }

 
}