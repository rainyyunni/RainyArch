using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using ProjectBase.Web.Mvc;

namespace NetNgArch.Web.Mvc.Home
{
    [DisplayName("User")]
    public class LoginAttemptViewModel:BaseViewModel
    {
        [DisplayName("公司代码")]
        [Required]
        public string CorpCode { get; set; }
        [Required]
        public string Code { get; set; }
        [Required]
        public string Password { get; set; }

        public LoginAttemptViewModel()
        {
            CorpCode = "";
            Code = "";
            Password = "";
        }
    }
}
