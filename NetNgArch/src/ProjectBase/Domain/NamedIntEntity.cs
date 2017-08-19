using System;
using SharpArch.Domain.DomainModel;

namespace ProjectBase.Domain
{
    [Serializable]
    public class NamedIntEntity : BaseDomainObject
    {
        #region "persistent properties"

        [DomainSignature]
        public virtual String Name { get; set; }

        #endregion

        #region "extended properties"

        #endregion

        #region "methods"


        #endregion
    }
}
