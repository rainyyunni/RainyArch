package projectbase.sharparch.domain.domainmodel;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/// <summary>
///     Provides a standard base class for facilitating comparison of objects.
/// 
///     For a discussion of the implementation of Equals/GetHashCode, see 
///     http://devlicio.us/blogs/billy_mccafferty/archive/2007/04/25/using-equals-gethashcode-effectively.aspx
///     and http://groups.google.com/group/sharp-architecture/browse_thread/thread/f76d1678e68e3ece?hl=en for 
///     an in depth and conclusive resolution.
/// </summary>
public abstract class BaseObject implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	// / <summary>
	// / To help ensure hashcode uniqueness, a carefully selected random number
	// multiplier
	// / is used within the calculation. Goodrich and Tamassia's Data Structures
	// and
	// / Algorithms in Java asserts that 31, 33, 37, 39 and 41 will produce the
	// fewest number
	// / of collissions. See
	// http://computinglife.wordpress.com/2008/11/20/why-do-hash-functions-use-prime-numbers/
	// / for more information.
	// / </summary>
	private final int HashMultiplier = 31;

	// / <summary>
	// / This static member caches the domain signature properties to avoid
	// looking them up for
	// / each instance of the same type.
	// /
	// / A description of the very slick ThreadStatic attribute may be found at
	// /
	// http://www.dotnetjunkies.com/WebLog/chris.taylor/archive/2005/08/18/132026.aspx
	// / </summary>
	// [ThreadStatic]
	private static Map<Class<?>, Iterable<Method>> signaturePropertiesDictionary;

	public boolean equals(Object obj) {
		BaseObject compareTo = (BaseObject) obj;

		if (this == compareTo) {
			return true;
		}

		return compareTo != null
				&& this.getClass().equals(compareTo.GetTypeUnproxied())
				&& this.HasSameObjectSignatureAs(compareTo);
	}

	// / <summary>
	// / This is used to provide the hashcode identifier of an Object import the
	// signature
	// / properties of the Object; although it's necessary for NHibernate's use,
	// this can
	// / also be useful for business logic purposes and has been included in
	// this base
	// / class, accordingly. Since it is recommended that GetHashCode change
	// infrequently,
	// / if at all, in an Object's lifetime, it's important that properties are
	// carefully
	// / selected which truly represent the signature of an Object.
	// / </summary>
	public int hashCode() {

		Collection<Method> signatureProperties = (Collection<Method>) this
				.GetSignatureProperties();

		// It's possible for two objects to return the same hash code based on
		// identically valued properties, even if they're of two different
		// types,
		// so we include the Object's type in the hash calculation
		int hashCode0 = this.getClass().hashCode();

		int hashCode = 0;

		hashCode = (int) signatureProperties
				.stream()
				.map(property -> {
					try {
						return property.invoke(this);
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage());
					}
				})
				.filter(value -> value != null)
				.reduce(hashCode0,
						(current, value) -> ((int) current * HashMultiplier)
								^ value.hashCode());

		if (!signatureProperties.isEmpty()) {
			return hashCode;
		}

		// If no properties were flagged as being part of the signature of the
		// Object,
		// then simply return the hashcode of the base Object as the hashcode.
		return super.hashCode();

	}

	// / <summary>
	// / </summary>
	public Iterable<Method> GetSignatureProperties() {
		Iterable<Method> properties;

		// Init the signaturePropertiesDictionary here due to reasons described
		// at
		// http://blogs.msdn.com/jfoscoding/archive/2006/07/18/670497.aspx
		if (signaturePropertiesDictionary == null) {
			signaturePropertiesDictionary = new HashMap<Class<?>, Iterable<Method>>();
		}

		if (!signaturePropertiesDictionary.containsKey(this.getClass()))
			signaturePropertiesDictionary.put(this.getClass(),
					this.GetTypeSpecificSignatureProperties());
		properties = signaturePropertiesDictionary.get(this.getClass());
		return properties;
	}

	// / <summary>
	// / You may override this method to provide your own comparison routine.
	// / </summary>
	public boolean HasSameObjectSignatureAs(BaseObject compareTo) {
		Collection<Method> signatureProperties = (Collection<Method>) this
				.GetSignatureProperties();
		try {
			for (Method property : signatureProperties) {
				Object valueOfThisObject = property.invoke(this);
				Object valueToCompareTo = property.invoke(compareTo);
				if ((valueOfThisObject != null && valueOfThisObject == null)
						|| (valueOfThisObject == null && valueToCompareTo != null)
						|| (!valueOfThisObject.equals(valueToCompareTo)))
					return false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		// If we've gotten this far and signature properties were found, then we
		// can
		// assume that everything matched; otherwise, if there were no signature
		// properties, then simply return the default bahavior of Equals
		return !signatureProperties.isEmpty() || super.equals(compareTo);
	}

	// / <summary>
	// / Enforces the template method pattern to have child objects determine
	// which specific
	// / properties should and should not be included in the Object signature
	// comparison. Note
	// / that the the BaseObject already takes care of performance caching, so
	// this method
	// / shouldn't worry about caching...just return the goods man!
	// / </summary>
	protected abstract Iterable<Method> GetTypeSpecificSignatureProperties();

	// / <summary>
	// / When NHibernate proxies objects, it masks the type of the actual entity
	// Object.
	// / This wrapper burrows into the proxied Object to get its actual type.
	// /
	// / Although this assumes NHibernate is being used, it doesn't require any
	// NHibernate
	// / related dependencies and has no bad side effects if NHibernate isn't
	// being used.
	// /
	// / Related discussion is at
	// http://groups.google.com/group/sharp-architecture/browse_thread/thread/ddd05f9baede023a
	// ...thanks Jay Oliver!
	// / </summary>
	protected Class<?> GetTypeUnproxied() {
		return this.getClass();
	}
}
