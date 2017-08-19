using System;
using NetNgArch.Domain.DomainModel.GN;
using ProjectBase.Domain;
using ProjectBase.Utils;
using SharpArch.Domain.DomainModel;

namespace NetNgArch.Domain.DomainModel.TA
{
    public class Task : BaseDomainObject
    {
        public static readonly SortStruc<Task>[] DefaultSort = new SortStruc<Task>[]
                                                                    {
                                                                        new SortStruc<Task>{
                                                                            OrderByExpression=o => o.Name,
                                                                            OrderByDirection = OrderByDirectionEnum.Asc
                                                                        }
                                                               };
        #region "persistent properties"

		[DomainSignature]
		public virtual string Name { get; set; }
		public virtual User User { get; set; }
		public virtual DateTime CreateDate { get; set; }
		public virtual DateTime? PlanBeginDate { get; set; }
		public virtual DateTime? PlanEndDate { get; set; }
		public virtual DateTime? BeginDate { get; set; }
		public virtual DateTime? EndDate { get; set; }
		public virtual StatusEnum Status { get; set; }

     
        #endregion


        #region "methods"

        #endregion

        #region "Enums"
        public enum StatusEnum
        {
            Default = 0,
            Done = 1,
            Closed = 2,
            Canceled = 3
        }
        #endregion
    }

}

