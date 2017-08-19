using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Mvc;

namespace ProjectBase.Web.Mvc
{

    public sealed class CustomRazorViewEngine : RazorViewEngine
    {

        public CustomRazorViewEngine()
        {
            AreaViewLocationFormats = new[] {
                "~/{2}/{1}/{0}.cshtml",
                "~/{2}/{1}/{0}.vbhtml",
                "~/{2}/Shared/{0}.cshtml",
                "~/{2}/Shared/{0}.vbhtml"
            };
            AreaMasterLocationFormats = new[] {
                "~/{2}/{1}/{0}.cshtml",
                "~/{2}/{1}/{0}.vbhtml",
                "~/{2}/Shared/{0}.cshtml",
                "~/{2}/Shared/{0}.vbhtml"
            };
            AreaPartialViewLocationFormats = new[] {
                "~/{2}/{1}/{0}.cshtml",
                "~/{2}/{1}/{0}.vbhtml",
                "~/{2}/Shared/{0}.cshtml",
                "~/{2}/Shared/{0}.vbhtml"
            };

            ViewLocationFormats = new[] {
                "~/{1}/{0}.cshtml",
                "~/{1}/{0}.vbhtml",
                "~/Shared/{0}.cshtml",
                "~/Shared/{0}.vbhtml"
            };
            MasterLocationFormats = new[] {
                "~/{1}/{0}.cshtml",
                "~/{1}/{0}.vbhtml",
                "~/Shared/{0}.cshtml",
                "~/Shared/{0}.vbhtml"
            };
            PartialViewLocationFormats = new[] {
                "~/{1}/{0}.cshtml",
                "~/{1}/{0}.vbhtml",
                "~/Shared/{0}.cshtml",
                "~/Shared/{0}.vbhtml"
            };

            FileExtensions = new[] {
                "cshtml",
                "vbhtml",
            };
        }
    }

}
