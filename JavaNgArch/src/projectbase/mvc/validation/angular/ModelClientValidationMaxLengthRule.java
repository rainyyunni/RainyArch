package projectbase.mvc.validation.angular;

    public class ModelClientValidationMaxLengthRule extends ModelClientValidationRule {
    	public ModelClientValidationMaxLengthRule(int maximumLength){
            setValidationType("ng-maxlength");
            setMessageKey("field.maxlength");
            setValidationParameter(maximumLength);
    	}
        public ModelClientValidationMaxLengthRule(String errorMessage, int maximumLength) {
        	this(maximumLength);
        	setErrorMessage(errorMessage);

        }
        public ModelClientValidationMaxLengthRule(String errorMessage, int maximumLength, String displayName) {
        	this(maximumLength);
        	setErrorMessage(errorMessage,displayName,String.valueOf(maximumLength));
        }
    }

