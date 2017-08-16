package javangarch.mvc.gn;
import javax.validation.constraints.*;

import javangarch.domain.domainmodel.gn.CorpContact;
import projectbase.domain.DORef;
import projectbase.mvc.DisplayName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.*;

@Getter@Setter
public class CorpEditVM {
	public CorpEditVM() {
		input = new EditInput();
	}
  

	private EditInput input;

	@Getter@Setter
	@DisplayName("Corp")
	public class EditInput {
		
		private int id;
		
		@NotNull
		@Size(max=10)
		private  String code;
		@NotNull
		@Size(max=50)
		private  String name;
		@Size(max=20)
		private  String phone;
		
		private List<CorpContactEditVM> contactList=new ArrayList<CorpContactEditVM>();
		private CorpContactEditVM newContact=new CorpContactEditVM();

    }
}
