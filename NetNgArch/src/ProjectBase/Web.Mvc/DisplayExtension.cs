using System;
using System.Collections;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Linq.Expressions;
using System.Web.Mvc;
using System.Web.Script.Serialization;
using ProjectBase.Domain;
using ProjectBase.Utils;
using System.Collections.Generic;

namespace ProjectBase.Web.Mvc
{
    public static class DisplayExtension
    {
        public static string SurfixForDisplay = "_Display";

        public static string Display(this bool? value)
        {
            if (value == null) return "";
            return value.Value ? "是" : "否";
        }

        public static string Display(this bool value)
        {
            return value ? "是" : "否";
        }

        /// <summary>
        /// 使用默认格式
        /// </summary>
        /// <param name="value"></param>
        /// <returns></returns>
        public static string Display(this DateTime value)
        {
            return Display(value, "yyyy-MM-dd");
        }

        public static string Display(this DateTime? value)
        {
            if (value == null) return "";
            return Display(value, "yyyy-MM-dd");
        }

        /// <summary>
        /// 使用指定格式，format=null表示不加任何格式
        /// </summary>
        /// <param name="value"></param>
        /// <param name="format"></param>
        /// <returns></returns>
        public static string Display(this DateTime value, string format)
        {
            return format == null ? value.ToString() : value.ToString(format);
        }

        public static string Display(this DateTime? value, string format)
        {
            if (value == null) return "";
            return format == null ? value.ToString() : value.Value.ToString(format);
        }

        public static string Display(this Decimal value)
        {
            return value.ToString();
        }

        public static string Display(this Decimal? value)
        {
            if (value == null) return "";
            return value.Value.ToString();
        }

        public static string Display(this Decimal value, string format)
        {
            return format == null ? value.ToString() : value.ToString(format);
        }

        public static string Display(this Decimal? value, string format)
        {
            if (value == null) return "";
            return format == null ? value.ToString() : value.Value.ToString(format);
        }

        public static string Display(this DORef value)
        {
            if (value == null) return "";
            return value.RefText;
        }

        public static string Display(this Enum enumValue)
        {
            if (enumValue == null) return "";
            string enumName = enumValue.GetType().ReflectedType.Name + "." + enumValue.GetType().Name + "." +
                              enumValue.ToString();
            return Util.DictMap.ContainsKey(enumName) ? Util.DictMap[enumName] : enumName;
        }

        public static Object Display(this Object value, string format)
        {
            if (value == null) return "";
            if (value is DORef) return Display((DORef) value);
            if (value is Enum) return Display((Enum) value);

            if (value is bool) return Display((bool) value);
            //if (value is bool?) return Display((bool?) value);

            if (format == null)
            {
                if (value is Decimal) return Display((Decimal) value);
                if (value is DateTime) return Display((DateTime) value);
            }
            else
            {
                if (value is Decimal) return Display((Decimal) value, format);
                //if (value is Decimal?) return Display((Decimal?) value, format);
                if (value is DateTime) return Display((DateTime) value, format);
                //if (value is DateTime?) return Display((DateTime?) value, format);
            }
            if (value is ValueType || value is string)
                return value.ToString();
            return value.GetDisplayDictionary();
        }

        public static Object Display(this Object value)
        {
            if (value is ViewDataDictionary)
            {
                var viewdata = (ViewDataDictionary) value;
                return viewdata.ModelMetadata.DisplayFormatString == null
                           ? Display(viewdata.Model)
                           : Display(viewdata.Model, viewdata.ModelMetadata.DisplayFormatString);
            }
            return Display(value, null);
        }

        private static IDictionary<string, object> GetDisplayDictionary(this object obj)
        {
            var t = obj.GetType()
                .GetProperties()
                .ToDictionary(o => o.Name,
                              o =>
                                  {
                                      var displayprop =
                                          obj.GetType().GetProperty(o.Name + DisplayExtension.SurfixForDisplay);
                                      if (displayprop != null) return displayprop.GetValue(obj, null);
                                      var attr =
                                          Attribute.GetCustomAttribute(o, typeof (DisplayFormatAttribute), true) as
                                          DisplayFormatAttribute;
                                      return attr == null
                                                 ? o.GetValue(obj, null).Display()
                                                 : o.GetValue(obj, null).Display(attr.DataFormatString);
                                  });
            return t;

        }
    }
}
