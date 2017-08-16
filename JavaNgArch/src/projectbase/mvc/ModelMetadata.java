package projectbase.mvc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import lombok.*;

public class ModelMetadata {
    private Class<?> containerType;
//    private bool _convertEmptyStringToNull = true;

//    private object _model;
    @Getter@Setter
    private Field field;
    private Method propertyGetter;
    private Class<?> modelType;
	private Map<String,ModelMetadata> properties;
	//    private readonly string _propertyName;
//    private Type _realModelType;
    private boolean requestValidationEnabled = true;
    private boolean isRequired;
    private boolean isDictEnum;
    private String dictEnumTypeName;
    private Integer maxLength;
    
    private String displayName;
    private Method displayMethod;

	public String getDisplayName() {
		return displayName;// ?? PropertyName ?? ModelType.Name;
	}

	public void setDisplayName(String value) {
		this.displayName = value;
		
	}

	public boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(boolean value) {
		this.isRequired = value;
	}

	public Map<String,ModelMetadata> getProperties() {
		return properties;
	}

	public Class<?> getContainerType() {
		return containerType;
	}

	public void setContainerType(Class<?> value) {
		this.containerType = value;
	}

	public Method getPropertyGetter() {
		return propertyGetter;
	}

	public void setPropertyGetter(Method value) {
		this.propertyGetter = value;
	}

	public boolean getRequestValidationEnabled() {
		return requestValidationEnabled;
	}

	public void setRequestValidationEnabled(boolean value) {
		this.requestValidationEnabled = value;
	}

	public Class<?> getModelType() {
		return modelType;
	}

	public void setModelType(Class<?> value) {
		this.modelType = value;
	}

	public boolean getIsDictEnum() {
		return isDictEnum;
	}

	public void setIsDictEnum(boolean value) {
		this.isDictEnum = value;
	}

	public String getDictEnumTypeName() {
		return dictEnumTypeName;
	}

	public void setDictEnumTypeName(String value) {
		this.dictEnumTypeName = value;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public void setProperties(Map<String, ModelMetadata> value) {
		this.properties = value;
	}

	public Method getDisplayMethod() {
		return displayMethod;
	}

	public void setDisplayMethod(Method displayMethod) {
		this.displayMethod = displayMethod;
	}
}
