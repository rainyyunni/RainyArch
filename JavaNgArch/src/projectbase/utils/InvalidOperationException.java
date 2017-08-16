package projectbase.utils;

public class InvalidOperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidOperationException() {
	}

	public InvalidOperationException(String msg) {
		super(msg);
	}

}
