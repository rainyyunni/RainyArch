package projectbase.sharparch.domain.designbycontract;

/// <summary>
///    Design by Contract checks developed by http://www.codeproject.com/KB/cs/designbycontract.aspx.
/// 
///    Each method generates an exception or
///    a trace assertion statement if the contract is broken.
/// </summary>
/// <remarks>
///    This example shows how to call the Require method.
///    Assume DBC_CHECK_PRECONDITION is defined.
///    <code>
///        public void Test(int x)
///        {
///        try
///        {
///        Check.Require(x > 1, "x must be > 1");
///        }
///        catch (System.Exception ex)
///        {
///        Console.WriteLine(ex.ToString());
///        }
///        }
///    </code>
///    If you wish to use trace assertion statements, intended for Debug scenarios,
///    rather than exception handling then set 
/// 
///    <code>Check.UseAssertions = true</code>
/// 
///    You can specify this in your application entry point and maybe make it
///    dependent on conditional compilation flags or configuration file settings, e.g.,
///    <code>
///        #if DBC_USE_ASSERTIONS
///        Check.UseAssertions = true;
///        #endif
///    </code>
///    You can direct output to a Trace listener. For example, you could insert
///    <code>
///        Trace.Listeners.Clear();
///        Trace.Listeners.Add(new TextWriterTraceListener(Console.Out));
///    </code>
/// 
///    or direct output to a file or the Event Log.
/// 
///    (Note: For ASP.NET clients use the Listeners collection
///    of the Debug, not the Trace, Object and, for a Release build, only exception-handling
///    is possible.)
/// </remarks>

public class Check {
	private static boolean useAssertions;

	// / <summary>
	// / Set this if you wish to use Trace Assert statements
	// / instead of exception handling.
	// / (The Check class uses exception handling by default.)
	// / </summary>
	public static boolean isUseAssertions() {
		return useAssertions;
	}

	public static void setUseAssertions(boolean value) {
		useAssertions = value;
	}

	// / <summary>
	// / Is error handling being used?
	// / </summary>
	private static boolean isUseExceptions() {
		return !useAssertions;
	}

	// / <summary>
	// / Assertion check.
	// / </summary>
	public static void Assert(boolean assertion, String message) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new AssertionException(message);
			}
		} else {
			assert assertion : "Assertion: " + message;
		}
	}

	// / <summary>
	// / Assertion check.
	// / </summary>
	public static void Assert(boolean assertion, String message, Exception inner) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new AssertionException(message, inner);
			}
		} else {
			assert assertion : "Assertion: " + message;
		}
	}

	// / <summary>
	// / Assertion check.
	// / </summary>
	public static void Assert(boolean assertion) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new AssertionException("Assertion failed.");
			}
		} else {
			assert assertion : "Assertion failed.";
		}
	}

	// / <summary>
	// / Postcondition check.
	// / </summary>
	public static void Ensure(boolean assertion, String message) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new PostconditionException(message);
			}
		} else {
			assert assertion : "Postcondition: " + message;
		}
	}

	// / <summary>
	// / Postcondition check.
	// / </summary>
	public static void Ensure(boolean assertion, String message, Exception inner) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new PostconditionException(message, inner);
			}
		} else {
			assert assertion : "Postcondition: " + message;
		}
	}

	// / <summary>
	// / Postcondition check.
	// / </summary>
	public static void Ensure(boolean assertion) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new PostconditionException("Postcondition failed.");
			}
		} else {
			assert assertion : "Postcondition failed.";
		}
	}

	// / <summary>
	// / Invariant check.
	// / </summary>
	public static void Invariant(boolean assertion, String message) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new InvariantException(message);
			}
		} else {
			assert assertion : "Invariant: " + message;
		}
	}

	// / <summary>
	// / Invariant check.
	// / </summary>
	public static void Invariant(boolean assertion, String message, Exception inner) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new InvariantException(message, inner);
			}
		} else {
			assert assertion : "Invariant: " + message;
		}
	}

	// / <summary>
	// / Invariant check.
	// / </summary>
	public static void Invariant(boolean assertion) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new InvariantException("Invariant failed.");
			}
		} else {
			assert assertion : "Invariant failed.";
		}
	}

	// / <summary>
	// / Precondition check - should run regardless of preprocessor directives.
	// / </summary>
	public static void Require(boolean assertion, String message) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new PreconditionException(message);
			}
		} else {
			assert assertion : "Precondition: " + message;
		}
	}

	// / <summary>
	// / Precondition check - should run regardless of preprocessor directives.
	// / </summary>
	public static void Require(boolean assertion, String message, Exception inner) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new PreconditionException(message, inner);
			}
		} else {
			assert assertion : "Precondition: " + message;
		}
	}

	// / <summary>
	// / Precondition check - should run regardless of preprocessor directives.
	// / </summary>
	public static void Require(boolean assertion) {
		if (isUseExceptions()) {
			if (!assertion) {
				throw new PreconditionException("Precondition failed.");
			}
		} else {
			assert assertion : "Precondition failed.";
		}
	}
}
