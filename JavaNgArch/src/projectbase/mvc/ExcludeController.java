

package projectbase.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * to exclude a class from being annotated by default as a controller class,for example,a base class 
 * @author tudoubaby
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
    public @interface ExcludeController 
    {
    }

