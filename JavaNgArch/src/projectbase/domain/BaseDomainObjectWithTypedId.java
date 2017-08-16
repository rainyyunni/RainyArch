package projectbase.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import projectbase.sharparch.domain.domainmodel.DomainSignature;
import projectbase.sharparch.domain.domainmodel.EntityWithTypedId;
import projectbase.domain.RefText;
import projectbase.utils.JavaArchException;

public abstract class BaseDomainObjectWithTypedId<TId> extends
		EntityWithTypedId<TId> {

	private static final long serialVersionUID = 1L;
	//protected static List<Method> refTextGetter;
	
	/**
	 * use this formula string as a hql selector to get RefText from db
	 * @return
	 */
	public static String GetRefTextFormula(){
		return "this.id||'' as refText";
	}
	/**
	 * override this transient property to customize refText string for the DO object
	 * use this method to get RefText from a loaded DO
	 * @return
	 */
	@Transient
	public String getRefText() {
		List<Method> signatures =new ArrayList<Method>();
        for(Method property:this.getClass().getMethods()){
        	if(property.getAnnotation(RefText.class)!=null && property.getReturnType()==String.class)
				try {
					return (String)property.invoke(this);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					throw new JavaArchException(e);
				}
			else if (property.getAnnotation(DomainSignature.class)!=null && property.getReturnType()==String.class)
        		signatures.add(property);
        }
		String s = "";
		for (Method prop : signatures) {
			try {
				s += prop.invoke(this) + "-";
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new JavaArchException(e);
			}
		}
		if(StringUtils.isEmpty(s)) 
			throw new JavaArchException("Must define RefText, or DomainSignature on String props for a DomainObject class:"+this.getClass().getSimpleName());
		return s.substring(0, s.length() - 1);
	}

	public String toString() {
		String s = "";
		Iterable<Method> signatures = GetTypeSpecificSignatureProperties();
		for (Method prop : signatures) {
			try {
				s += prop.invoke(this) + "-";
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		if(s.equals("")) return super.toString();
		return s.substring(0, s.length() - 1);
	}

}
