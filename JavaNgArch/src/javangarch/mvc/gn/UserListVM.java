package javangarch.mvc.gn;
import java.util.ArrayList;
import java.util.List;

import javangarch.domain.domainmodel.gn.User;
import projectbase.mvc.ListInput;
import lombok.*;

@Getter@Setter
public class UserListVM
{
    public UserListVM()
    {
        resultList = new ArrayList<UserListVM_ListRow>();
        input = new ListInput(5);
    }

    private List<UserListVM_ListRow> resultList;
    
    private ListInput input;
    
    public Integer deptId;

}

