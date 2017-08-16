
package javangarch.mvc.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javangarch.domain.domainmodel.gn.Corp;
import javangarch.domain.domainmodel.gn.User;
import lombok.Getter;
import lombok.Setter;
    public class LoginInfoViewModel
    {
    	@Getter@Setter
    	private LoginAttemptViewModel input=new LoginAttemptViewModel();
    	public void AddCorpFuncCode(String value) {
			this.corpFuncCodes.add(value);
		}
    	public void AddDeptFuncCode(String value) {
			this.deptFuncCodes.add(value);
		}
    	public void AddUserFuncCode(String value) {
			this.userFuncCodes.add(value);
		}
		@Setter private User loginUser;
		@JsonIgnore
        public User getLoginUser() {
			return loginUser;
		}
		@Setter private Corp loginCorp;
		@JsonIgnore
		public Corp getLoginCorp() {
			return loginCorp;
		}

		
        private List<String> corpFuncCodes=new ArrayList<String>();
        private List<String> deptFuncCodes=new ArrayList<String>();
        private List<String> userFuncCodes=new ArrayList<String>();


        public boolean CanAccess(String funcCode)
        {
    		if (corpFuncCodes.contains(funcCode) || deptFuncCodes.contains(funcCode) || userFuncCodes.contains(funcCode))
                return false;
            return true;
       }
        
		public String getLoginCorpName() {
			return loginCorp.getName();
		}
		public String getLoginUserName() {
			return loginUser.getName();
		}
        //所有无权限的菜单项的funccode列表字符串，逗号分隔，逗号开头结尾
        public String getForbiddenMenuFuncList()
        {
            Collection<String> all =CollectionUtils.union(CollectionUtils.union(corpFuncCodes,deptFuncCodes),userFuncCodes);
            String s = ","+all.stream().filter(o -> o.startsWith("M_")).reduce("",(codes,funccode)->codes + funccode + ",");
            return s;
        }

    }

