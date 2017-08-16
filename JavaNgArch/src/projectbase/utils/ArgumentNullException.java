package projectbase.utils;

public class ArgumentNullException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ArgumentNullException() {
	}

	public ArgumentNullException(String msg) {
		super(msg);
	}

}
