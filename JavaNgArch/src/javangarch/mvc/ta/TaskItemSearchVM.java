package javangarch.mvc.ta;
import projectbase.mvc.DisplayName;
import lombok.*;

@Getter@Setter
public class TaskItemSearchVM 
{
	public TaskItemSearchVM()
	{
		input = new SearchInput();
	}
       
	private SearchInput input;
    
	@Getter@Setter
	@DisplayName("TaskItem")
	public class SearchInput 
	{
           
	}
}

