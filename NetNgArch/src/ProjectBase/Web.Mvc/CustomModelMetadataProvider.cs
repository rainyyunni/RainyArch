using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Linq.Expressions;
using System.Reflection;
using System.Text;
using System.Web;
using System.Web.Mvc;
using ProjectBase.Utils;

namespace ProjectBase.Web.Mvc
{
    public class CustomModelMetadataProvider : DataAnnotationsModelMetadataProvider
    {
        public static string DisplayNameResourceClassKey = ProjectHierarchy.DisplayNameResourceClassKey;

        protected override ModelMetadata CreateMetadata(
        IEnumerable<Attribute> attributes,
        Type containerType,
        Func<object> modelAccessor,
        Type modelType,
        string propertyName)
        {
            ModelMetadata metadata = base.CreateMetadata(attributes, containerType,
                                                         modelAccessor, modelType, propertyName);

            //---customize display name 
            if (!string.IsNullOrEmpty(metadata.DisplayName)) return metadata;

            metadata.DisplayName = DisplayNameForProp(attributes, containerType, propertyName);

            if (string.IsNullOrEmpty(metadata.DisplayName) && propertyName != null && propertyName.EndsWith(DisplayExtension.SurfixForDisplay) && containerType != null)
            {
                metadata.DisplayName = DisplayNameForProp(null, containerType, propertyName.Replace(DisplayExtension.SurfixForDisplay, ""));
            }
            if (string.IsNullOrEmpty(metadata.DisplayName))
                metadata.DisplayName = null;//important!don't let DisplayName be empty,make it null insteadof empty.Otherwise ValidationContext.set_DisplayName would throw exception
            return metadata;
        }

        private string DisplayNameForProp(IEnumerable<Attribute> attributes,Type containerType,string propertyName)
        {
            string displayname = null;
            string resourceName = "";
            DisplayNameKeyAttribute keyattr = null;
            if (attributes!=null) 
                keyattr = attributes.OfType<DisplayNameKeyAttribute>().FirstOrDefault();
            else
            {
                keyattr = (DisplayNameKeyAttribute)Attribute.GetCustomAttribute(containerType.GetProperty(propertyName), typeof(DisplayNameKeyAttribute));
            }
            
            if (keyattr != null)
            {
                resourceName = keyattr.Key;
                displayname= HttpContext.GetGlobalResourceObject(DisplayNameResourceClassKey, resourceName) as string;
                if (!string.IsNullOrEmpty(displayname)) return displayname;
            }

            if (containerType != null)
            {
                var classAttribute =
                    Attribute.GetCustomAttribute(containerType, typeof(DisplayNameAttribute)) as
                    DisplayNameAttribute;
                if (classAttribute != null)
                    resourceName = classAttribute.DisplayName + "_";
            }
            if (propertyName != null) resourceName = resourceName + propertyName;
            displayname =
                HttpContext.GetGlobalResourceObject(DisplayNameResourceClassKey, resourceName) as string;
            if (string.IsNullOrEmpty(displayname) && propertyName!=null)
                displayname =
                    HttpContext.GetGlobalResourceObject(DisplayNameResourceClassKey, propertyName) as string;
            return displayname;
        }
    }
}
