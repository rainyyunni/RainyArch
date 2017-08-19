using System.Web.Mvc;

namespace NetNgArch.Web.GN
{
    public class CZAreaRegistration : AreaRegistration
    {
        public override string AreaName
        {
            get
            {
                return "CZ";
            }
        }

        public override void RegisterArea(AreaRegistrationContext context)
        {
            context.MapRoute(
                "CZ_default",
                "CZ/{controller}/{action}",
                new { controller = "Member", action = "Search" },
                new string[] { "NetNgArch.Web.Mvc.CZ" }
            );
        }
    }
}
