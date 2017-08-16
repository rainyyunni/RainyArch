package projectbase.mvc.validation;
    public class ModelClientValidationRangeRule extends ModelClientValidationRule {
        private ModelClientValidationRangeRule(Object minValue, Object maxValue) {
            setValidationType("range");
            setMessageKey("field.range");
            getValidationParameters().put("min",minValue);
            getValidationParameters().put("max",maxValue);
        }
        public ModelClientValidationRangeRule(String errorMessage, Object minValue, Object maxValue) {
        	this(minValue,maxValue);
        	setErrorMessage(errorMessage);
        }
        public ModelClientValidationRangeRule(String errorMessage, Object minValue, Object maxValue,String displayName) {
        	this(minValue,maxValue);
        	setErrorMessage(errorMessage,displayName,minValue,maxValue);
        }
    }

