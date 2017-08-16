

package projectbase.mvc;
import projectbase.mvc.result.JavaScriptSerializer;
    public class BaseViewModel
    {
        public String ToJsonString()
        {
            return (new JavaScriptSerializer()).Serialize(this);
        }
        public String ToJsonString(Object data)
        {
            return (new JavaScriptSerializer()).Serialize(data);
        }
    }


