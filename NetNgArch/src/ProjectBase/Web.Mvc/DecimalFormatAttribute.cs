using System;

namespace ProjectBase.Web.Mvc
{
    [AttributeUsage(AttributeTargets.Property,AllowMultiple= false,Inherited= true)]
    public class DecimalFormatAttribute :Attribute 
    {
        public int Precision { get; set; }
        public int Scale { get; set; }
        public DecimalFormatAttribute(int precision,int scale)
        {
            Precision = precision;
            Scale = scale;
        }

    }
}
