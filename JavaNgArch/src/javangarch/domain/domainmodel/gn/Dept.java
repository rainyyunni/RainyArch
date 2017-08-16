package javangarch.domain.domainmodel.gn;

import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import projectbase.domain.BaseDomainObject;
import projectbase.domain.RefText;
import projectbase.domain.customcollection.IDomainList;
import projectbase.sharparch.domain.domainmodel.DomainSignature;
@Getter
@Setter
public class Dept extends BaseDomainObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String DefaultSort = "this.code asc";

	@DomainSignature
	private Corp corp;
	@DomainSignature
	private String code;

	@RefText
	private String name;

	private IDomainList<User> users;
	public List<User> getUsers() {
		return users;
	}

	private List<Func> funcs;
	@OneToMany
	@JoinTable(	name="gn_deptfunc",joinColumns=@JoinColumn(name="DeptID"),inverseJoinColumns=@JoinColumn(name="FuncID")	)
	public List<Func> getFuncs() {
		return funcs;
	}

}