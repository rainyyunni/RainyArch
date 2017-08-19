using System;
using NetNgArch.Domain.DomainModel.GN;
using ProjectBase.Domain;
using ProjectBase.Utils;
using SharpArch.Domain.DomainModel;

namespace NetNgArch.Domain.DomainModel.TA
{
    public class TaskItem : BaseDomainObject
    {
        public static readonly SortStruc<TaskItem>[] DefaultSort = new SortStruc<TaskItem>[]
                                                                    {
                                                                        new SortStruc<TaskItem>{
                                                                            OrderByExpression=o => o.OrderNum,
                                                                            OrderByDirection = OrderByDirectionEnum.Asc
                                                                        }
                                                               };
        #region "persistent properties"

		[DomainSignature]
		public virtual Task Task { get; set; }
		[DomainSignature]
		public virtual string Brief { get; set; }
		public virtual User User { get; set; }
		public virtual string Requirement { get; set; }
		public virtual string Record { get; set; }
		public virtual string KeyInfo { get; set; }
		public virtual DateTime CreateDate { get; set; }
		public virtual DateTime? ActionDate { get; set; }
		public virtual Task.StatusEnum Status { get; set; }
		public virtual int OrderNum { get; set; }

     
        #endregion


        #region "methods"

        #endregion

        #region "Enums"

        #endregion
    }

}

