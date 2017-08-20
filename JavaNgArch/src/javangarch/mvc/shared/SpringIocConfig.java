package javangarch.mvc.shared;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
@Configuration
@ComponentScan(basePackages = {"javangarch.bd","javangarch.data","javangarch.mvc"},
		useDefaultFilters=false,
		includeFilters = @Filter(type = FilterType.CUSTOM, value = projectbase.bd.SpringComponentRegistrar.class)

)
public class SpringIocConfig {
}
