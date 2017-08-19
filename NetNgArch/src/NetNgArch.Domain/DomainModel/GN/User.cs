using System;
using System.Linq;
using System.Collections.Generic;
using ProjectBase.Domain;
using ProjectBase.Utils;
using SharpArch.Domain.DomainModel;

namespace NetNgArch.Domain.DomainModel.GN
{
    public class User : BaseDomainObject
    {
        public static readonly SortStruc<User>[] DefaultSort = new SortStruc<User>[]
                                                                    {
                                                                        new SortStruc<User>{
                                                                            OrderByExpression=o => o.Code,
                                                                            OrderByDirection = OrderByDirectionEnum.Asc
                                                                        }
                                                               };
        #region "persistent properties"
        public virtual Corp Corp { get; set; }
		public virtual Dept Dept { get; set; }
		[DomainSignature]
		public virtual string Code { get; set; }
		public virtual string Password { get; set; }
		public virtual string Name { get; set; }
        public virtual string CellPhone { get; set; }
        public virtual string LoginMark { get; set; }
        public virtual bool IsActive { get; set; }
        public virtual IList<Func> Funcs { get; set; }

        #endregion


        #region "methods"
        //public virtual bool CanOperate(string funcCode, Func.OperationEnum operation)
        //{
        //    var func=this.Funcs.First(f => f.Key.Code == funcCode);

        //    return !func.Equals(null) && func.Value.Contains((char)operation);

        //}
        #endregion

        #region "Enums"

        #endregion
    }

}
