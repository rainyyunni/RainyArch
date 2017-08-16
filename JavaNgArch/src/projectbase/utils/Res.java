package projectbase.utils;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
	/***
	 * look for localization resource in ProjectHierarchy,falling back on projectbase.mvc.resource
	 * @author tudoubaby
	 *
	 */
    public class Res
    {
    	protected static ResourceBundle messages =
    			ResourceBundle.getBundle(ProjectHierarchy.getMvcNS()+".resource."+ProjectHierarchy.MessagesResourceClassKey);
    	protected static ResourceBundle displayNames =
    			ResourceBundle.getBundle(ProjectHierarchy.getMvcNS()+".resource."+ProjectHierarchy.DisplayNameResourceClassKey);
    	protected static ResourceBundle validationMessages =
    			ResourceBundle.getBundle(ProjectHierarchy.getMvcNS()+".resource."+ProjectHierarchy.ValidationMessagesResourceClassKey);
       	protected static ResourceBundle messages_default =
    			ResourceBundle.getBundle("projectbase.mvc.resource.Messages");
    	protected static ResourceBundle displayNames_default =
    			ResourceBundle.getBundle("projectbase.mvc.resource.DisplayNames");
    	protected static ResourceBundle validationMessages_default =
    			ResourceBundle.getBundle("projectbase.mvc.resource.ValidationMessages");

      public static String M(String resourceName)
        {
            String s = Messages(resourceName);
            if (s == null || s.contains("%1$"))
                s = DisplayName(resourceName);

            return s;
        }
        public static String M(String resourceName, String displayResourceName)
        {
            return Messages(resourceName, DisplayName(displayResourceName));
        }
        public static String M(String resourceName, String displayResourceName0, String displayResourceName1)
        {
            return Messages(resourceName, DisplayName(displayResourceName0), DisplayName(displayResourceName1));
        }
        public static String M(String resourceName, String displayResourceName0, String displayResourceName1, String displayResourceName2)
        {
            return Messages(resourceName, DisplayName(displayResourceName0), DisplayName(displayResourceName1),DisplayName(displayResourceName1));
        }
        public static String V(String resourceName)
        {
        	String s = ValidationMessages(resourceName);
            if (s == null || s.contains("%1$"))
                s = DisplayName(resourceName);

            return s;
        }
        public static String V(String resourceName, String displayResourceName)
        {
            return ValidationMessages(resourceName, DisplayName(displayResourceName));
        }
        public static String V(String resourceName, String displayResourceName0, String displayResourceName1)
        {
            return ValidationMessages(resourceName, DisplayName(displayResourceName0), DisplayName(displayResourceName1));
        }
        public static String V(String resourceName, String displayResourceName0, String displayResourceName1, String displayResourceName2)
        {
            return ValidationMessages(resourceName, DisplayName(displayResourceName0), DisplayName(displayResourceName1), DisplayName(displayResourceName1));
        }
        public static String D(String resourceName)
        {
            return DisplayName(resourceName);
        }
        public static String DisplayName(String resourceName)
        {
        	String value=resourceName;
        	try{
        		value=displayNames.getString(resourceName);
        	}catch(MissingResourceException e){
        		try{
        			value=displayNames_default.getString(resourceName);
        		}catch(MissingResourceException ex){}
        	}
            return value;
        }
        public static String Messages(String resourceName, Object... args)
        {
        	String value="Messages:" + resourceName;;
        	try{
        		value=messages.getString(resourceName);
        	}catch(MissingResourceException e){
            	try{
            		value=messages_default.getString(resourceName);
            	}catch(MissingResourceException ex){}
        	}
            return String.format(value,args);

        }
        public static String ValidationMessages(String resourceName, Object... args)
        {
        	String value="ValidationMessages:" + resourceName;;
        	try{
        		value=validationMessages.getString(resourceName);
        	}catch(MissingResourceException e){
        		try{
            		value=validationMessages_default.getString(resourceName);
            	}catch(MissingResourceException ex){}
        	}
            return String.format(value,args);
        }
    }

