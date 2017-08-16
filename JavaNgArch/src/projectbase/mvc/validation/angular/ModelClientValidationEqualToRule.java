package projectbase.mvc.validation.angular;
    public class ModelClientValidationEqualToRule extends ModelClientValidationRule {
        private ModelClientValidationEqualToRule(Object other){
            setValidationType("equalto");
            setMessageKey("field.equalto");
            setValidationParameter(other);
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

