package javangarch.mvc.gn;
import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import projectbase.domain.DORef;
import projectbase.domain.ICommonBD;
import projectbase.domain.customcollection.DomainList;

import projectbase.mvc.Auth;
import projectbase.mvc.AuthFailureException;
import projectbase.mvc.Transaction;
import projectbase.mvc.result.ActionResult;
import projectbase.utils.HqlCriterion;
import projectbase.utils.Util;
import javangarch.domain.domainmodel.gn.*;
import javangarch.mvc.shared.AppBaseController;


    @Auth
    public class UserController extends AppBaseController
    {
        @Resource public ICommonBD<User> UserBD;
        @Resource public ICommonBD<Dept> DeptBD;
        @Resource public ICommonBD<Func> FuncBD;

        public ActionResult Search()
        {
        	UserSearchVM m=new UserSearchVM();
        	HqlCriterion filter = new HqlCriterion().And("this.corp.id = ?",GetLoginCorpId());
        	m.setDepts(DeptBD.GetRefList(filter, "Code"));
            return ForView(m);
        }
        
        public ActionResult List(@Valid UserSearchVM searchvm,UserListVM listvm)
        {
        	HqlCriterion filter = new HqlCriterion().And("this.corp.id = ?",GetLoginCorpId());
            if (Util.IsNotNull(searchvm.getInput().getDept()))
            {
                listvm.setDeptId(searchvm.getInput().getDept().getId());
                filter = new HqlCriterion().And("this.dept.id = ?",listvm.getDeptId());
            }
            listvm.setResultList(UserBD.GetDtoList(listvm.getInput().getPager(), 
            		UserListVM_ListRow.class,filter,listvm.getInput().getOrderExpression()));
            return ForView("List",listvm);
        }

        @RequestMapping(method=RequestMethod.POST)
        @Transaction
        public ActionResult Delete(UserSearchVM search,UserListVM list)
        {
            for (Integer id : list.getInput().getSelectedValues())
                UserBD.Delete(id);
            return List(search,list);
        }
        public ActionResult Add(Integer deptId)
        {
        	UserEditVM m = new UserEditVM();
        	m.getInput().setIsActive(true);
            if (deptId != null) m.getInput().setDept(new DORef<Dept>(deptId.intValue()));
            m.setCanChangeUserFunc(CanAccess("_ChangeUserFunc"));
            return ForView("Edit",m);
        }

        public ActionResult Edit(int id)
        {
            User user = UserBD.Get(id);
            UserEditVM m = new UserEditVM();
            Map(user,m.getInput());

            for (Func func : user.getDept().getCorp().getFuncs())
            {
                m.getInput().setCorpFuncIds(m.getInput().getCorpFuncIds() + "," + func.getId());
            }
            if (!m.getInput().getCorpFuncIds().isEmpty()) m.getInput().setCorpFuncIds(m.getInput().getCorpFuncIds().substring(1));
            for (Func func : user.getDept().getFuncs())
            {
                m.getInput().setDeptFuncIds(m.getInput().getDeptFuncIds() + "," + func.getId());
            }
            if (!m.getInput().getDeptFuncIds().isEmpty()) m.getInput().setDeptFuncIds(m.getInput().getDeptFuncIds().substring(1));
            for (Func func : user.getFuncs())
            {
                m.getInput().setUserFuncIds(m.getInput().getUserFuncIds() + "," + func.getId());
            }
            if (!m.getInput().getUserFuncIds().isEmpty()) m.getInput().setUserFuncIds(m.getInput().getUserFuncIds().substring(1));

            m.setCanChangeUserFunc (!user.equals(GetLoginUser()) && CanAccess("_ChangeUserFunc"));
            return ForView(m);
        }

        @RequestMapping(method=RequestMethod.POST)
        @Transaction
        public ActionResult Save(@Valid UserEditVM editvm) 
        {
            Save(editvm.getInput());
            if (editvm.getInput().getId() == 0) return RcJson();
            return ClientShowMessage(Message_SaveSuccessfully);
        }
        @RequestMapping(method=RequestMethod.POST)
        @Transaction
        public ActionResult SavePassword(String OldPassword, String NewPassword)
        {
                User user = GetLoginUser();
                if (!user.getPassword().equals(OldPassword))
                {
                    SetViewMessage("OldPassword_Wrong");
                }
                else
                {
                    //user.setPassword ( NewPassword);
                    SetViewMessage(Message_SaveSuccessfully);
                }

            return ClientShowMessage();
        }
        @RequestMapping(method=RequestMethod.POST)
        @Transaction
        public ActionResult ResetPassword(int userId)
        {
            if (!IsAdmin()) return AuthFailure();
            User user = UserBD.Get(userId);
            user.setPassword( DefaultPassword);

            return ClientShowMessage("ResetPassword_Succeed");
        }
  
        @Override
        protected  void OnViewExecuting(Object viewModel)
        {
        	if(viewModel instanceof UserEditVM)
        		((UserEditVM)viewModel).setDepts(DeptBD.GetRefList(new HqlCriterion().And("this.corp.id =?", GetLoginCorpId())));
        }
        
         private User Save(UserEditVM.EditInput input) 
        {
            User user;
            if (input.getId() == 0)
            {
                user = new User();
                user.setCorp(GetLoginCorp());
                user.setPassword(DefaultPassword);
                user.setFuncs( new DomainList<Func>());
            }
            else
            {
                user = UserBD.Get(input.getId());
            }
            if (IsAdmin(user)) throw new AuthFailureException();
			user.setDept ( input.getDept().ToReferencedDO(DeptBD));
            if (!AdminCode.equalsIgnoreCase(user.getCode()))
            {
                user.setCode(input.getCode());
            }
            user.setName(input.getName());
			user.setCellPhone(input.getCellPhone());
            user.setIsActive(input.getIsActive());
            if (!user.equals(GetLoginUser()))
            {
                if (CanAccess("_ChangeUserFunc"))
                {
                    user.getFuncs().clear();
                    if (!StringUtils.isEmpty(input.getUserFuncIds()))
                    {
                    	for(String id:input.getUserFuncIds().split(","))
                        	user.getFuncs().add(FuncBD.Get(Integer.valueOf(id)));
                    }
                }
            }
            UserBD.Save(user);
            return user;
        }

    }


