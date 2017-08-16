package projectbase.sharparch.domain.designbycontract;

// End Check
/// <summary>
///     Exception raised when an assertion fails.
/// </summary>
public class AssertionException extends DesignByContractException {
	private static final long serialVersionUID = 1;
	
	// / <summary>
	// / Assertion Exception.
	// / </summary>
	public AssertionException() {
	}

	// / <summary>
	// / Assertion Exception.
	// / </summary>
	public AssertionException(String message) {
		super(message);
	}

	// / <summary>
	// / Assertion Exception.
	// / </summary>
	public AssertionException(String message, Exception inner) {
		super(message, inner);
	}
}

