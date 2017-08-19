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

    public class UserMapping : ClassMapping<User>, IClassByClassMapping
    {
        public UserMapping()
        {

            //Map(o => o.Funcs, cm =>
            //                      {
            //                          cm.Table("GN_UserFunc");
            //                          cm.Key(k => k.Column("UserId"));

            //                      },
            //    em => em.Element(m => m.Column("Operation")));
            //don't use parameter for mapkeymanytomany above,'cause it will trigger a bug

            Bag(o => o.Funcs, cm =>
                                  {
                                      cm.Table("GN_UserFunc");
                                      cm.Key(k => k.Column("UserId"));
                                      cm.Inverse(false);
                                      cm.Cascade(Cascade.None);
                                  },em=>em.ManyToMany(m=>m.Column("FuncId")));
        }
    }
}
