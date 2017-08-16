package projectbase.mvc.validation;
    public class ModelClientValidationRegexRule extends ModelClientValidationRule {
        private ModelClientValidationRegexRule(String pattern) {
            setValidationType("regex");
            setMessageKey("field.regex");
            getValidationParameters().put("pattern", pattern);
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

