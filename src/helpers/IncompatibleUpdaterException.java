package helpers;

@SuppressWarnings("serial")
public class IncompatibleUpdaterException extends RuntimeException {
	
	public IncompatibleUpdaterException() {
        super();
    }

    public IncompatibleUpdaterException(String message) {
        super(message);
    }

    public IncompatibleUpdaterException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompatibleUpdaterException(Throwable cause) {
        super(cause);
    }

}
