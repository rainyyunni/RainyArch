using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Web.Mvc;

namespace ProjectBase.Web.Mvc.Angular
{
    public class ClientDataTypeModelValidatorProvider : ModelValidatorProvider
    {

        private static readonly HashSet<Type> _numericTypes = new HashSet<Type>(new Type[] {
        typeof(byte), typeof(sbyte),
        typeof(short), typeof(ushort),
        typeof(int), typeof(uint),
        typeof(long), typeof(ulong),
        typeof(float), typeof(double), typeof(decimal)
        });
        private static readonly HashSet<Type> _integralTypes = new HashSet<Type>(new Type[] {
        typeof(byte), typeof(sbyte),
        typeof(short), typeof(ushort),
        typeof(int), typeof(uint),
        typeof(long), typeof(ulong)
        });

        public static int DefaultDecimalPrecision = 18;
        public static int DefaultDecimalScale = 2;
       
        public override IEnumerable<ModelValidator> GetValidators(ModelMetadata metadata, ControllerContext context)
        {
            if (metadata == null)
            {
                throw new ArgumentNullException("metadata");
            }
            if (context == null)
            {
                throw new ArgumentNullException("context");
            }

            return GetValidatorsImpl(metadata, context);
        }

        private static IEnumerable<ModelValidator> GetValidatorsImpl(ModelMetadata metadata, ControllerContext context)
        {
            Type type = metadata.ModelType;
            if (IsNumericType(type))
            {
                yield return new NumericModelValidator(metadata, context);
            }
            if ((metadata.ModelType == typeof(DateTime) || metadata.ModelType == typeof(DateTime?)))
            {
                yield return new DateTimeModelValidator(metadata, context);
            }
            if ((metadata.ModelType == typeof(string)))
            {
                yield return new StringModelValidator(metadata, context);
            }

        }

        private static bool IsNumericType(Type type)
        {
            Type underlyingType = Nullable.GetUnderlyingType(type); // strip off the Nullable<>
            return _numericTypes.Contains(underlyingType ?? type);
        }
        private static bool IsIntegralType(Type type)
        {
            Type underlyingType = Nullable.GetUnderlyingType(type); // strip off the Nullable<>
            return _integralTypes.Contains(underlyingType ?? type);
        }
        internal sealed class NumericModelValidator : ModelValidator
        {
            private ModelMetadata _metadata;
            public NumericModelValidator(ModelMetadata metadata, ControllerContext controllerContext)
                : base(metadata, controllerContext)
            {
                _metadata = metadata;
            }

            public override IEnumerable<ModelClientValidationRule> GetClientValidationRules()
            {
                ModelClientValidationRule rule = null;
                long min=long.MinValue ,max=long.MaxValue ;

                if (_metadata.ContainerType != null)
                {
                    //add default range for integral number
                    if (IsIntegralType(_metadata.ModelType)
                        &&
                        !Attribute.IsDefined(_metadata.ContainerType.GetProperty(_metadata.PropertyName),
                                             typeof (RangeAttribute), true))
                    {
                        if (_metadata.ModelType == typeof (long))
                        {
                            min = long.MinValue;
                            max = long.MaxValue;
                        }
                        else if (_metadata.ModelType == typeof (int))
                        {
                            min = int.MinValue;
                            max = int.MaxValue;
                        }
                        else if (_metadata.ModelType == typeof (short))
                        {
                            min = short.MinValue;
                            max = short.MaxValue;
                        }

                        rule = new ModelClientValidationRangeRule("NumericModelValidator_RangeError", min, max);
                    }


                    //add default edit format for decimal
                    if (_metadata.ModelType == typeof(decimal) || _metadata.ModelType == typeof(decimal?))
                    {
                        var precision = DefaultDecimalPrecision;
                        var scale = DefaultDecimalScale;

                        var attr =
                            Attribute.GetCustomAttribute(
                                _metadata.ContainerType.GetProperty(_metadata.PropertyName),
                                typeof(DecimalFormatAttribute), true) as DecimalFormatAttribute;
                        if (attr!=null){
                            precision = attr.Precision;
                            scale=attr.Scale;
                        }

                       // var rex = "^(\\d{1," + (precision - scale) + "})(\\.\\d{1," + scale + "})?$";
                        var rex = "^(\\d{1," + (precision - scale) + "})";
                        if (scale > 0) rex = rex + "(\\.\\d{1," + scale + "})?";
                        rex = rex + "$";
                        rule = new ModelClientValidationRegexRule("NumericModelValidator_DecimalFormatError", rex);
                    }
                }
                var rule0 = new ModelClientValidationRule()
                                {
                                    ValidationType = "number",
                                    ErrorMessage ="NumericModelValidator_FormatError"
                                };
                if (rule==null)
                    return new [] {rule0};
                else
                    return new [] { rule0,rule };
            }

            //public static string MakeErrorString(string resourceName, string displayName,long param1,long param2)
            //{
            //    // use CurrentCulture since this message is intended for the site visitor
            //    return String.Format(CultureInfo.CurrentCulture, resourceName, displayName, param1, param2);
            //}

            public override IEnumerable<ModelValidationResult> Validate(object container)
            {
                // this is not a server-side validator，see ProjectBase.Web.Mvc.DefaultModelBinder for serverside validation for datatype
                return Enumerable.Empty<ModelValidationResult>();
            }
        }

        internal sealed class DateTimeModelValidator : ModelValidator
        {
            public DateTimeModelValidator(ModelMetadata metadata, ControllerContext controllerContext)
                : base(metadata, controllerContext)
            {
            }

            public override IEnumerable<ModelClientValidationRule> GetClientValidationRules()
            {
                ModelClientValidationRule rule = new ModelClientValidationRule()
                                                     {
                                                         ValidationType = "date",
                                                         ErrorMessage = ""
                                                     };

                return new [] {rule};
            }

            //private static string MakeErrorString(string displayName)
            //{
            //    // use CurrentCulture since this message is intended for the site visitor
            //    return String.Format(CultureInfo.CurrentCulture, ValidationMessages.DateTimeModelValidator_FormatError,
            //                         displayName);
            //}

            public override IEnumerable<ModelValidationResult> Validate(object container)
            {
                // this is not a server-side validator
                return Enumerable.Empty<ModelValidationResult>();
            }
        }
        internal sealed class StringModelValidator : ModelValidator
        {
            private ModelMetadata _metadata;
            public StringModelValidator(ModelMetadata metadata, ControllerContext controllerContext)
                : base(metadata, controllerContext)
            {
                _metadata = metadata;
            }

            public override IEnumerable<ModelClientValidationRule> GetClientValidationRules()
            {
                ModelClientValidationRule rule = null;
 
                if (_metadata.ContainerType != null)
                {
                    //add default edit format for decimal string
                    var attr =
    Attribute.GetCustomAttribute(
        _metadata.ContainerType.GetProperty(_metadata.PropertyName),
        typeof(DecimalFormatAttribute), true) as DecimalFormatAttribute;
                    if (attr != null)
                    {
                        var precision = attr.Precision;
                        var scale = attr.Scale;
                        var rex = "^(\\d{1," + (precision - scale) + "})";
                        if (scale > 0) rex = rex + "(\\.\\d{1," + scale + "})?";
                        rex = rex + "$";
                        rule = new ModelClientValidationRegexRule("NumericModelValidator_DecimalFormatError", rex);
                    }
                }

                if (rule==null)
                    return new ModelClientValidationRule[] {};
                else
                    return new [] {rule };
            }
            //public static string MakeErrorString(string resourceName, string displayName, long param1, long param2)
            //{
            //    // use CurrentCulture since this message is intended for the site visitor
            //    return String.Format(CultureInfo.CurrentCulture, resourceName, displayName, param1, param2);
            //}
            public override IEnumerable<ModelValidationResult> Validate(object container)
            {
                // this is not a server-side validator
                return Enumerable.Empty<ModelValidationResult>();
            }
        }

    }

}

