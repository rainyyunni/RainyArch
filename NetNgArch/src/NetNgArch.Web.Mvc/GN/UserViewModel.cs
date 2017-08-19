using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Linq.Expressions;

using System.Web.Mvc;
using NetNgArch.Domain.DomainModel.GN;
using ProjectBase.Domain;
using ProjectBase.Utils;
using System.ComponentModel.DataAnnotations;
using ProjectBase.Web.Mvc;

namespace NetNgArch.Web.Mvc.GN
{
    [Bind(Include = "Input")]
    public class UserListVM
    {
        public UserListVM()
        {
            ResultList = new List<ListRow>();
            Input = new ListInput(5);
        }

        public IList<ListRow> ResultList { get; set; }
        public ListInput Input { get; set; }
        public int? DeptId { get; set; }

        public class ListRow
        {
            public int Id { get; set; }
            public string Code { get; set; }
            public string Password { get; set; }
            public string Name { get; set; }
            public string CellPhone { get; set; }
            public string LoginMark { get; set; }
            public bool IsActive { get; set; }


        }
    }

    [Bind(Include = "Input")]
    public class UserEditVM
    {
        public UserEditVM()
        {
            Input = new EditInput();
        }
    	public IList<DORef<Dept>> Depts { get; set; }
    
        public EditInput Input { get; set; }

        public bool CanChangeUserFunc { get; set; }

        [DisplayName("User")]
        public class EditInput
        {
            public EditInput()
            {
                CorpFuncIds = "";
                DeptFuncIds = "";
                UserFuncIds = "";
            }

            #region "input properties"

	        public int Id { get; set; }
			[Required]
			public  DORef<Dept> Dept { get; set; }
			[Required]
			[StringLength(20)]
			public  string Code { get; set; }
			[Required]
			[StringLength(10)]
			public  string Name { get; set; }
			[StringLength(30)]
			public  string EnName { get; set; }
			[StringLength(20)]
			public  string CellPhone { get; set; }
            public bool IsActive { get; set; }
            public string CorpFuncIds { get; set; }
            public string DeptFuncIds { get; set; }
            public string UserFuncIds { get; set; }

            #endregion


        }
    }
    public class UserSearchVM 
    {
        public SearchInput Input { get; set; }
        public IList<DORef<Dept>> Depts { get; set; }
        public UserSearchVM()
        {
            Input = new SearchInput();
        }
        [DisplayName("User")]
        public class SearchInput 
        {
            public DORef<Dept> Dept { get; set; }
        }
    }

}
