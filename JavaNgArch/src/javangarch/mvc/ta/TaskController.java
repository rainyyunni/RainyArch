 package javangarch.mvc.ta;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import projectbase.mvc.Auth;
import projectbase.domain.ICommonBD;
import projectbase.mvc.Transaction;
import projectbase.mvc.result.ActionResult;
import projectbase.utils.HqlCriterion;
import javangarch.domain.domainmodel.gn.User;
import javangarch.domain.domainmodel.ta.Task;
import javangarch.mvc.shared.AppBaseController;

@Auth
public class TaskController extends AppBaseController{

        @Resource public ICommonBD<Task> TaskBD;
		@Resource public ICommonBD<User> UserBD;


	@Override
	protected void OnViewExecuting(Object viewModel) {
			if(viewModel instanceof TaskEditVM) {
				((TaskEditVM)viewModel).setUsers(UserBD.GetRefList());
			}
	}
		
       public ActionResult Search()
        {
            return ForView(new TaskSearchVM());
        }
        
        public ActionResult List(@Valid TaskSearchVM searchvm, @Valid TaskListVM listvm)
        {
        	HqlCriterion filter=BuildFilter(searchvm.getInput());
            if (filter!= null)
            {
            	listvm.setResultList( TaskBD.GetDtoList(listvm.getInput().getPager(),
                		TaskListVM_ListRow.class,
                		filter,
                		listvm.getInput().getOrderExpression()));
            }
            return ForView("List",listvm);
        }

    	@RequestMapping(method=RequestMethod.POST)
    	@Transaction
        public ActionResult Delete(@Valid TaskSearchVM searchvm, @Valid TaskListVM listvm)
        {
            for (Integer id : listvm.getInput().getSelectedValues())
                TaskBD.Delete(id);
            return List(searchvm,listvm);
        }

        public ActionResult Add()
        {
            return ForView("Edit",new TaskEditVM());
        }

        public ActionResult Edit(int id)
        {
        	TaskEditVM m=new TaskEditVM();
        	Map(TaskBD.Get(id),m.getInput());
            return ForView(m);
        }


    	@RequestMapping(method=RequestMethod.POST)
    	@Transaction
        public ActionResult Save(@Valid TaskEditVM editvm)
        {
            Save(editvm.getInput());
            if (editvm.getInput().getId() == 0) return RcJson();
            return ClientShowMessage(Message_SaveSuccessfully);
        }

        private void Save(TaskEditVM.EditInput input)
        {
            Task task;
            if (input.getId() == 0)
            {
                task = new Task();
            }
            else
            {
                task = TaskBD.Get(input.getId());
            }

			task.setName(input.getName());
			task.setUser(input.getUser().ToReferencedDO(UserBD));
			task.setCreateDate(input.getCreateDate());
			task.setPlanBeginDate(input.getPlanBeginDate());
			task.setPlanEndDate(input.getPlanEndDate());
			task.setBeginDate(input.getBeginDate());
			task.setEndDate(input.getEndDate());
			task.setStatus(input.getStatus().getIntValue());

            TaskBD.Save(task);
        }

        private HqlCriterion BuildFilter(TaskSearchVM.SearchInput input)
        {
            HqlCriterion filter = new HqlCriterion();

			if (!StringUtils.isEmpty(input.getName()))
				filter = filter.And("this.name like ? ",input.getName()+"%");
            /*		if (Util.IsNotNull(input.getUser())
				filter = filter.And("this.user.id  = ?",input.getUser().getId());
			if (input.getCreateDate()!=null)
				filter = filter.And("this.createDate=?",input.getCreateDate());
			if (input.getPlanBeginDate()!=null)
				filter = filter.And("this.planBeginDate=?",input.getPlanBeginDate());
			if (input.getPlanEndDate()!=null)
				filter = filter.And("this.planEndDate=?",input.getPlanEndDate());
			if (input.getBeginDate()!=null)
				filter = filter.And("this.beginDate=?",input.getBeginDate());
			if (input.getEndDate()!=null)
				filter = filter.And("this.endDate=?",input.getEndDate());
			if (input.getStatus()!=null)
				filter = filter.And("this.status=?",input.getStatus());

*/
            return filter;
        }

    }

