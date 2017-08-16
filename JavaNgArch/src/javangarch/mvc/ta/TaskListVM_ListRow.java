package javangarch.mvc.ta;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class TaskListVM_ListRow
{
	private int id;
	private String name;
	private String userName ;
	private Date createDate;
	private Date planBeginDate;
	private Date planEndDate;
	private Date beginDate;
	private Date endDate;
	private Integer status;
	
}


