package javangarch.domain.domainmodel.gn;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
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
public class Corp extends BaseDomainObject {

	private static final long serialVersionUID = 1L;
	public static final String DefaultSort = "Name asc";

	@DomainSignature
	private String code;
	@RefText
	private String name;
	private String phone;

	private IDomainList<Dept> depts;
	public List<Dept> getDepts() {
		return depts;
	}
	
	private List<Func> funcs;
	@OneToMany
	@JoinTable(name = "gn_corpfunc", joinColumns = @JoinColumn(name = "CorpID") , inverseJoinColumns = @JoinColumn(name = "FuncID") )
	public List<Func> getFuncs() {
		return funcs;
	}  
	
	private List<CorpContact> contacts;
	@ElementCollection
	@CollectionTable(name = "gn_corpcontact", joinColumns = @JoinColumn(name = "CorpID")  )
	public List<CorpContact> getContacts() {
		return contacts;
	}
	
}
