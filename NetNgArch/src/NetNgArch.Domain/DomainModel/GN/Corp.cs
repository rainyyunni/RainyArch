using System;
using System.Collections.Generic;
using ProjectBase.Domain;
using ProjectBase.Utils;
using SharpArch.Domain.DomainModel;

namespace NetNgArch.Domain.DomainModel.GN
{
    public class Corp : BaseDomainObject
    {
        public static readonly SortStruc<Corp>[] DefaultSort = new SortStruc<Corp>[]
                                                                    {
                                                                        new SortStruc<Corp>{
                                                                            OrderByExpression=o => o.Name,
                                                                            OrderByDirection = OrderByDirectionEnum.Asc
                                                                        }
                                                               };
        #region "persistent properties"
        public virtual string Code { get; set; }
        [DomainSignature]
		public virtual string Name { get; set; }
        public virtual string Phone { get; set; }
        public virtual IDomainList<Dept> Depts { get; set; }
        public virtual IList<Func> Funcs { get; set; } 
        #endregion


        #region "methods"

        #endregion

        #region "Enums"

        #endregion
    }

}

