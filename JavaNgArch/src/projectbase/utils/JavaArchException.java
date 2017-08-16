package projectbase.utils;

public class JavaArchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JavaArchException() {
	}

	public JavaArchException(String msg) {
		super(msg);
	}
	
	public JavaArchException(Exception e) {
		super(e);
	}

}
