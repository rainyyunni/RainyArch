package projectbase.sharparch.domain.domainmodel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

    /// <summary>
	///     Facilitates indicating which property(s) describe the unique signature of an 
    ///     entity.  See Entity.GetTypeSpecificSignatureProperties() for when this is leveraged.
    /// </summary>
    /// <remarks>
    ///     This is intended for use with <see cref = "Entity" />.  It may NOT be used on a <see cref = "ValueObject" />.
    /// </remarks>
	@Retention(value = RetentionPolicy.RUNTIME)
    public @interface DomainSignature 
    {
    	
    }
