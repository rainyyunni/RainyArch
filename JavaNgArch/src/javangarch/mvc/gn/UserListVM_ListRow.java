package javangarch.mvc.gn;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class UserListVM_ListRow
{
    private int id;
	private String corpName ;
	private String deptName ;
	private String code;
	private String password;
	private String name;
	private String cellPhone;
	private String loginMark;
	private boolean isActive;
	public boolean getIsActive() { return isActive;  }
	public void setIsActive(boolean value) { isActive=value;  }

	
}


