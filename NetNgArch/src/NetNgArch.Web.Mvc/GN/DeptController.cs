using System;
using System.Collections.Generic;
using System.Web.Mvc;
using NetNgArch.Domain.DomainModel.GN;
using NetNgArch.Web.Mvc.Shared;
using ProjectBase.Domain;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc;
using System.Linq;


namespace NetNgArch.Web.Mvc.GN
{
    [Auth]
    public class DeptController : AppBaseController
    {
        public ICommonBD<Dept> DeptBD { get; set; }
        public ICommonBD<Func> FuncBD { get; set; }

        private Corp corp;
        
        public ActionResult List(DeptListVM listvm)
        {
            if (corp == null) corp = GetLoginCorp();
            listvm.ResultList = corp.Depts.View<DeptListVM.ListRow>(listvm.Input.Pager, null, listvm.Input.OrderExpression);
            return ForView("List", listvm);
        }

        [HttpPost]
        [Transaction]
        public ActionResult Delete(DeptListVM listvm)
        {throw new Exception("eee");
            corp = GetLoginCorp();
            foreach (var id in listvm.Input.SelectedValues)
                DeptBD.Delete(id);
            
            return List(listvm);
        }

        public ActionResult Add()
        {
            return ForView("Edit", new DeptEditVM(){CanChangeDeptFunc=IsAdmin()});
        }

        public ActionResult Edit(int id)
        {
            var dept = DeptBD.Get(id);
            var m = new DeptEditVM
                        {
                            Input = Map<Dept, DeptEditVM.EditInput>(dept)
                        };
            foreach (var func in dept.Corp.Funcs)
            {
                m.Input.CorpFuncIds = m.Input.CorpFuncIds + "," + func.Id;
            }
            if (m.Input.CorpFuncIds!="") m.Input.CorpFuncIds = m.Input.CorpFuncIds.Substring(1);
            foreach (var func in dept.Funcs)
            {
                m.Input.DeptFuncIds = m.Input.DeptFuncIds + "," + func.Id;
            }
            if (m.Input.DeptFuncIds!="") m.Input.DeptFuncIds = m.Input.DeptFuncIds.Substring(1);
            m.CanChangeDeptFunc = IsAdmin();
            return ForView(m);
        }


        [HttpPost]
        [Transaction]
        public ActionResult Save(DeptEditVM editvm)
        {
            if (ModelState.IsValid)
            {
                Save(editvm.Input,true);
                SetViewMessage(Message_SaveSuccessfully);
                if (editvm.Input.Id == 0) return RcJson();
            }
            return ClientShowMessage();
        }
        [HttpPost]
        [Transaction]
	    public ActionResult SaveInLine(DeptListVM editvm) {
            int id=Save(editvm.EditInput,false);
            return RcJson(id);
	    }
	
        [HttpPost]
        [Transaction]
	    public ActionResult DeleteInLine(int id) {
		    DeptBD.Delete(id);
		    return RcJson(true);
	    }
        private int Save(DeptEditVM.EditInput input,bool changeDeptFunc)
        {
            Dept dept;
            if (input.Id == 0)
            {
                dept = new Dept();
                dept.Corp = GetLoginCorp();
                dept.Funcs=new DomainList<Func>();
            }
            else
            {
                dept = DeptBD.Get(input.Id);
            }
            if (!AdminCode.Equals(dept.Code,StringComparison.OrdinalIgnoreCase))
            {
                dept.Code = input.Code;
            }
            dept.Name = input.Name;
            if (IsAdmin() && changeDeptFunc)
            {
                dept.Funcs.Clear();
                if (!string.IsNullOrEmpty(input.DeptFuncIds))
                {
                    Array.ForEach(input.DeptFuncIds.Split(','), o => dept.Funcs.Add(FuncBD.Get(int.Parse(o))));
                }
            }
            DeptBD.Save(dept);
            return dept.Id;
        }

    }



}
