using System;
using System.Collections.Generic;
using ProjectBase.Domain;
using ProjectBase.Utils;
using SharpArch.Domain.DomainModel;

namespace NetNgArch.Domain.DomainModel.GN
{
    public class Dept : BaseDomainObject
    {
        public static readonly string DefaultSortString = "Code";
        public static readonly SortStruc<Dept>[] DefaultSort = new SortStruc<Dept>[]
                                                                    {
                                                                        new SortStruc<Dept>{
                                                                            OrderByExpression=o => o.Code,
                                                                            OrderByDirection = OrderByDirectionEnum.Asc
                                                                        }
                                                               };
        #region "persistent properties"

		[DomainSignature]
		public virtual Corp Corp { get; set; }
		[DomainSignature]
		public virtual string Code { get; set; }
        [RefText]
		public virtual string Name { get; set; }

        public virtual IDomainList<User> Users { get; set; }
        public virtual IList<Func> Funcs { get; set; } 
     
        #endregion


        #region "methods"

        #endregion

        #region "Enums"

        #endregion
    }

}

