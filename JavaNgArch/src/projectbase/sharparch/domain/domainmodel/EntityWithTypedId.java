package projectbase.sharparch.domain.domainmodel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

    /// <summary>
    ///     For a discussion of this Object, see 
    ///     http://devlicio.us/blogs/billy_mccafferty/archive/2007/04/25/using-equals-gethashcode-effectively.aspx
    /// </summary>
    public abstract class EntityWithTypedId<TId>  extends BaseObject implements  IEntityWithTypedId<TId>
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/// <summary>
        ///     To help ensure hashcode uniqueness, a carefully selected random number multiplier 
        ///     is used within the calculation.  Goodrich and Tamassia's Data Structures and
        ///     Algorithms in Java asserts that 31, 33, 37, 39 and 41 will produce the fewest number
        ///     of collissions.  See http://computinglife.wordpress.com/2008/11/20/why-do-hash-functions-use-prime-numbers/
        ///     for more information.
        /// </summary>
        private final int HashMultiplier = 31;

        private Integer cachedHashcode;

        /// <summary>
        ///     Id may be of type String, int, custom type, etc.
        ///     Setter is protected to allow unit tests to set this property via reflection and to allow 
        ///     domain objects more flexibility in setting this for those objects with assigned Ids.
        ///     It's virtual to allow NHibernate-backed objects to be lazily loaded.
        /// 
        ///     This is ignored for XML serialization because it does not have a public setter (which is very much by design).
        ///     See the FAQ within the documentation if you'd like to have the Id XML serialized.
        /// </summary>

        //[JsonProperty]
        private TId _id;
        public TId getId(){
        	return _id;
        }
        protected void setId(TId value){
        	_id=value;
        }

        public boolean equals(Object obj)
        {
        	EntityWithTypedId<TId> compareTo = (EntityWithTypedId<TId>)obj;

            if (this==compareTo)
            {
                return true;
            }

            if (compareTo == null || !this.getClass().equals(compareTo.GetTypeUnproxied()))
            {
                return false;
            }

            if (this.HasSameNonDefaultIdAs(compareTo))
            {
                return true;
            }

            // Since the Ids aren't the same, both of them must be transient to 
            // compare domain signatures; because if one is transient and the 
            // other is a persisted entity, then they cannot be the same Object.
            return this.IsTransient() && compareTo.IsTransient() && this.HasSameObjectSignatureAs(compareTo);
        }

        public int hashCode()
        {
            if (this.cachedHashcode!=null)
            {
                return this.cachedHashcode.intValue();
            }

            if (this.IsTransient())
            {
                this.cachedHashcode = super.hashCode();
            }
            else
            {

                    // It's possible for two objects to return the same hash code based on 
                    // identically valued properties, even if they're of two different types, 
                    // so we include the Object's type in the hash calculation
                    int hashCode = this.getClass().hashCode();
                    this.cachedHashcode = (hashCode * HashMultiplier) ^ this.getId().hashCode();

            }

            return this.cachedHashcode.intValue();
        }

        /// <summary>
        ///     Transient objects are not associated with an item already in storage.  For instance,
        ///     a Customer is transient if its Id is 0.  It's virtual to allow NHibernate-backed 
        ///     objects to be lazily loaded.
        /// </summary>
        public boolean IsTransient()
        {
            return this.getId() == null || this.getId().equals(0);
        }

        /// <summary>
        ///     The property getter for SignatureProperties should ONLY compare the properties which make up 
        ///     the "domain signature" of the Object.
        /// 
        ///     If you choose NOT to override this method (which will be the most common scenario), 
        ///     then you should decorate the appropriate property(s) with [DomainSignature] and they 
        ///     will be compared automatically.  This is the preferred method of managing the domain
        ///     signature of entity objects.
        /// </summary>
        /// <remarks>
        ///     This ensures that the entity has at least one property decorated with the 
        ///     [DomainSignature] attribute.
        /// </remarks>
        protected Iterable<Method> GetTypeSpecificSignatureProperties()
        {
        	List<Method> props=new ArrayList<Method>();
            for(Method property:this.getClass().getMethods()){
            	if(property.getAnnotation(DomainSignature.class)!=null)
            		props.add(property);
            }
            return props;
        }
        /// <summary>
        ///     Returns true if self and the provided entity have the same Id values 
        ///     and the Ids are not of the default Id value
        /// </summary>
        private boolean HasSameNonDefaultIdAs(EntityWithTypedId<TId> compareTo)
        {
            return !this.IsTransient() && !compareTo.IsTransient() && this.getId().equals(compareTo.getId());
        }
    }
