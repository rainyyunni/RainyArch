package projectbase.sharparch.domain.designbycontract;

/// <summary>
///     Exception raised when a precondition fails.
/// </summary>
public class PreconditionException extends DesignByContractException {
	private static final long serialVersionUID = 1;

	// / <summary>
	// / Precondition Exception.
	// / </summary>
	public PreconditionException() {
	}

	// / <summary>
	// / Precondition Exception.
	// / </summary>
	public PreconditionException(String message) {
		super(message);
	}

	// / <summary>
	// / Precondition Exception.
	// / </summary>
	public PreconditionException(String message, Exception inner) {
		super(message, inner);
	}
}
