package projectbase.mvc.validation.angular;

    public class ModelClientValidationRequiredRule extends ModelClientValidationRule {
    	public ModelClientValidationRequiredRule() {
            setValidationType("required");
            setValidationParameter("required");
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

