package javangarch.mvc.ta;
import javax.validation.constraints.*;

import javangarch.domain.bdinterface.dictenum.Task_StatusEnum;
import javangarch.domain.domainmodel.gn.User;
import javangarch.domain.domainmodel.ta.Task;
import projectbase.domain.DORef;
import projectbase.mvc.DisplayName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.*;

@Getter@Setter
public class TaskItemEditVM {
	public TaskItemEditVM() {
		input = new EditInput();
	}
	private List<DORef<Task>> tasks;
	private List<DORef<User>> users;
  
	private EditInput input;

	@Getter@Setter
	@DisplayName("TaskItem")
	public class EditInput {
		
		private int id;
			
		@NotNull
		private  DORef<Task> task;
		@Size(max=30)
		@NotNull
		private  String brief;
		@NotNull
		private  DORef<User> user;
		@Size(max=200)
		private  String requirement;
		@Size(max=1000)
		private  String record;
		@Size(max=200)
		private  String keyInfo;
		@NotNull
		private  Date createDate;
		private  Date actionDate;
		@NotNull
		private  Task_StatusEnum status;
		private  int orderNum;

    }
}
