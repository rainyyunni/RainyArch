using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Web.Mvc;

namespace ProjectBase.Web.Mvc
{
    public class EmailAddressAttribute : ValidationAttribute, IClientValidatable
    {
        private static readonly Regex emailRegex = new Regex(".+@.+\\..+");

        public EmailAddressAttribute()
        {
            ErrorMessageResourceType = typeof(ValidationMessages);
            ErrorMessageResourceName = "EmailAddressAttribute_FormatError";
        }

        public override bool IsValid(object value)
        {
            if (string.IsNullOrEmpty((string)value)) return true;
            return emailRegex.IsMatch((string)value);
        }

        public IEnumerable<ModelClientValidationRule> GetClientValidationRules(
        ModelMetadata metadata, ControllerContext context)
        {
            return new List<ModelClientValidationRule> {
                    new ModelClientValidationRule {
                            ValidationType = "email",
                            ErrorMessage = this.FormatErrorMessage(metadata.GetDisplayName())
                        }
                };
        }
    }
}
