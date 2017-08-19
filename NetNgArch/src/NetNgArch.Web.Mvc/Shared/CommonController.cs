
using System.Web.Mvc;
using NetNgArch.Domain.BDInterface;
using ProjectBase.Utils;

namespace NetNgArch.Web.Mvc.Shared
{
    public class CommonController : AppBaseController
    {
        public IAdminBD AdminBD { get; set; }

        protected override void OnActionExecuting(ActionExecutingContext filterContext)
        {
            return;//to ensure every request go through the action
        }
        public ActionResult FuncList()
        {
            var html=new MvcHtmlString(Util.FuncTree);
            return PartialView("~/Shared/Directive/FuncList.cshtml",html);
        }
        public new ActionResult AuthFailure()
        {
            return base.AuthFailure();
        }
        public ActionResult NgControllerJs()
        {
            return JavaScript(Util.NgControllerJs());
        }
        public ActionResult CheckUnique(string name, string value)
        {
            return JsonGet(name==value);
        }
        public ActionResult test()
        {
            return JavaScript(Util.test());
        }
    }
}
