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
public class Task extends BaseDomainObject {
	
	public static final String DefaultSort = "this.name";

	@DomainSignature
	private String name;
	private User user;
	private Date createDate;
	private Date planBeginDate;
	private Date planEndDate;
	private Date beginDate;
	private Date endDate;
	private Integer status;

}




