﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.296
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace ProjectBase.Web.Mvc {
    using System;
    
    
    /// <summary>
    ///   A strongly-typed resource class, for looking up localized strings, etc.
    /// </summary>
    // This class was auto-generated by the StronglyTypedResourceBuilder
    // class via a tool like ResGen or Visual Studio.
    // To add or remove a member, edit your .ResX file then rerun ResGen
    // with the /str option, or rebuild your VS project.
    [global::System.CodeDom.Compiler.GeneratedCodeAttribute("System.Resources.Tools.StronglyTypedResourceBuilder", "4.0.0.0")]
    [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
    [global::System.Runtime.CompilerServices.CompilerGeneratedAttribute()]
    internal class ValidationMessages {
        
        private static global::System.Resources.ResourceManager resourceMan;
        
        private static global::System.Globalization.CultureInfo resourceCulture;
        
        [global::System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1811:AvoidUncalledPrivateCode")]
        internal ValidationMessages() {
        }
        
        /// <summary>
        ///   Returns the cached ResourceManager instance used by this class.
        /// </summary>
        [global::System.ComponentModel.EditorBrowsableAttribute(global::System.ComponentModel.EditorBrowsableState.Advanced)]
        internal static global::System.Resources.ResourceManager ResourceManager {
            get {
                if (object.ReferenceEquals(resourceMan, null)) {
                    global::System.Resources.ResourceManager temp = new global::System.Resources.ResourceManager("ProjectBase.Web.Mvc.ValidationMessages", typeof(ValidationMessages).Assembly);
                    resourceMan = temp;
                }
                return resourceMan;
            }
        }
        
        /// <summary>
        ///   Overrides the current thread's CurrentUICulture property for all
        ///   resource lookups using this strongly typed resource class.
        /// </summary>
        [global::System.ComponentModel.EditorBrowsableAttribute(global::System.ComponentModel.EditorBrowsableState.Advanced)]
        internal static global::System.Globalization.CultureInfo Culture {
            get {
                return resourceCulture;
            }
            set {
                resourceCulture = value;
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to {0} must be a date..
        /// </summary>
        internal static string DateTimeModelValidator_FormatError {
            get {
                return ResourceManager.GetString("DateTimeModelValidator_FormatError", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to filed {0} must be an email address..
        /// </summary>
        internal static string EmailAddressAttribute_FormatError {
            get {
                return ResourceManager.GetString("EmailAddressAttribute_FormatError", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to .
        /// </summary>
        internal static string NumericModelValidator_DecimalFormatError {
            get {
                return ResourceManager.GetString("NumericModelValidator_DecimalFormatError", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to {0} must be a number..
        /// </summary>
        internal static string NumericModelValidator_FormatError {
            get {
                return ResourceManager.GetString("NumericModelValidator_FormatError", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to .
        /// </summary>
        internal static string NumericModelValidator_RangeError {
            get {
                return ResourceManager.GetString("NumericModelValidator_RangeError", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to .
        /// </summary>
        internal static string NumericModelValidator_RangeMaxError {
            get {
                return ResourceManager.GetString("NumericModelValidator_RangeMaxError", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to .
        /// </summary>
        internal static string NumericModelValidator_RangeMinError {
            get {
                return ResourceManager.GetString("NumericModelValidator_RangeMinError", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to User input hasn&apos;t pass validation!.
        /// </summary>
        internal static string UserInput_NotPassValidation {
            get {
                return ResourceManager.GetString("UserInput_NotPassValidation", resourceCulture);
            }
        }
    }
}