using System;
using NetNgArch.Domain.DomainModel.GN;
using ProjectBase.Domain;
using ProjectBase.Utils;
using SharpArch.Domain.DomainModel;

namespace NetNgArch.Domain.DomainModel.CZ
{
    public class MemberNote : BaseDomainObject
    {
        public static readonly SortStruc<MemberNote>[] DefaultSort = new SortStruc<MemberNote>[]
                                                                    {
                                                                        new SortStruc<MemberNote>{
                                                                            OrderByExpression=o => o.Seq,
                                                                            OrderByDirection = OrderByDirectionEnum.Asc
                                                                        }
                                                               };
        #region "persistent properties"
        [DomainSignature]
		public virtual Member Member { get; set; }
        [DomainSignature]
		public virtual int Seq { get; set; }
		public virtual DateTime ActionTime { get; set; }
		public virtual string Note { get; set; }
		public virtual User User { get; set; }
		public virtual ResultEnum Result { get; set; }
		public virtual NoteClassEnum NoteClass { get; set; }
		public virtual string Memo { get; set; }
        public virtual decimal? CreditSum { get; set; }
        public virtual bool? IsActive { get; set; }
     
        #endregion


        #region "methods"

        #endregion

        #region "Enums"
        public enum ResultEnum
        {
		Untreated = 0,
		Processing = 1,
		Recored = 2
        }
        public enum NoteClassEnum
        {
		Saved = 0,
		Important1 = 1
        }
        #endregion
    }

}

