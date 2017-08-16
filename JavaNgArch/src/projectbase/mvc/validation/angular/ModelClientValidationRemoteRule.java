/*package projectbase.mvc.validation.angular;

import org.apache.commons.lang3.StringUtils;

    public class ModelClientValidationRemoteRule extends ModelClientValidationRule {

        //[SuppressMessage("Microsoft.Design", "CA1054:UriParametersShouldNotBeStrings", Justification = "The value is a not a regular URL since it may contain ~/ ASP.NET-specific characters")]
        public ModelClientValidationRemoteRule(String errorMessage, String url, String httpMethod, String additionalFields) {
        	setErrorMessage(errorMessage);
            setValidationType("remote");
            setValidationParameter(url);

            if (!StringUtils.isEmpty(httpMethod)) {
            	getValidationParameters().put("type",httpMethod);
            }

            getValidationParameters().put("additionalfields", additionalFields);
        }
    }
*/