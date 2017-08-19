using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq.Expressions;

using System.Web.Mvc;
using NetNgArch.Domain.DomainModel.CZ;
using NetNgArch.Domain.DomainModel.GN;
using ProjectBase.Domain;
using ProjectBase.Utils;
using System.ComponentModel.DataAnnotations;
using ProjectBase.Web.Mvc;

namespace NetNgArch.Web.Mvc.CZ
{
	[Bind(Include="Input")]
    public class MemberNoteSearchVM 
    {

        public MemberNoteSearchVM()
        {
            Input = new SearchInput();
        }
        
        public SearchInput Input { get; set; }
        public IList<DORef<Member>> Members { get; set; }
        [DisplayName("MemberNote")]
        public class SearchInput
        {
            public DORef<Member> Member { get; set; }
            public int? Seq { get; set; }
            [StringLength(500)]
            public string Note { get; set; }
            public virtual bool? IsActive { get; set; }
        }
    }

    [Bind(Include = "Input")]
    public class MemberNoteListVM
    {
        public MemberNoteListVM()
        {
            ResultList=new List<ListRow>();
            Input = new ListInput(5);
        }
        
        public IList<ListRow> ResultList { get; set; }
        public ListInput Input { get; set; }

        public class ListRow
        {
            public int Id { get; set; }
            public string MemberName { get; set; }
            public int Seq { get; set; }
            public DateTime ActionTime { get; set; }
            public string UserName { get; set; }
            public string Note { get; set; }
            public MemberNote.ResultEnum Result { get; set; }
            public MemberNote.NoteClassEnum NoteClass { get; set; }
            public string Memo { get; set; }
            public virtual bool? IsActive { get; set; }
            public virtual decimal? CreditSum { get; set; }
            //public string Result_Display { get { return Result.Display(); } }
            //public string NoteClass_Display { get { return NoteClass.Display(); } }

        }
    }

	[Bind(Include = "Input")]
    public class MemberNoteEditVM 
    {
    	public MemberNoteEditVM()
        {
            Input = new EditInput();
        }
    	public IList<DORef<Member>> Members { get; set; }
		public IList<DORef<User>> Users { get; set; }
    
        public EditInput Input { get; set; }

		[DisplayName("MemberNote")]
        public class EditInput
        {
            #region "input properties"

			public int Id { get; set; }
			[Required]
			public  DORef<Member> Member { get; set; }
			public  int Seq { get; set; }
			public  DateTime ActionTime { get; set; }
			[Required]
			[StringLength(500)]
			public  string Note { get; set; }
			[Required]
			public  DORef<User> User { get; set; }
			public  MemberNote.ResultEnum Result { get; set; }
			public  MemberNote.NoteClassEnum NoteClass { get; set; }
			[StringLength(100)]
			public  string Memo { get; set; }
            public virtual bool IsActive { get; set; }
            public virtual decimal? CreditSum { get; set; }
            #endregion


        }

    }
}
