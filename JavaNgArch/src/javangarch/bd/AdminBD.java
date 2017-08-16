

package javangarch.bd;



import javax.annotation.Resource;



import projectbase.bd.BaseBusinessDelegate;

import projectbase.domain.IGenericDao;
import projectbase.domain.IUtilQuery;
import projectbase.utils.HqlCriterion;
import javangarch.domain.bdinterface.IAdminBD;
import javangarch.domain.domainmodel.gn.Corp;
import javangarch.domain.domainmodel.gn.User;

    public class AdminBD  extends  BaseBusinessDelegate implements IAdminBD
    {
    	
    	@Resource public IGenericDao<User> UserDao;
    	@Resource public IGenericDao<Corp> CorpDao;
    	@Resource  public IUtilQuery UtilQuery;

         public User GetLoginUser(String corpCode,String userCode,String password)
         {
        	 HqlCriterion filter=new HqlCriterion()
        	 .And("this.corp.code=?" , corpCode)
        	 .And("this.code=?" , userCode)
        	 .And("this.password=?",password);
             return UserDao.GetOneByQuery(filter);
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


