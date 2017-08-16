// ----------------------------------------------------------
// Taken from MvcContrib to reduce number of dependencies. Was only method in MvcContrib being used in project.
// ----------------------------------------------------------

package projectbase.sharparch.mvc;


import projectbase.mvc.BaseController;
import projectbase.mvc.GlobalConstant;

    public class ControllerExtensions
    {
        /// <summary>
        /// Determines whether the specified type is a controller
        /// </summary>
        /// <param name="type">Type to check</param>
        /// <returns>True if type is a controller, otherwise false</returns>
        public static boolean IsController(Class<?> clazz)
        {
            return clazz != null
            		&& clazz.getName().endsWith(GlobalConstant.Controller_ClassSuffix)
                  	&& BaseController.class.isAssignableFrom(clazz);
        }
        public static boolean IsController(String classname)
        {
            return classname != null 
            		&& classname.endsWith(GlobalConstant.Controller_ClassSuffix) && !classname.endsWith("BaseController");
        }

    }
