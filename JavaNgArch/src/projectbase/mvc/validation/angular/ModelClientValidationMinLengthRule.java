package projectbase.mvc.validation.angular;

    public class ModelClientValidationMinLengthRule extends ModelClientValidationRule {
    	public ModelClientValidationMinLengthRule(int minimumLength){
            setValidationType("ng-minlength");
            setMessageKey("field.minlength");
            setValidationParameter(minimumLength);
    	}
        public ModelClientValidationMinLengthRule(String errorMessage, int minimumLength) {
        	this(minimumLength);
        	setErrorMessage(errorMessage);

        }
        public ModelClientValidationMinLengthRule(String errorMessage, int minimumLength, String displayName) {
        	this(minimumLength);
        	setErrorMessage(errorMessage,displayName,String.valueOf(minimumLength));
        }
    }

