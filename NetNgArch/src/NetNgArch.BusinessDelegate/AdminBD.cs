using System;
using System.Collections.Generic;
using System.Linq;
using NetNgArch.Domain.BDInterface;
using NetNgArch.Domain.DomainModel.GN;
using ProjectBase.BusinessDelegate;
using ProjectBase.Domain;
using ProjectBase.Utils;

namespace NetNgArch.BusinessDelegate
{
    public class AdminBD : BaseBusinessDelegate,IAdminBD
    {
        public IGenericDao<User> UserDao { get; set; }
        public IGenericDao<Corp> CorpDao { get; set; }
        public IUtilQuery UtilQuery { get; set; }

        public User GetLoginUser(string userCode,string password)
        {
            return UserDao.GetOneByQuery(o => o.Code == userCode && o.Password == password);
        }
        public void RefreshUser(User user)
        {
            UserDao.Refresh(user);
        }
        public void RefreshCorp(Corp corp)
        {
            CorpDao.Refresh(corp);
        }
    }
}
