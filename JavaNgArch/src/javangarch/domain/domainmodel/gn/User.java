package javangarch.domain.domainmodel.gn;

import java.util.Date;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CollectionType;

import lombok.Getter;
import lombok.Setter;
import projectbase.domain.BaseDomainObject;
import projectbase.domain.customcollection.IDomainList;
import projectbase.sharparch.domain.domainmodel.DomainSignature;
@Getter@Setter
public class User extends BaseDomainObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String DefaultSort = "this.code asc";
	@DomainSignature
	private Corp corp;
	private Dept dept;
	@DomainSignature
	private String code ;
	private String password ;
	private String name ;
	private String cellPhone ;
    private String loginMark ;
    private boolean isActive ;
    private List<Func> Funcs;
    

	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean value) {
		this.isActive = value;
	}
	@OneToMany
	@JoinTable(	name="gn_UserFunc",joinColumns=@JoinColumn(name="UserID"),inverseJoinColumns=@JoinColumn(name="FuncID")	)
	public List<Func> getFuncs() {
		return Funcs;
	}


    
    

}
