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
import javangarch.domain.domainmodel.ta.TaskItem;
import javangarch.mvc.shared.AppBaseController;

@Auth
public class TaskItemController extends AppBaseController{

        @Resource public ICommonBD<TaskItem> TaskItemBD;
		@Resource public ICommonBD<Task> TaskBD;
		@Resource public ICommonBD<User> UserBD;


	@Override
	protected void OnViewExecuting(Object viewModel) {
			if(viewModel instanceof TaskItemEditVM) {
				((TaskItemEditVM)viewModel).setTasks(TaskBD.GetRefList());
				((TaskItemEditVM)viewModel).setUsers(UserBD.GetRefList());
			}

	}
		
       public ActionResult Search()
        {
            return ForView(new TaskItemSearchVM());
        }
        
        public ActionResult List(@Valid TaskItemSearchVM searchvm, @Valid TaskItemListVM listvm)
        {
        	HqlCriterion filter=BuildFilter(searchvm.getInput());
            if (filter!= null)
            {
            	listvm.setResultList( TaskItemBD.GetDtoList(listvm.getInput().getPager(),
                		TaskItemListVM_ListRow.class,
                		filter,
                		listvm.getInput().getOrderExpression()));
            }
            return ForView("List",listvm);
        }

    	@RequestMapping(method=RequestMethod.POST)
    	@Transaction
        public ActionResult Delete(@Valid TaskItemSearchVM searchvm, @Valid TaskItemListVM listvm)
        {
            for (Integer id : listvm.getInput().getSelectedValues())
                TaskItemBD.Delete(id);
            return List(searchvm,listvm);
        }

        public ActionResult Add()
        {
            return ForView("Edit",new TaskItemEditVM());
        }

        public ActionResult Edit(int id)
        {
        	TaskItemEditVM m=new TaskItemEditVM();
        	Map(TaskItemBD.Get(id),m.getInput());
            return ForView(m);
        }


    	@RequestMapping(method=RequestMethod.POST)
    	@Transaction
        public ActionResult Save(@Valid TaskItemEditVM editvm)
        {
            Save(editvm.getInput());
            if (editvm.getInput().getId() == 0) return RcJson();
            return ClientShowMessage(Message_SaveSuccessfully);
        }

        private void Save(TaskItemEditVM.EditInput input)
        {
            TaskItem taskitem;
            if (input.getId() == 0)
            {
                taskitem = new TaskItem();
            }
            else
            {
                taskitem = TaskItemBD.Get(input.getId());
            }

			taskitem.setTask(input.getTask().ToReferencedDO(TaskBD));
			taskitem.setBrief(input.getBrief());
			taskitem.setUser(input.getUser().ToReferencedDO(UserBD));
			taskitem.setRequirement(input.getRequirement());
			taskitem.setRecord(input.getRecord());
			taskitem.setKeyInfo(input.getKeyInfo());
			taskitem.setCreateDate(input.getCreateDate());
			taskitem.setActionDate(input.getActionDate());
			taskitem.setStatus(input.getStatus().getIntValue());
			taskitem.setOrderNum(input.getOrderNum());

            TaskItemBD.Save(taskitem);
        }

        private HqlCriterion BuildFilter(TaskItemSearchVM.SearchInput input)
        {
            HqlCriterion filter = new HqlCriterion();
            /*
			if (Util.IsNotNull(input.getTask())
				filter = filter.And("this.task.id  = ?",input.getTask().getId());
			if (!StringUtils.isEmpty(input.getBrief()))
				filter = filter.And("this.brief like ? ",input.getBrief()+"%");
			if (Util.IsNotNull(input.getUser())
				filter = filter.And("this.user.id  = ?",input.getUser().getId());
			if (!StringUtils.isEmpty(input.getRequirement()))
				filter = filter.And("this.requirement like ? ",input.getRequirement()+"%");
			if (!StringUtils.isEmpty(input.getRecord()))
				filter = filter.And("this.record like ? ",input.getRecord()+"%");
			if (!StringUtils.isEmpty(input.getKeyInfo()))
				filter = filter.And("this.keyInfo like ? ",input.getKeyInfo()+"%");
			if (input.getCreateDate()!=null)
				filter = filter.And("this.createDate=?",input.getCreateDate());
			if (input.getActionDate()!=null)
				filter = filter.And("this.actionDate=?",input.getActionDate());
			if (input.getStatus()!=null)
				filter = filter.And("this.status=?",input.getStatus());
			if (input.getOrderNum()!=null)
				filter = filter.And("this.orderNum=?",input.getOrderNum());

*/
            return filter;
        }

    }

