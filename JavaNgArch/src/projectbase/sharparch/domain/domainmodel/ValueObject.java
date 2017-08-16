package projectbase.sharparch.domain.domainmodel;

        /// <summary>
        ///     The getter for SignatureProperties for value objects should include the properties 
        ///     which make up the entirety of the Object's properties; that's part of the definition 
        ///     of a value Object.
        /// </summary>
        /// <remarks>
        ///     This ensures that the value Object has no properties decorated with the 
        ///     [DomainSignature] attribute.
        /// </remarks>
//        protected  IEnumerable<PropertyInfo> GetTypeSpecificSignatureProperties()
//        {
//            var invalidlyDecoratedProperties =
//                this.GetType().GetProperties().Where(
//                    p => Attribute.IsDefined(p, typeof(DomainSignatureAttribute), true));
//
//            String message = "Properties were found within " + this.GetType() +
//                             @" having the
//                [DomainSignature] attribute. The domain signature of a value Object includes all
//                of the properties of the Object by convention; consequently, adding [DomainSignature]
//                to the properties of a value Object's properties is misleading and should be removed. 
//                Alternatively, you can inherit from Entity if that fits your needs better.";
//
//            Check.Require(
//                !invalidlyDecoratedProperties.Any(), 
//                message);
//
//            return this.GetType().GetProperties();
//        }
//    }
