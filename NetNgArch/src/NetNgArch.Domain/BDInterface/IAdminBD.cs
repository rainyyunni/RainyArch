
using System.Collections.Generic;
using NetNgArch.Domain.DomainModel.GN;
using ProjectBase.Domain;

namespace NetNgArch.Domain.BDInterface
{
    public interface IAdminBD :IBusinessDelegate 
    {
        User GetLoginUser(string userCode,string password);
        void RefreshUser(User user);
        void RefreshCorp(Corp corp);
    }
}
