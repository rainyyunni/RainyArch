using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ProjectBase.Utils
{
    public class NetArchException:Exception
    {
        public NetArchException() : base(){}
        public NetArchException(string msg) : base(msg) { }

    }

    public interface IErrorForUser
    {
    }
}
