using System;
using System.Collections.Generic;
using System.Web.Mvc;
using NetNgArch.Domain.DomainModel.GN;
using NetNgArch.Domain.DomainModel.TA;
using NetNgArch.Web.Mvc.Shared;
using ProjectBase.Domain;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc;
using System.Linq.Expressions;
using System.Linq;

namespace NetNgArch.Web.Mvc.TA
{
    [Auth]
    public class TaskItemController : AppBaseController
    {
        public ICommonBD<TaskItem> TaskItemBD { get; set; }
		public ICommonBD<Task> TaskBD { get; set; }
		public ICommonBD<User> UserBD { get; set; }


        protected override void OnViewExecuting(object viewModel)
        {
			SetViewModel<TaskItemEditVM>(m =>{ 
				m.Tasks = TaskBD.GetRefList();
				m.Users = UserBD.GetRefList();
			});
        }
        
        public ActionResult Search()
        {
            return ForView(new TaskItemSearchVM());
        }
        
        public ActionResult List(TaskItemSearchVM searchvm, TaskItemListVM listvm)
        {
            if (!ModelState.IsValid) return ClientShowMessage();
            var filter = BuildFilter(searchvm.Input);
            if (filter != null)
            {
                listvm.ResultList = TaskItemBD.GetDtoList<TaskItemListVM.ListRow>(listvm.Input.Pager,
                                                        filter,
                                                       listvm.Input.OrderExpression);
            }
            return ForView("List",listvm);
        }

        [HttpPost]
        [Transaction]
        public ActionResult Delete(TaskItemSearchVM searchvm, TaskItemListVM listvm)
        {
            foreach (var id in listvm.Input.SelectedValues)
                TaskItemBD.Delete(id);
            return List(searchvm,listvm);
        }

        public ActionResult Add()
        {
        	var m=new TaskItemEditVM{/*[todo:set default value]*/};
            return ForView("Edit",m);
        }

        public ActionResult Edit(int id)
        {
        	var m=new TaskItemEditVM
            {
                Input = Map<TaskItem, TaskItemEditVM.EditInput>(TaskItemBD.Get(id))
            };
            return ForView(m);
        }

       
        [HttpPost]
        [Transaction]
        public ActionResult Save(TaskItemEditVM editvm)
        {
            if (ModelState.IsValid)
            {
                Save(editvm.Input);
                SetViewMessage(Message_SaveSuccessfully);
                if (editvm.Input.Id == 0) return RcJson();
            }
            return ClientShowMessage();
        }

        private void Save(TaskItemEditVM.EditInput input)
        {
            TaskItem taskitem;
            if (input.Id == 0)
            {
                taskitem = new TaskItem();
            }
            else
            {
                taskitem = TaskItemBD.Get(input.Id);
            }

			taskitem.Task = input.Task.ToReferencedDO(TaskBD);
			taskitem.Brief = input.Brief;
			taskitem.User = input.User.ToReferencedDO(UserBD);
			taskitem.Requirement = input.Requirement;
			taskitem.Record = input.Record;
			taskitem.KeyInfo = input.KeyInfo;
			taskitem.CreateDate = input.CreateDate;
			taskitem.ActionDate = input.ActionDate;
			taskitem.Status = input.Status;
			taskitem.OrderNum = input.OrderNum;


            TaskItemBD.Save(taskitem);
        }


        private Expression<Func<TaskItem,bool>> BuildFilter(TaskItemSearchVM.SearchInput input)
        {
            var filter = PredicateBuilder.True<TaskItem>();
     		/*[todo:choose to use]
			if (Util.IsNotNull(input.Task)
				filter = filter.And(o => o.Task.Id  == input.Task.Id);
			if (!string.IsNullOrEmpty(input.Brief))
				filter = filter.And(o => o.Brief.StartsWith(input.Brief));
			if (Util.IsNotNull(input.User)
				filter = filter.And(o => o.User.Id  == input.User.Id);
			if (!string.IsNullOrEmpty(input.Requirement))
				filter = filter.And(o => o.Requirement.StartsWith(input.Requirement));
			if (!string.IsNullOrEmpty(input.Record))
				filter = filter.And(o => o.Record.StartsWith(input.Record));
			if (!string.IsNullOrEmpty(input.KeyInfo))
				filter = filter.And(o => o.KeyInfo.StartsWith(input.KeyInfo));
			if (input.CreateDate!=null)
				filter = filter.And(o => o.CreateDate==input.CreateDate);
			if (input.ActionDate!=null)
				filter = filter.And(o => o.ActionDate==input.ActionDate);
			if (input.Status!=null)
				filter = filter.And(o => o.Status==input.Status);
			if (input.OrderNum!=null)
				filter = filter.And(o => o.OrderNum==input.OrderNum);

			*/
            return filter;
        }

    }



}
