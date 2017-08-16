package javangarch.mvc.gn;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javangarch.domain.domainmodel.gn.Dept;
import javangarch.mvc.gn.DeptEditVM.EditInput;
import projectbase.mvc.DisplayName;
import projectbase.mvc.ListInput;
import lombok.*;

@Getter@Setter
public class DeptListVM
{
    public DeptListVM()
    {
        resultList = new ArrayList<DeptListVM_ListRow>();
        input = new ListInput(5);
        input.setOrderExpression(Dept.DefaultSort);
    }

    private List<DeptListVM_ListRow> resultList;
    
    private ListInput input;
    
	private DeptEditVM.EditInput editInput=(new DeptEditVM()).new EditInput();

}

