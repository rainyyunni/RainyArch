using System;
using System.Collections.Generic;
using System.Web.Mvc;
using NetNgArch.Domain.DomainModel.GN;
using NetNgArch.Web.Mvc.Shared;
using ProjectBase.Domain;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc;
using System.Linq.Expressions;


namespace NetNgArch.Web.Mvc.GN
{
    [Auth]
    public class UserController : AppBaseController
    {
        public ICommonBD<User> UserBD { get; set; }
        public ICommonBD<Dept> DeptBD { get; set; }
        public ICommonBD<Func> FuncBD { get; set; }

        public ActionResult Search()
        {
            return ForView(new UserSearchVM()
                               {
                                   Depts = DeptBD.GetRefList(o => o.Corp.Id == GetLoginCorpId())
                               });
        }

        public ActionResult List(UserSearchVM searchvm,UserListVM listvm)
        {
            Expression<Func<User,bool>> filter = o => o.Corp.Id == GetLoginCorpId();
            if (Util.IsNotNull(searchvm.Input.Dept))
            {
                listvm.DeptId = searchvm.Input.Dept.Id;
                filter = o => o.Dept.Id == searchvm.Input.Dept.Id;
            }
            listvm.ResultList = UserBD.GetDtoList<UserListVM.ListRow>(listvm.Input.Pager, filter, listvm.Input.OrderExpression);
            return ForView("List",listvm);
        }

        [HttpPost]
        [Transaction]
        public ActionResult Delete(UserSearchVM searchvm, UserListVM listvm)
        {
            foreach (var id in listvm.Input.SelectedValues)
                UserBD.Delete(id);
            return List(searchvm, listvm);
        }
        public ActionResult Add(int? deptId)
        {
            var m = new UserEditVM {Input = new UserEditVM.EditInput {IsActive = true}};
            if (deptId != null) m.Input.Dept = new DORef<Dept>(deptId.Value);
            m.CanChangeUserFunc=CanAccess("_ChangeUserFunc");
            return ForView("Edit",m);
        }

        public ActionResult Edit(int id)
        {
            var user = UserBD.Get(id);
            var m = new UserEditVM
            {
                Input = Map<User, UserEditVM.EditInput>(user)
            };
            foreach (var func in user.Dept.Corp.Funcs)
            {
                m.Input.CorpFuncIds = m.Input.CorpFuncIds + "," + func.Id;
            }
            if (m.Input.CorpFuncIds != "") m.Input.CorpFuncIds = m.Input.CorpFuncIds.Substring(1);
            foreach (var func in user.Dept.Funcs)
            {
                m.Input.DeptFuncIds = m.Input.DeptFuncIds + "," + func.Id;
            }
            if (m.Input.DeptFuncIds != "") m.Input.DeptFuncIds = m.Input.DeptFuncIds.Substring(1);
            foreach (var func in user.Funcs)
            {
                m.Input.UserFuncIds = m.Input.UserFuncIds + "," + func.Id;
            }
            if (m.Input.UserFuncIds != "") m.Input.UserFuncIds = m.Input.UserFuncIds.Substring(1);
            m.CanChangeUserFunc=(!user.Equals(GetLoginUser()) && CanAccess("_ChangeUserFunc"));
            return ForView(m);
        }

        [HttpPost]
        [Transaction]
        public ActionResult Save(UserEditVM editvm)
        {
            if (ModelState.IsValid)
            {
                Save(editvm.Input);
                SetViewMessage(Message_SaveSuccessfully);
                if (editvm.Input.Id == 0) return RcJson();
            }
            return ClientShowMessage();
        }
        [HttpPost]
        [Transaction]
        public ActionResult SavePassword(string oldpassword, string newpassword)
        {
            if (!ModelState.IsValid)
                SetViewMessage(Message_UserInputError);
            else
            {
                var user = GetLoginUser();
                if (user.Password != oldpassword)
                {
                    SetViewMessage(Res.M("OldPassword_Wrong"));
                }
                else
                {
                    user.Password = newpassword;
                    SetViewMessage(Message_SaveSuccessfully);
                }
            }
            return ClientShowMessage();
        }
        [HttpPost]
        [Transaction]
        public ActionResult ResetPassword(int userId)
        {
            if (!IsAdmin()) return AuthFailure();
            var user = UserBD.Get(userId);
            user.Password = DefaultPassword;

            return ClientShowMessage(Res.M("ResetPassword_Succeed"));
        }
      
        protected override void OnViewExecuting(object viewModel)
        {
            var l = DeptBD.GetRefList(o => o.Corp.Id == GetLoginCorpId());
            SetViewModel<UserEditVM>(m => m.Depts =l );
            SetViewModel<UserSearchVM>(m => m.Depts =l );
        }
        private void Save(UserEditVM.EditInput input)
        {
            User user;
            if (input.Id == 0)
            {
                user = new User();
                user.Corp = GetLoginCorp();
                user.Password = DefaultPassword;
                user.Funcs = new DomainList<Func>();
            }
            else
            {
                user = UserBD.Get(input.Id);
            }
            if (IsAdmin(user)) throw new AuthFailureException();
			user.Dept = input.Dept.ToReferencedDO(DeptBD);
            if (!AdminCode.Equals(user.Code, StringComparison.OrdinalIgnoreCase))
            {
                user.Code = input.Code;
            }
            user.Name = input.Name;
            user.IsActive = input.IsActive;
            if (user!=GetLoginUser())
            {
                if (CanAccess("_ChangeUserFunc"))
                {
                    user.Funcs.Clear();
                    if (!string.IsNullOrEmpty(input.UserFuncIds))
                    {
                        Array.ForEach(input.UserFuncIds.Split(','), o => user.Funcs.Add(FuncBD.Get(int.Parse(o))));
                    }
                }
            }
            UserBD.Save(user);
        }

    }



}
