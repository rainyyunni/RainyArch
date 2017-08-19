using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ProjectBase.Web.Mvc
{
    public class AuthFailureException:Exception
    {
        public AuthFailureException() : base(){}
        public AuthFailureException(string msg) : base(msg) { }

    }
}
