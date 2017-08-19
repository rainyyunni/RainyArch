using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ProjectBase.Domain;

namespace ProjectBase.Domain
{
    public class DORef
    {
        public int? Id { get; set; }
        public string RefText { get; set; }
    }
    public class DORef<T> :DORef where T : BaseDomainObject 
    {

        public T ToReferencedDO(ICommonBD<T> bd)
        {
            return bd.Get(Id);
        }

        public DORef()
        {
        }
        public DORef(int id)
        {
            Id = id;
        }
        public DORef(int id,string refText)
        {
            Id = id;
            RefText = refText;
        }
        public DORef(T entity)
        {
            Id = entity.Id;
            RefText = entity.RefText;
        }
    }

}
