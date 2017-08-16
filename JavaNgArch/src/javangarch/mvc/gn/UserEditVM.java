package javangarch.mvc.gn;
import javax.validation.constraints.*;

import javangarch.domain.domainmodel.gn.Corp;
import javangarch.domain.domainmodel.gn.Dept;
import projectbase.domain.DORef;
import projectbase.mvc.DisplayName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.*;

@Getter@Setter
public class UserEditVM {
	public UserEditVM() {
		input = new EditInput();
	}
	private List<DORef<Dept>> depts;

	private EditInput input;

	private boolean canChangeUserFunc;
	public boolean getCanChangeUserFunc(){return canChangeUserFunc;}
	public void setCanChangeUserFunc(boolean value){canChangeUserFunc=value;}
	
	@Getter@Setter
	@DisplayName("User")
	public class EditInput {
		
		private int id;
			
		@NotNull
		private  DORef<Dept> dept;
		@NotNull
		@Size(max=20)
		private  String code;
		@NotNull
		@Size(max=10)
		private  String name;
		@Size(max=20)
		private  String cellPhone;
		private  boolean isActive;
		public  boolean getIsActive() { return isActive; }
		public  void setIsActive(boolean value) { isActive=value; }
		
		private String corpFuncIds="";
		private String deptFuncIds="";
		private String userFuncIds="";

    }
}
