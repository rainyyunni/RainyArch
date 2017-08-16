package projectbase.mvc.validation;
    public class ModelClientValidationEqualToRule extends ModelClientValidationRule {
        private ModelClientValidationEqualToRule(Object other){
            setValidationType("equalto");
            setMessageKey("field.equalto");
            getValidationParameters().put("other",other);
        }
        public ModelClientValidationEqualToRule(String errorMessage, Object other){
        	this(other);
        	setErrorMessage(errorMessage);
        }
        public ModelClientValidationEqualToRule(String errorMessage, Object other,String displayName){
        	this(other);
        	setErrorMessage(errorMessage,displayName,other);
        }
    }

