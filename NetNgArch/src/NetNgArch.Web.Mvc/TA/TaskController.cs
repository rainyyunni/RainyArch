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
    public class TaskController : AppBaseController
    {
        public ICommonBD<Task> TaskBD { get; set; }
		public ICommonBD<User> UserBD { get; set; }


        protected override void OnViewExecuting(object viewModel)
        {
			SetViewModel<TaskEditVM>(m => {
			                                  m.Users = UserBD.GetRefList(o => o.Id != 1);
			});
        }
        
        public ActionResult Search()
        {
            return ForView(new TaskSearchVM());
        }
        
        public ActionResult List(TaskSearchVM searchvm, TaskListVM listvm)
        {
            if (!ModelState.IsValid) return ClientShowMessage();
            var filter = BuildFilter(searchvm.Input);
            if (filter != null)
            {
                listvm.ResultList = TaskBD.GetDtoList<TaskListVM.ListRow>(listvm.Input.Pager,
                                                        filter,
                                                       listvm.Input.OrderExpression);
            }
            return ForView("List",listvm);
        }

        [HttpPost]
        [Transaction]
        public ActionResult Delete(TaskSearchVM searchvm, TaskListVM listvm)
        {
            foreach (var id in listvm.Input.SelectedValues)
                TaskBD.Delete(id);
            return List(searchvm,listvm);
        }

        public ActionResult Add()
        {
        	var m=new TaskEditVM{/*[todo:set default value]*/};
            return ForView("Edit",m);
        }

        public ActionResult Edit(int id)
        {
        	var m=new TaskEditVM
            {
                Input = Map<Task, TaskEditVM.EditInput>(TaskBD.Get(id))
            };
            return ForView(m);
        }

       
        [HttpPost]
        [Transaction]
        public ActionResult Save(TaskEditVM editvm)
        {
            if (ModelState.IsValid)
            {
                Save(editvm.Input);
                SetViewMessage(Message_SaveSuccessfully);
                if (editvm.Input.Id == 0) return RcJson();
            }
            return ClientShowMessage();
        }

        private void Save(TaskEditVM.EditInput input)
        {
            Task task;
            if (input.Id == 0)
            {
                task = new Task();
            }
            else
            {
                task = TaskBD.Get(input.Id);
            }

			task.Name = input.Name;
			task.User = input.User.ToReferencedDO(UserBD);
			task.CreateDate = input.CreateDate;
			task.PlanBeginDate = input.PlanBeginDate;
			task.PlanEndDate = input.PlanEndDate;
			task.BeginDate = input.BeginDate;
			task.EndDate = input.EndDate;
			task.Status = input.Status;


            TaskBD.Save(task);
        }


        private Expression<Func<Task,bool>> BuildFilter(TaskSearchVM.SearchInput input)
        {
            var filter = PredicateBuilder.True<Task>();
     		/*[todo:choose to use]
			if (!string.IsNullOrEmpty(input.Name))
				filter = filter.And(o => o.Name.StartsWith(input.Name));
			if (Util.IsNotNull(input.User)
				filter = filter.And(o => o.User.Id  == input.User.Id);
			if (input.CreateDate!=null)
				filter = filter.And(o => o.CreateDate==input.CreateDate);
			if (input.PlanBeginDate!=null)
				filter = filter.And(o => o.PlanBeginDate==input.PlanBeginDate);
			if (input.PlanEndDate!=null)
				filter = filter.And(o => o.PlanEndDate==input.PlanEndDate);
			if (input.BeginDate!=null)
				filter = filter.And(o => o.BeginDate==input.BeginDate);
			if (input.EndDate!=null)
				filter = filter.And(o => o.EndDate==input.EndDate);
			if (input.Status!=null)
				filter = filter.And(o => o.Status==input.Status);

			*/
            return filter;
        }

    }



}
