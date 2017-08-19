using ProjectBase.Utils;
using System;
using SharpArch.Domain.DomainModel;

namespace ProjectBase.Domain
{
    /// <summary>
    /// 
    /// </summary>
    [Serializable]
    public abstract class BaseDomainObject : BaseDomainObjectWithTypedId<int>
    {
    }

    [Serializable]
    public abstract class BaseDomainObjectWithTypedId<TId> : EntityWithTypedId<TId>
    {
        public virtual string RefText { get; protected set; }

        public override string ToString()
        {
            string s = "";
            var signatures = GetTypeSpecificSignatureProperties();
            foreach (var prop in signatures)
            {
                s += prop.GetValue(this, null) + "-";
            }
            if (s=="") return base.ToString();
            return s.Remove(s.Length - 1);
        }

    }


    
}