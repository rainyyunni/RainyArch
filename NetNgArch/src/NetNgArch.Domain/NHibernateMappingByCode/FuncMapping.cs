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

    public class FuncMapping : ClassMapping<Func>, IClassByClassMapping
    {
        public FuncMapping()
        {
            Bag(o => o.Corps, cm =>
            {
                cm.Table("GN_CorpFunc");
                cm.Key(k => k.Column("FuncId"));
                cm.Inverse(true);
                cm.Cascade(Cascade.None);
            }, em => em.ManyToMany(m => m.Column("CorpId")));
            Bag(o => o.Depts, cm =>
            {
                cm.Table("GN_DeptFunc");
                cm.Key(k => k.Column("FuncId"));
                cm.Inverse(true);
                cm.Cascade(Cascade.None);
            }, em => em.ManyToMany(m => m.Column("DeptId")));
            Bag(o => o.Users, cm =>
            {
                cm.Table("GN_UserFunc");
                cm.Key(k => k.Column("FuncId"));
                cm.Inverse(true);
                cm.Cascade(Cascade.None);
            }, em => em.ManyToMany(m => m.Column("UserId")));

        }
    }
}
