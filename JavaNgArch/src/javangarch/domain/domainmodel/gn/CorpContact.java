package javangarch.domain.domainmodel.gn;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Embeddable;

import java.util.Date;
import projectbase.domain.BaseDomainObject;
import projectbase.domain.customcollection.IDomainList;
import projectbase.sharparch.domain.domainmodel.DomainSignature;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javangarch.domain.domainmodel.gn.Corp;


@Getter@Setter
@Embeddable
public class CorpContact {

	private String phone;
	private String name;
	private String position;

	public CorpContact(){
	}
	
	public CorpContact(String phone,String name,String position){
		this.phone=phone;
		this.name=name;
		this.position=position;
	}
}




