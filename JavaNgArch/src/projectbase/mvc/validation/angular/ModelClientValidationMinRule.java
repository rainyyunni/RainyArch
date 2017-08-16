package projectbase.mvc.validation.angular;
    public class ModelClientValidationMinRule extends ModelClientValidationRule {
        public ModelClientValidationMinRule(Object minValue) {
            setValidationType("min");
            setMessageKey("field.min");
            setValidationParameter(minValue);
        }
        public ModelClientValidationMinRule(String errorMessage, Object minValue) {
        	this(minValue);
        	setErrorMessage(errorMessage);
        }
        public ModelClientValidationMinRule(String errorMessage, Object minValue, String displayName) {
        	this(minValue);
        	setErrorMessage(errorMessage,displayName,minValue);
        }
    }

