package javangarch.mvc.gn;
import java.util.List;

import javangarch.domain.domainmodel.gn.Dept;
import lombok.*;
import projectbase.domain.DORef;
import projectbase.mvc.DisplayName;

@Getter@Setter
public class UserSearchVM 
{
	public UserSearchVM()
	{
		input = new SearchInput();
	}
	
	private List<DORef<Dept>> depts;
	private SearchInput input;
    
	@Getter@Setter
	@DisplayName("User")
	public class SearchInput 
	{
		private DORef<Dept> dept;
	}
}

