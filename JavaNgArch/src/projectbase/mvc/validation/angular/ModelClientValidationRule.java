package projectbase.mvc.validation.angular;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import projectbase.utils.Res;


    public class ModelClientValidationRule {

        private Object validationParameter=null ;
        private String validationType="";
        private String errorMessage;
        private String messageKey;
        
        public String getValidationType() {
			return validationType;
		}

		public void setValidationType(String value) {
			this.validationType = value;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String value) {
			errorMessage = value;
		}
		public Object getValidationParameter() {
			return validationParameter;
		}
		public void setValidationParameter(Object value) {
			validationParameter = value;
		}
		public String getMessageKey() {
			return messageKey;
		}

		public void setMessageKey(String value) {
			this.messageKey = value;
		}
		public void setErrorMessage(String value,Object ...args) {
			if(value.startsWith("key=")){
				value=value.substring(4);
			}
			errorMessage = value;
		}


    }
 
