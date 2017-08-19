using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web.Mvc;
using ProjectBase.Utils;
using SharpArch.Web.Mvc.ModelBinder;


namespace ProjectBase.Web.Mvc
{

    public class DefaultModelBinder : SharpModelBinder
    {
        public DefaultModelBinder()
        {
            ResourceClassKey = ProjectHierarchy.ValidationMessagesResourceClassKey;
        }
        protected override void BindProperty(ControllerContext controllerContext, ModelBindingContext bindingContext,
                                             PropertyDescriptor propertyDescriptor)
        {
            base.BindProperty(controllerContext, bindingContext, propertyDescriptor);

            AddDataTypeConversionMessages(bindingContext, propertyDescriptor);

        }

        /// <summary>
        /// add datatype validation messages to modelstate to enhance validation messages for type conversion error
        /// </summary>
        /// <param name="bindingContext"></param>
        /// <param name="propertyDescriptor"></param>
        private static void AddDataTypeConversionMessages(ModelBindingContext bindingContext,
                                                          PropertyDescriptor propertyDescriptor)
        {
            // need to skip properties that aren't part of the request, else we might hit a StackOverflowException
            string fullPropertyKey = CreateSubPropertyName(bindingContext.ModelName, propertyDescriptor.Name);
            if (!bindingContext.ValueProvider.ContainsPrefix(fullPropertyKey))
            {
                return;
            }

            ModelState modelState = bindingContext.ModelState[fullPropertyKey];
            if (modelState == null) return;

            foreach (
                ModelError error in
                    modelState.Errors.Where(err => !String.IsNullOrEmpty(err.ErrorMessage) && err.Exception == null).
                        ToList())
            {
                if (error.ErrorMessage.Contains("@DataType"))
                {
                    Type numericType;
                    if (DataTypeDescription.ContainsKey(propertyDescriptor.PropertyType))
                        numericType = propertyDescriptor.PropertyType;
                    else
                    {
                        numericType = Nullable.GetUnderlyingType(propertyDescriptor.PropertyType);
                    }
                    var newMessage = error.ErrorMessage.Replace("@DataType",
                                                                DataTypeDescription[numericType]);
                    modelState.Errors.Remove(error);
                    modelState.Errors.Add(newMessage);
                }

            }

            if ((propertyDescriptor.PropertyType == typeof(decimal) || propertyDescriptor.PropertyType == typeof(decimal?)) && bindingContext.PropertyMetadata[propertyDescriptor.Name].IsRequired)
            {
                var precision = ClientDataTypeModelValidatorProvider.DefaultDecimalPrecision;
                var scale = ClientDataTypeModelValidatorProvider.DefaultDecimalScale;
                var attr = propertyDescriptor.Attributes.OfType<DecimalFormatAttribute>().FirstOrDefault();
                if (attr != null)
                {
                    precision = attr.Precision;
                    scale = attr.Scale;
                }
                
                var rex =new Regex("^(\\d{1," + (precision - scale) + "})(\\.\\d{1," + scale + "})?$");
                if (!rex.IsMatch(modelState.Value.AttemptedValue))
                {
                    var errorMessage =
                        ClientDataTypeModelValidatorProvider.NumericModelValidator.MakeErrorString(
                            ValidationMessages.NumericModelValidator_DecimalFormatError,
                            bindingContext.PropertyMetadata[propertyDescriptor.Name].GetDisplayName(), precision - scale, scale);
                    modelState.Errors.Add(errorMessage);
                }
            }
        }

        private static readonly Dictionary<Type, string> DataTypeDescription =
            new Dictionary<Type, string>
                {
                    {typeof (int), "整数"},
                    {typeof (byte), "整数"},
                    {typeof (sbyte), "整数"},
                    {typeof (short), "整数"},
                    {typeof (ushort), "整数"},
                    {typeof (uint), "整数"},
                    {typeof (long), "整数"},
                    {typeof (ulong), "整数"},
                    {typeof (float), "单精度"},
                    {typeof (double), "双精度"},
                    {typeof (decimal), "小数"}
                };

        private static readonly Dictionary<Type, string> DataTypeDescriptionWithRange =
            new Dictionary<Type, string>
                {
                    {typeof (int), int.MinValue+" 到 "+int.MaxValue+" 之间的整数"},
                    {typeof (byte), byte.MinValue+" 到 "+byte.MaxValue+" 之间的整数"},
                    {typeof (sbyte), sbyte.MinValue+" 到 "+sbyte.MaxValue+" 之间的整数"},
                    {typeof (short), short.MinValue+" 到 "+short.MaxValue+" 之间的整数"},
                    {typeof (ushort), ushort.MinValue+" 到 "+ushort.MaxValue+" 之间的整数"},
                    {typeof (uint), uint.MinValue+" 到 "+uint.MaxValue+" 之间的整数"},
                    {typeof (long),long.MinValue+" 到 "+long.MaxValue+" 之间的整数"},
                    {typeof (ulong), ulong.MinValue+" 到 "+ulong.MaxValue+" 之间的整数"},
                    {typeof (float), "单精度"},
                    {typeof (double), "双精度"},
                    {typeof (decimal), "小数"}
                };
    }
}