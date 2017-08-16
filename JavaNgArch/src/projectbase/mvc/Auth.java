

package projectbase.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

    /// <summary>
    /// check user's priviledges to accessing actions.
    /// you can assign funccode to each action,or the default value for funccode is controllername.actionname 
    /// </summary>
@Target({ElementType.METHOD , ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
    public @interface Auth 
    {
    	public String FuncCode() default "";
    	public boolean AuthEnabled() default true;
    	public String value() default "";
    }

