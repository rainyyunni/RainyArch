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
    public class TaskItemSearchVM 
    {

        public TaskItemSearchVM()
        {
            Input = new SearchInput();
        }
        
        public SearchInput Input { get; set; }
        
        [DisplayName("TaskItem")]
        public class SearchInput
        {
            //[todo:copy properties from Edit viewmodel]
        }
    }
    
    [Bind(Include = "Input")]
    public class TaskItemListVM
    {
        public TaskItemListVM()
        {
            ResultList=new List<ListRow>();
            Input = new ListInput(5);
        }
        
        public IList<ListRow> ResultList { get; set; }
        public ListInput Input { get; set; }

        public class ListRow
        {
        	public int Id { get; set; }
			public string TaskName  { get; set; }
			public string Brief { get; set; }
			public string UserName  { get; set; }
			public string Requirement { get; set; }
			public string Record { get; set; }
			public string KeyInfo { get; set; }
			public DateTime CreateDate { get; set; }
			public DateTime? ActionDate { get; set; }
			public Task.StatusEnum Status { get; set; }
			public int OrderNum { get; set; }


        }
	}
	
	[Bind(Include = "Input")]
    public class TaskItemEditVM 
    {
    	public TaskItemEditVM()
        {
            Input = new EditInput();
        }
		public IList<DORef<Task>> Tasks { get; set; }
		public IList<DORef<User>> Users { get; set; }
    
        public EditInput Input { get; set; }

		[DisplayName("TaskItem")]
        public class EditInput
        {
            #region "input properties"

			public int Id { get; set; }
			[Required]
			public  DORef<Task> Task { get; set; }
			[Required]
			[StringLength(30)]
			public  string Brief { get; set; }
			[Required]
			public  DORef<User> User { get; set; }
			[StringLength(200)]
			public  string Requirement { get; set; }
			[StringLength(1000)]
			public  string Record { get; set; }
			[StringLength(200)]
			public  string KeyInfo { get; set; }
			public  DateTime CreateDate { get; set; }
			public  DateTime? ActionDate { get; set; }
			public  Task.StatusEnum Status { get; set; }
			public  int OrderNum { get; set; }


            #endregion


        }

    }
}
