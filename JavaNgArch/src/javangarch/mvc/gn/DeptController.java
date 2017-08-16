package javangarch.mvc.gn;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import projectbase.domain.ICommonBD;
import projectbase.domain.customcollection.DomainList;
import projectbase.domain.customcollection.IDomainList;
import projectbase.mvc.Auth;
import projectbase.mvc.AuthFailureException;
import projectbase.mvc.Transaction;
import projectbase.mvc.result.ActionResult;
import projectbase.utils.HqlCriterion;
import javangarch.domain.domainmodel.gn.Corp;
import javangarch.domain.domainmodel.gn.Dept;
import javangarch.domain.domainmodel.gn.Func;
import javangarch.domain.domainmodel.gn.User;
import javangarch.mvc.gn.DeptEditVM.EditInput;
import javangarch.mvc.shared.AppBaseController;

@Auth
public class DeptController extends AppBaseController {
	@Resource
	public ICommonBD<Dept> DeptBD;
	@Resource
	public ICommonBD<Func> FuncBD;
	@Resource public ICommonBD<User> UserBD;

	private Corp corp;

	@InitBinder
	public void init() {
		corp = null;
	}

	public ActionResult Search() {
		return ForView();
	}

	public ActionResult List(DeptListVM listvm) {
		if (corp == null)
			corp = GetLoginCorp();
		listvm.setResultList(
				((IDomainList<Dept>) corp.getDepts()).View(listvm.getInput().getPager(),Dept.class,DeptListVM_ListRow.class,null,listvm.getInput().getOrderExpression())
				);
		
		return ForView("List", listvm);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Transaction
	public ActionResult Delete(DeptListVM list) {
		corp = GetLoginCorp();
		for (int id : list.getInput().getSelectedValues())
			DeptBD.Delete(id);

		return List(list);
	}

	public ActionResult Add() {
		DeptEditVM m = new DeptEditVM();
		m.setCanChangeDeptFunc(IsAdmin());
		return ForView("Edit", m);
	}

	public ActionResult Edit(int id) {
		Dept dept = DeptBD.Get(id);
		DeptEditVM m = new DeptEditVM();

		Map(dept, m.getInput());
		EditInput input = m.getInput();
		for (Func func : dept.getCorp().getFuncs()) {
			input.setCorpFuncIds(input.getCorpFuncIds() + "," + func.getId());
		}
		if (!input.getCorpFuncIds().isEmpty())
			input.setCorpFuncIds(input.getCorpFuncIds().substring(1));
		for (Func func : dept.getFuncs()) {
			input.setDeptFuncIds(input.getDeptFuncIds() + "," + func.getId());
		}
		if (!input.getDeptFuncIds().isEmpty())
			input.setDeptFuncIds(input.getDeptFuncIds().substring(1));
		m.setCanChangeDeptFunc(IsAdmin());
		return ForView(m);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Transaction
	public ActionResult Save(@Valid DeptEditVM editvm) {
        Save(editvm.getInput(),true);
        if (editvm.getInput().getId() == 0) return RcJson();
        return ClientShowMessage(Message_SaveSuccessfully);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Transaction
	public ActionResult SaveInLine(@Valid DeptListVM editvm) {
        int id=Save(editvm.getEditInput(),false);
        return RcJson(id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Transaction
	public ActionResult DeleteInLine(int id) {
		DeptBD.Delete(id);
		return RcJson(true);
	}
	private int Save(DeptEditVM.EditInput input,boolean changeDeptFunc) {
		Dept dept;
		if (input.getId() == 0) {
			dept = new Dept();
			dept.setCorp(GetLoginCorp());
			dept.setFuncs(new DomainList<Func>());
		} else {
			dept = DeptBD.Get(input.getId());
		}

		if (!AdminCode.equalsIgnoreCase(dept.getCode())){
			dept.setCode(input.getCode());
		}
		dept.setName(input.getName());
		if (IsAdmin() && changeDeptFunc) {
			dept.getFuncs().clear();
			if (!StringUtils.isEmpty(input.getDeptFuncIds())) {
				for (String s : input.getDeptFuncIds().split(","))
					dept.getFuncs().add(FuncBD.Get(Integer.valueOf(s)));
			}
		}
		DeptBD.Save(dept);
		return dept.getId();
	}


}
