using System;
using System.Collections.Generic;
using System.Web.Mvc;
using NetNgArch.Domain.DomainModel.CZ;
using NetNgArch.Domain.DomainModel.GN;
using NetNgArch.Web.Mvc.Shared;
using ProjectBase.Domain;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc;
using System.Linq.Expressions;
using System.Linq;

namespace NetNgArch.Web.Mvc.CZ
{
    [Auth]
    public class MemberNoteController : AppBaseController
    {
        public ICommonBD<MemberNote> MemberNoteBD { get; set; }
		public ICommonBD<Member> MemberBD { get; set; }
		public ICommonBD<User> UserBD { get; set; }

		protected override void OnViewExecuting(Object viewModel)
		{
		    SetViewModel<MemberNoteEditVM>(m =>
		                                       {
		                                           m.Members = MemberBD.GetRefList();
		                                           m.Users = UserBD.GetRefList();
		                                       });
		}
        public ActionResult Search()
        {
            return ForView(new MemberNoteSearchVM(){Members = MemberBD.GetRefList()});
        }

        public ActionResult List(MemberNoteSearchVM searchvm, MemberNoteListVM listvm)
        {
            if (!ModelState.IsValid) return ClientShowMessage();
            var filter = BuildFilter(searchvm.Input);
            if (filter != null)
            {
                listvm.ResultList = MemberNoteBD.GetDtoList<MemberNoteListVM.ListRow>(listvm.Input.Pager,
                                                        filter,
                                                       listvm.Input.OrderExpression);
            }
            return ForView("List", listvm);
        }

        [HttpPost]
        [Transaction]
        public ActionResult Delete(MemberNoteSearchVM searchvm, MemberNoteListVM listvm)
        {
            foreach (var id in listvm.Input.SelectedValues)
                MemberNoteBD.Delete(id);
            return List(searchvm, listvm);
        }

        public ActionResult Add()
        {
        	var m=new MemberNoteEditVM{/*[todo:set default value]*/};
            return ForView("Edit",m);
        }

        public ActionResult Edit(int id)
        {
        	var m=new MemberNoteEditVM
            {
                Input = Map<MemberNote, MemberNoteEditVM.EditInput>(MemberNoteBD.Get(id))
            };
            return ForView(m);
        }
        
        [HttpPost]
        [Transaction]
        public ActionResult Save(MemberNoteEditVM editvm)
        {
            if (ModelState.IsValid)
            {
                Save(editvm.Input);
                SetViewMessage(Message_SaveSuccessfully);
                if (editvm.Input.Id == 0) return RcJson();
            }
            return ClientShowMessage();
        }

        private void Save(MemberNoteEditVM.EditInput input)
        {
            MemberNote membernote;
            if (input.Id == 0)
            {
                membernote = new MemberNote();
            }
            else
            {
                membernote = MemberNoteBD.Get(input.Id);
            }

			membernote.Member = input.Member.ToReferencedDO(MemberBD);
			membernote.Seq = input.Seq;
			membernote.ActionTime = input.ActionTime;
			membernote.Note = input.Note;
			membernote.User = input.User.ToReferencedDO(UserBD);
			membernote.Result = input.Result;
			membernote.NoteClass = input.NoteClass;
			membernote.Memo = input.Memo;
            membernote.IsActive = input.IsActive;
            membernote.CreditSum = input.CreditSum;
            MemberNoteBD.Save(membernote);
        }

        private Expression<Func<MemberNote,bool>> BuildFilter(MemberNoteSearchVM.SearchInput input)
        {
            var filter = PredicateBuilder.True<MemberNote>();
            if (Util.IsNotNull(input.Member))
				filter = filter.And(o => o.Member.Id  == input.Member.Id);
			if (input.Seq!=null)
				filter = filter.And(o => o.Seq==input.Seq);
			/*if (input.ActionTime!=null)
				filter = filter.And(o => o.ActionTime==input.ActionTime);*/
			if (!string.IsNullOrEmpty(input.Note))
				filter = filter.And(o => o.Note.StartsWith(input.Note));
            /*	if (Util.IsNotNull(input.User))
                    filter = filter.And(o => o.User.Id  == input.User.Id);
                if (input.Result!=null)
                    filter = filter.And(o => o.Result==input.Result);
                if (input.NoteClass!=null)
                    filter = filter.And(o => o.NoteClass==input.NoteClass);
                if (!string.IsNullOrEmpty(input.Memo))
                    filter = filter.And(o => o.Memo.StartsWith(input.Memo));

                */
            if (input.IsActive!=null)
                filter = filter.And(o => o.IsActive != null && o.IsActive.Value == input.IsActive.Value);
            return filter;
        }

    }



}
