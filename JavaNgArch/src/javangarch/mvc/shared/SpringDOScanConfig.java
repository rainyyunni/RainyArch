package javangarch.mvc.shared;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
/**
 * just to find all DO classes for future annotation appending
 * @author 
 *
 */
@Configuration
@ComponentScan(basePackages = {"javangarch.domain.domainmodel"},
useDefaultFilters=false,
includeFilters = @Filter(type = FilterType.CUSTOM, value = projectbase.data.hibernatemapbycode.convention.DOClassAnnotationAppender.class)
)
public class SpringDOScanConfig {
}
