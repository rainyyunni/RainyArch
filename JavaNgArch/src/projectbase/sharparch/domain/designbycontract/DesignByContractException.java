package projectbase.sharparch.domain.designbycontract; 

/// <summary>
///     Exception raised when a contract is broken.
///     Catch this exception type if you wish to differentiate between 
///     any DesignByContract exception and other runtime exceptions.
/// </summary>
public class DesignByContractException extends RuntimeException {
	private static final long serialVersionUID = 1;

	protected DesignByContractException() {
	}

	protected DesignByContractException(String message) {
		super(message);
	}

	protected DesignByContractException(String message, Exception inner) {
		super(message + " with inner exception: " + inner.getMessage());
	}
}
