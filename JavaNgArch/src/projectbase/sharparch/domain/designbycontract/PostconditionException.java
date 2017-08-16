package projectbase.sharparch.domain.designbycontract;

/// <summary>
///     Exception raised when a postcondition fails.
/// </summary>
public class PostconditionException extends DesignByContractException {
	private static final long serialVersionUID = 1;

	// / <summary>
	// / Postcondition Exception.
	// / </summary>
	public PostconditionException() {
	}

	// / <summary>
	// / Postcondition Exception.
	// / </summary>
	public PostconditionException(String message) {
		super(message);
	}

	// / <summary>
	// / Postcondition Exception.
	// / </summary>
	public PostconditionException(String message, Exception inner) {
		super(message, inner);
	}
}
