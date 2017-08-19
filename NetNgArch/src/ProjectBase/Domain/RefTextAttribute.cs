using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ProjectBase.Domain
{
    [AttributeUsage(AttributeTargets.Property,AllowMultiple=false,Inherited=true)]
    public class RefTextAttribute : Attribute
    {
        public RefTextAttribute()
        {

        }
    }
}
