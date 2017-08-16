
package projectbase.mvc.converter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import projectbase.domain.DictEnum;

public class StringToDictEnumConverterFactory implements
		ConverterFactory<String, DictEnum> {
	public <T extends DictEnum> Converter<String, T> getConverter(
			Class<T> targetType) {
		return new StringToDictEnumConverter<T>(targetType);
	}

	private final class StringToDictEnumConverter<T extends DictEnum> implements
			Converter<String, T> {
		private Class<T> dicEnumType;

		public StringToDictEnumConverter(Class<T> dicEnumType) {
			this.dicEnumType = dicEnumType;
		}

		public T convert(String source) {
			if(StringUtils.isEmpty(source)) return null;
			return DictEnum.ValueOf(this.dicEnumType, Integer.valueOf(source.trim()));
		}
	}

}