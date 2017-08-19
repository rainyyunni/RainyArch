using System;
using System.Collections.Generic;
using ProjectBase.Domain;
using ProjectBase.Utils;
using SharpArch.Domain.DomainModel;

namespace NetNgArch.Domain.DomainModel.GN
{
    public class Func : BaseDomainObject
    {
        public static readonly SortStruc<Func>[] DefaultSort = new SortStruc<Func>[]
                                                                    {
                                                                        new SortStruc<Func>{
                                                                            OrderByExpression=o => o.Name,
                                                                            OrderByDirection = OrderByDirectionEnum.Asc
                                                                        }
                                                               };
        #region "persistent properties"

		public virtual string Name { get; set; }
		[DomainSignature]
		public virtual string Level { get; set; }
		public virtual string Code { get; set; }

        public virtual IList<Corp> Corps { get; set; }
        public virtual IList<Dept> Depts { get; set; }
        public virtual IList<User> Users { get; set; } 
     
        #endregion

        #region "methods"

        #endregion

    }

}

