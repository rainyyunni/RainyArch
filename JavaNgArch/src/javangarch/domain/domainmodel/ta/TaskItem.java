package javangarch.domain.domainmodel.ta;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javangarch.domain.domainmodel.gn.User;
import projectbase.domain.BaseDomainObject;
import projectbase.domain.customcollection.IDomainList;
import projectbase.sharparch.domain.domainmodel.DomainSignature;
import lombok.*;

@Getter@Setter
public class TaskItem extends BaseDomainObject {
	
	public static final String DefaultSort = "this.orderNum";

	@DomainSignature
	private Task task;
	@DomainSignature
	private String brief;
	private User user;
	private String requirement;
	private String record;
	private String keyInfo;
	private Date createDate;
	private Date actionDate;
	private Integer status;
	private int orderNum;

}




