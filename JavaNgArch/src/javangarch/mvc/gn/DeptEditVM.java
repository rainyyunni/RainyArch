package javangarch.mvc.gn;
import javax.validation.constraints.*;

import javangarch.domain.domainmodel.gn.Corp;
import projectbase.domain.DORef;
import projectbase.mvc.DisplayName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.*;

@Getter@Setter
public class DeptEditVM {
	public DeptEditVM() {
		input = new EditInput();
	}
	private List<DORef<Corp>> corps;

	private EditInput input;
	private boolean canChangeDeptFunc;
	public boolean getCanChangeDeptFunc(){return canChangeDeptFunc;}
	public void setCanChangeDeptFunc(boolean value){canChangeDeptFunc=value;}

	@Getter@Setter
	@DisplayName("Dept")
	public class EditInput {
		
		private int id;
			
		@NotNull
		@Size(max=10)
		private  String code;
		@NotNull
		@Size(max=20)
		private  String name;

		private String corpFuncIds="";
		private String deptFuncIds="";
    }
}
