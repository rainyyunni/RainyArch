using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NetNgArch.Domain.DomainModel.GN;
using NHibernate.Mapping.ByCode;
using NHibernate.Mapping.ByCode.Conformist;
using ProjectBase.Domain.NhibernateMapByCode;

namespace NetNgArch.Domain.NHibernateMappingByCode
{

    public class DeptMapping : ClassMapping<Dept>, IClassByClassMapping
    {
        public DeptMapping()
        {
            Bag(o => o.Funcs, cm =>
            {
                cm.Table("GN_DeptFunc");
                cm.Key(k => k.Column("DeptId"));
                cm.Inverse(false);
                cm.Cascade(Cascade.None);
            }, em => em.ManyToMany(m => m.Column("FuncId")));

        }
    }
}
