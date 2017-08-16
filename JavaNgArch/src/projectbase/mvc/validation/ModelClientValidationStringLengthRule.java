package projectbase.mvc.validation;

    public class ModelClientValidationStringLengthRule extends ModelClientValidationRule {
    	private ModelClientValidationStringLengthRule(int minimumLength, int maximumLength){
            setValidationType("length");
            setMessageKey("field.length");
            if (minimumLength != 0) {
                getValidationParameters().put("min",minimumLength);
            }

            if (maximumLength != Integer.MAX_VALUE) {
            	getValidationParameters().put("max",maximumLength);
            }
    	}
        public ModelClientValidationStringLengthRule(String errorMessage, int minimumLength, int maximumLength) {
        	this(minimumLength,maximumLength);
        	setErrorMessage(errorMessage);

        }
        public ModelClientValidationStringLengthRule(String errorMessage, int minimumLength, int maximumLength,String displayName) {
        	this(minimumLength,maximumLength);
        	setErrorMessage(errorMessage,displayName,String.valueOf(minimumLength),String.valueOf(maximumLength));
        }
    }

