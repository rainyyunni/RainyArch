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
                    {typeof (int), "����"},
                    {typeof (byte), "����"},
                    {typeof (sbyte), "����"},
                    {typeof (short), "����"},
                    {typeof (ushort), "����"},
                    {typeof (uint), "����"},
                    {typeof (long), "����"},
                    {typeof (ulong), "����"},
                    {typeof (float), "������"},
                    {typeof (double), "˫����"},
                    {typeof (decimal), "С��"}
                };

        private static readonly Dictionary<Type, string> DataTypeDescriptionWithRange =
            new Dictionary<Type, string>
                {
                    {typeof (int), int.MinValue+" �� "+int.MaxValue+" ֮�������"},
                    {typeof (byte), byte.MinValue+" �� "+byte.MaxValue+" ֮�������"},
                    {typeof (sbyte), sbyte.MinValue+" �� "+sbyte.MaxValue+" ֮�������"},
                    {typeof (short), short.MinValue+" �� "+short.MaxValue+" ֮�������"},
                    {typeof (ushort), ushort.MinValue+" �� "+ushort.MaxValue+" ֮�������"},
                    {typeof (uint), uint.MinValue+" �� "+uint.MaxValue+" ֮�������"},
                    {typeof (long),long.MinValue+" �� "+long.MaxValue+" ֮�������"},
                    {typeof (ulong), ulong.MinValue+" �� "+ulong.MaxValue+" ֮�������"},
                    {typeof (float), "������"},
                    {typeof (double), "˫����"},
                    {typeof (decimal), "С��"}
                };
    }
}