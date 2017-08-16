package projectbase.domain.customcollection;

public class TransientUserCollectionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransientUserCollectionException() {
		super("A Transient User Collection shouldn't call this method!");
	}

	public TransientUserCollectionException(String msg) {
		super(msg);
	}
}
