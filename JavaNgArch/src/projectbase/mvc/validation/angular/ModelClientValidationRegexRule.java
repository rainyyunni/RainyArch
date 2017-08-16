package projectbase.mvc.validation.angular;
    public class ModelClientValidationRegexRule extends ModelClientValidationRule {
        public ModelClientValidationRegexRule(String pattern) {
            setValidationType("ng-pattern");
            setMessageKey("field.pattern");
            if(!pattern.startsWith("/"))pattern="/"+pattern+"/";
            setValidationParameter(pattern);
        }
        public ModelClientValidationRegexRule(String errorMessage, String pattern) {
        	this(pattern);
        	setErrorMessage(errorMessage);
        }
        public ModelClientValidationRegexRule(String errorMessage, String pattern,String displayname) {
        	this(pattern);
        	setErrorMessage(errorMessage,displayname,pattern);
        }
    }

