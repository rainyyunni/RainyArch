using System.Web.Mvc;

namespace NetNgArch.Web.GN
{
    public class GNAreaRegistration : AreaRegistration
    {
        public override string AreaName
        {
            get
            {
                return "GN";
            }
        }

        public override void RegisterArea(AreaRegistrationContext context)
        {
            context.MapRoute(
                "GN_default",
                "GN/{controller}/{action}",
                new { controller = "Home", action = "Welcome" },
                new string[] { "NetNgArch.Web.Mvc.GN" }
            );
        }
    }
}
