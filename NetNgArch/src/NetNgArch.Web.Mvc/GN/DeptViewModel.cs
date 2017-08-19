using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Linq.Expressions;

using System.Web.Mvc;
using NetNgArch.Domain.DomainModel.GN;

using ProjectBase.Utils;
using System.ComponentModel.DataAnnotations;
using ProjectBase.Web.Mvc;

namespace NetNgArch.Web.Mvc.GN
{
    [Bind(Include = "Input,EditInput")]
    public class DeptListVM
    {
        public DeptListVM()
        {
            ResultList = new List<ListRow>();
            Input = new ListInput(5);
            Input.OrderExpression = Dept.DefaultSortString;
            EditInput = new DeptEditVM.EditInput();
        }

        public IList<ListRow> ResultList { get; set; }
        public ListInput Input { get; set; }
        public DeptEditVM.EditInput EditInput { get; set; }

        public class ListRow
        {
            public int Id { get; set; }
            public string Code { get; set; }
            public string Name { get; set; }
        }
    }

    [Bind(Include = "Input")]
    public class DeptEditVM
    {
    	public DeptEditVM()
        {
            Input = new EditInput();
        }
        
        public EditInput Input { get; set; }
        public bool CanChangeDeptFunc { get; set; }
        
        public class EditInput
        {
            #region "input properties"
            public EditInput()
            {
                CorpFuncIds = "";
                DeptFuncIds = "";
            }
            public int Id { get; set; }

			[Required]
			[StringLength(10)]
			public  string Code { get; set; }
			[Required]
			[StringLength(20)]
			public  string Name { get; set; }
            public string CorpFuncIds { get; set; }
            public string DeptFuncIds { get; set; }

            #endregion


        }

    }
}

