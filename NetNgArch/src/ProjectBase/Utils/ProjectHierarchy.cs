
namespace ProjectBase.Utils
{
    public class ProjectHierarchy
    {
        private static string _businessDelegateNS;
        private static string _dataNS;
        private static string _domainNS;
        private static string _mvcNS;

        public static string ProjectName = "App";

        public static string BusinessDelegateNS
        {
            get
            {
                if (string.IsNullOrEmpty(_businessDelegateNS))
                    return ProjectName + ".BusinessDelegate";
                else
                {
                    return _businessDelegateNS;
                }
            }
            set { _businessDelegateNS = value; }

        }
        public static string DataNS
        {
            get
            {
                if (string.IsNullOrEmpty(_dataNS))
                    return ProjectName + ".Data";
                else
                {
                    return _dataNS;
                }
            }
            set { _dataNS = value; }

        }
        public static string DomainNS
        {
            get
            {
                if (string.IsNullOrEmpty(_domainNS))
                    return ProjectName + ".Domain";
                else
                {
                    return _domainNS;
                }
            }
            set { _domainNS = value; }

        }
        public static string MvcNS
        {
            get
            {
                if (string.IsNullOrEmpty(_mvcNS))
                    return ProjectName + ".Web.Mvc";
                else
                {
                    return _mvcNS;
                }
            }
            set { _mvcNS = value; }

        }

        public static string MessagesResourceClassKey = "Messages";
        public static string ValidationMessagesResourceClassKey = "ValidationMessages";
        public static string DisplayNameResourceClassKey = "DisplayName";
    }
}
