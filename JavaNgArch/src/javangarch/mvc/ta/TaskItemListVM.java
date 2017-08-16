package javangarch.mvc.ta;
import java.util.ArrayList;
import java.util.List;

import javangarch.domain.domainmodel.ta.Task;
import javangarch.domain.domainmodel.ta.TaskItem;
import projectbase.mvc.ListInput;
import lombok.*;

@Getter@Setter
public class TaskItemListVM
{
    public TaskItemListVM()
    {
        resultList = new ArrayList<TaskItemListVM_ListRow>();
        input = new ListInput(5);
        input.setOrderExpression(TaskItem.DefaultSort);
    }

    private List<TaskItemListVM_ListRow> resultList;
    
    private ListInput input;

}

