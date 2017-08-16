package projectbase.domain;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

//[AttributeUsage(AttributeTargets.Property,AllowMultiple=false,Inherited=true)]
@Target({ ElementType.METHOD,ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface RefText {
}
