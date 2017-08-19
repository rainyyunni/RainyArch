using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NHibernate;

namespace NetNgArch.Domain.NHibernateMappingByCode
{
    public class MyInterceptor : EmptyInterceptor
    {

        public override NHibernate.SqlCommand.SqlString OnPrepareStatement(NHibernate.SqlCommand.SqlString sql)
        {
            return base.OnPrepareStatement(sql);

        }

    } 
}
