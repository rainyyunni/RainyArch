package projectbase.mvc.validation.angular;
    public class ModelClientValidationMaxRule extends ModelClientValidationRule {
        public ModelClientValidationMaxRule(Object maxValue) {
            setValidationType("max");
            setMessageKey("field.max");
            setValidationParameter(maxValue);
        }
        public ModelClientValidationMaxRule(String errorMessage, Object maxValue) {
        	this(maxValue);
        	setErrorMessage(errorMessage);
        }
        public ModelClientValidationMaxRule(String errorMessage, Object maxValue, String displayName) {
        	this(maxValue);
        	setErrorMessage(errorMessage,displayName,maxValue);
        }
    }

