package projectbase.mvc.validation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import projectbase.utils.Res;


    public class ModelClientValidationRule {

        private final Map<String, Object> validationParameters = new HashMap<String, Object>();
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

		public Map<String, Object> getValidationParameters() {
			return validationParameters;
		}

		public String getMessageKey() {
			return messageKey;
		}

		public void setMessageKey(String value) {
			this.messageKey = value;
		}
		public void setErrorMessage(String value,Object ...args) {
			if(StringUtils.isEmpty(value) || value.startsWith("{java")){
				value=Res.ValidationMessages(messageKey, args);
			}
			if(value.startsWith("key=")){
				value=Res.ValidationMessages(value.substring(4), args);
			}
			errorMessage = value;
		}
    }
 
