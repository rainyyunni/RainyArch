using System.Web.Mvc;

namespace NetNgArch.Web.TA
{
    public class TAAreaRegistration : AreaRegistration
    {
        public override string AreaName
        {
            get
            {
                return "TA";
            }
        }

        public override void RegisterArea(AreaRegistrationContext context)
        {
            context.MapRoute(
                "TA_default",
                "TA/{controller}/{action}",
                new { controller = "Home", action = "Welcome" },
                new string[] { "NetNgArch.Web.Mvc.TA" }
            );
        }
    }
}
