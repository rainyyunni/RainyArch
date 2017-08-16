package javangarch.mvc.ta;
import java.util.ArrayList;
import java.util.List;

import javangarch.domain.domainmodel.ta.Task;
import projectbase.mvc.ListInput;
import lombok.*;

@Getter@Setter
public class TaskListVM
{
    public TaskListVM()
    {
        resultList = new ArrayList<TaskListVM_ListRow>();
        input = new ListInput(5);
        input.setOrderExpression(Task.DefaultSort);
    }

    private List<TaskListVM_ListRow> resultList;
    
    private ListInput input;

}

