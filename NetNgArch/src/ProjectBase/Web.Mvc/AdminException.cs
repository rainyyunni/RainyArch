using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ProjectBase.Utils;

namespace ProjectBase.Web.Mvc 
{
    public class AdminException :Exception, IErrorForUser
    {
        public AdminException() : base(){}
        public AdminException(string msg) : base(msg) { }

    }
}
