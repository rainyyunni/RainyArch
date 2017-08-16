package projectbase.domain;

import projectbase.sharparch.domain.domainmodel.DomainSignature;

public class NamedIntEntity extends BaseDomainObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _name;

	@DomainSignature
	public String getName() {
		return _name;
	}

	public void setName(String value) {
		this._name = value;
	}

}
