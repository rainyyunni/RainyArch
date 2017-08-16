package javangarch.mvc.ta;
import javax.validation.constraints.*;

import javangarch.domain.bdinterface.dictenum.Task_StatusEnum;
import javangarch.domain.domainmodel.gn.User;
import projectbase.domain.DORef;
import projectbase.mvc.DisplayName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.*;

@Getter@Setter
public class TaskEditVM {
	public TaskEditVM() {
		input = new EditInput();
	}
	private List<DORef<User>> users;
  
	private EditInput input;

	@Getter@Setter
	@DisplayName("Task")
	public class EditInput {
		
		private int id;
			
		@Size(max=100)
		@NotNull
		private  String name;
		@NotNull
		private  DORef<User> user;
		@NotNull
		private  Date createDate;
		private  Date planBeginDate;
		private  Date planEndDate;
		private  Date beginDate;
		private  Date endDate;
		@NotNull
		private  Task_StatusEnum status;

    }
}
