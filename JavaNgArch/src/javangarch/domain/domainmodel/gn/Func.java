package javangarch.domain.domainmodel.gn;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import projectbase.domain.BaseDomainObject;
import projectbase.sharparch.domain.domainmodel.DomainSignature;
@Getter@Setter
public class Func extends BaseDomainObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String DefaultSort = "Name asc";

	private String name;
	@DomainSignature
	private String level;
	private String code;

}