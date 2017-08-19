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

    public class CorpMapping : ClassMapping<Corp>, IClassByClassMapping
    {
        public CorpMapping()
        {
           Bag(o => o.Funcs, cm =>
                                  {
                                      cm.Table("GN_CorpFunc");
                                      cm.Key(k => k.Column("CorpId"));
                                      cm.Inverse(false);
                                      cm.Cascade(Cascade.None);

                                  }, em => em.ManyToMany(m => m.Column("FuncId")));
        }
    }
}
