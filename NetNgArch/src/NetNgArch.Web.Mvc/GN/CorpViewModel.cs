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
	[Bind(Include = "Input")]
    public class CorpEditVM 
    {
    	public CorpEditVM()
        {
            Input = new EditInput();
        }
    
        public EditInput Input { get; set; }

		[DisplayName("Corp")]
        public class EditInput
        {
            #region "input properties"

			public int Id { get; set; }
            [Required]
			[StringLength(10)]
			public  string Code { get; set; }
			[Required]
			[StringLength(50)]
			public  string Name { get; set; }
			[StringLength(20)]
			public  string Phone { get; set; }


            #endregion


        }

    }
}
