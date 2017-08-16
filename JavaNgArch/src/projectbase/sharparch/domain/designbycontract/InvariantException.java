package projectbase.sharparch.domain.designbycontract;

/// <summary>
///     Exception raised when an invariant fails.
/// </summary>
public class InvariantException extends DesignByContractException {
	private static final long serialVersionUID = 1;
	
	// / <summary>
	// / Invariant Exception.
	// / </summary>
	public InvariantException() {
	}

	// / <summary>
	// / Invariant Exception.
	// / </summary>
	public InvariantException(String message) {
		super(message);
	}

	// / <summary>
	// / Invariant Exception.
	// / </summary>
	public InvariantException(String message, Exception inner) {
		super(message, inner);
	}
}
