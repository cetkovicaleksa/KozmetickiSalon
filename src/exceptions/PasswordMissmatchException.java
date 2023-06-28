package exceptions;

@SuppressWarnings("serial")
public class PasswordMissmatchException extends RuntimeException {
	
	public PasswordMissmatchException() {
        super();
    }

    public PasswordMissmatchException(String message) {
        super(message);
    }

    public PasswordMissmatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordMissmatchException(Throwable cause) {
        super(cause);
    }

}