using System;
using NetNgArch.Domain.DomainModel.GN;
using ProjectBase.Domain;
using ProjectBase.Utils;
using SharpArch.Domain.DomainModel;

namespace NetNgArch.Domain.DomainModel.CZ
{
    public class Member : BaseDomainObject
    {
        public static readonly SortStruc<Member>[] DefaultSort = new SortStruc<Member>[]
                                                                    {
                                                                        new SortStruc<Member>{
                                                                            OrderByExpression=o => o.Name,
                                                                            OrderByDirection = OrderByDirectionEnum.Asc
                                                                        }
                                                               };
        #region "persistent properties"
        [RefText]
		public virtual string NickName { get; set; }
		public virtual string Name { get; set; }
        [DomainSignature]
		public virtual string Phone { get; set; }
		public virtual Corp Corp { get; set; }
		public virtual Member FromMember { get; set; }
		public virtual MemberClassEnum MemberClass { get; set; }
		public virtual DateTime JoinDate { get; set; }
		public virtual JoinWayEnum JoinWay { get; set; }
		public virtual string WeiXinCode { get; set; }
		public virtual string WeiXinName { get; set; }
		public virtual DateTime? WeiXinSubscribeDate { get; set; }
		public virtual string Password { get; set; }
		public virtual string Memo { get; set; }

     
        #endregion


        #region "methods"

        #endregion

        #region "Enums"
        public enum MemberClassEnum
        {
            Credit = 0,
            Debit = 1
        }
        public enum JoinWayEnum
        {
            Credit = 0,
            Debit = 1
        }
        #endregion
    }

}

