package javangarch.mvc.ta;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class TaskItemListVM_ListRow
{
	private int id;
	private String taskName ;
	private String brief;
	private String userName ;
	private String requirement;
	private String record;
	private String keyInfo;
	private Date createDate;
	private Date actionDate;
	private Integer status;
	private int orderNum;
	
}


