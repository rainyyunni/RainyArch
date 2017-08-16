


package javangarch.domain.bdinterface;

import javangarch.domain.domainmodel.gn.Corp;
import javangarch.domain.domainmodel.gn.User;
import projectbase.domain.IBusinessDelegate;

    public interface IAdminBD extends IBusinessDelegate 
    {
        User GetLoginUser(String corpCode, String userCode, String password);
        void RefreshUser(User user);
        void RefreshCorp(Corp corp);
    }

