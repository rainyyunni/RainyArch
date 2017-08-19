using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;
using NetNgArch.Domain.DomainModel.GN;


namespace NetNgArch.Web.Mvc.Home
{
    public class LoginInfoViewModel
    {
        public LoginAttemptViewModel Input = new LoginAttemptViewModel();

        public void AddCorpFuncCode(String value)
        {
            this.corpFuncCodes.Add(value);
        }
        public void AddDeptFuncCode(String value)
        {
            this.deptFuncCodes.Add(value);
        }
        public void AddUserFuncCode(String value)
        {
            this.userFuncCodes.Add(value);
        }

        [ScriptIgnore]
        public User LoginUser { get; set; }
        [ScriptIgnore]
        public Corp LoginCorp { get; set; }

        private IList<string> corpFuncCodes=new List<string>();
        private IList<string> deptFuncCodes = new List<string>();
        private IList<string> userFuncCodes = new List<string>();

        public String LoginCorpName
        {
            get
            {
                return LoginCorp.Name;
            }
        }

        public String LoginUserName
        {
            get
            {
                return LoginUser.Name;
            }
        }
        public bool CanAccess(string funcCode)
        {
            if (corpFuncCodes.Contains(funcCode) || deptFuncCodes.Contains(funcCode) || userFuncCodes.Contains(funcCode))
                return false;
            return true;
        }
        //所有无权限的菜单项的funccode列表字符串，逗号分隔，逗号开头结尾
        public string ForbiddenMenuFuncList
        {
            get
            {
                var s = ",";
                var all = corpFuncCodes.Union(deptFuncCodes).Union(userFuncCodes).Where(o => o.StartsWith("M_"));
                foreach (var funccode in all)
                {
                    s = s + funccode + ",";
                }
                return s;
            }
        }
        //所有无权限的funccode列表字符串，逗号分隔，逗号开头结尾
        public string ForbiddenFuncList
        {
            get
            {
                var s = ",";
                var all = corpFuncCodes.Union(deptFuncCodes).Union(userFuncCodes);
                foreach (var funccode in all)
                {
                    s = s + funccode + ",";
                }
                return s;
            }
        }
    }
}
