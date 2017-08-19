using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ProjectBase.Utils;

namespace ProjectBase.BusinessDelegate
{
    public class BizException :Exception, IErrorForUser
    {
        public BizException() : base(){}
        public BizException(string msg) : base(msg) { }

    }
}
