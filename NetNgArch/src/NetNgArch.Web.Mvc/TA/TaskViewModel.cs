using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq.Expressions;

using System.Web.Mvc;
using NetNgArch.Domain.DomainModel.GN;
using NetNgArch.Domain.DomainModel.TA;
using ProjectBase.Domain;
using ProjectBase.Utils;
using System.ComponentModel.DataAnnotations;
using ProjectBase.Web.Mvc;

namespace NetNgArch.Web.Mvc.TA
{
	[Bind(Include="Input")]
    public class TaskSearchVM 
    {

        public TaskSearchVM()
        {
            Input = new SearchInput();
        }
        
        public SearchInput Input { get; set; }
        
        [DisplayName("Task")]
        public class SearchInput
        {
            //[todo:copy properties from Edit viewmodel]
        }
    }
    
    [Bind(Include = "Input")]
    public class TaskListVM
    {
        public TaskListVM()
        {
            ResultList=new List<ListRow>();
            Input = new ListInput(5);
        }
        
        public IList<ListRow> ResultList { get; set; }
        public ListInput Input { get; set; }

        public class ListRow
        {
        	public int Id { get; set; }
			public string Name { get; set; }
			public string UserName  { get; set; }
			public DateTime CreateDate { get; set; }
			public DateTime? PlanBeginDate { get; set; }
			public DateTime? PlanEndDate { get; set; }
			public DateTime? BeginDate { get; set; }
			public DateTime? EndDate { get; set; }
			public Task.StatusEnum Status { get; set; }


        }
	}
	
	[Bind(Include = "Input")]
    public class TaskEditVM 
    {
    	public TaskEditVM()
        {
            Input = new EditInput();
        }
		public IList<DORef<User>> Users { get; set; }
    
        public EditInput Input { get; set; }

		[DisplayName("Task")]
        public class EditInput
        {
            #region "input properties"

			public int Id { get; set; }
			[Required]
			[StringLength(100)]
			public  string Name { get; set; }
			[Required]
			public  DORef<User> User { get; set; }
			public  DateTime CreateDate { get; set; }
			public  DateTime? PlanBeginDate { get; set; }
			public  DateTime? PlanEndDate { get; set; }
			public  DateTime? BeginDate { get; set; }
			public  DateTime? EndDate { get; set; }
			public  Task.StatusEnum Status { get; set; }


            #endregion


        }

    }
}
