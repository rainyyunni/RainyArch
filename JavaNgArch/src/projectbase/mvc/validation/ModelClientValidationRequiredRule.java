package projectbase.mvc.validation;

    public class ModelClientValidationRequiredRule extends ModelClientValidationRule {
        private ModelClientValidationRequiredRule() {
            setValidationType("required");
            setMessageKey("field.required");
        }
        public ModelClientValidationRequiredRule(String errorMessage) {
        	this();
            setErrorMessage(errorMessage);
        }
        public ModelClientValidationRequiredRule(String errorMessage,String displayName) {
        	this();
            setErrorMessage(errorMessage,displayName);
        }
    }

