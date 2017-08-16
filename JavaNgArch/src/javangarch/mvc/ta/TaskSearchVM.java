package javangarch.mvc.ta;
import projectbase.mvc.DisplayName;
import lombok.*;

@Getter@Setter
public class TaskSearchVM 
{
	public TaskSearchVM()
	{
		input = new SearchInput();
	}
       
	private SearchInput input;
    
	@Getter@Setter
	@DisplayName("Task")
	public class SearchInput 
	{
		private  String name;
	}
}

