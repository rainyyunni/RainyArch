

package javangarch.mvc.home;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import projectbase.mvc.DisplayName;
	@Getter@Setter
    @DisplayName("User")
    public class LoginAttemptViewModel
    {
    	@NotNull
    	@DisplayName("CorpCode")
    	private String corpCode;
    	@NotNull
        private String code;
        @NotNull
        private String password;


        public LoginAttemptViewModel()
        {
        	corpCode = "";
            code = "";
            password = "";
        }

    }

