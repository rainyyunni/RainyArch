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
	public class CorpContactEditVM {
		
		@NotNull
		@Size(max=20)
		private  String phone;
		@NotNull
		@Size(max=10)
		private  String name;
		@Size(max=20)
		private  String position;
	}

