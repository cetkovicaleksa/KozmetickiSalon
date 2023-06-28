package exceptions;

@SuppressWarnings("serial")
public class NoPayloadDataException extends RuntimeException {
	
	public NoPayloadDataException() {
        super();
    }

    public NoPayloadDataException(String message) {
        super(message);
    }

    public NoPayloadDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPayloadDataException(Throwable cause) {
        super(cause);
    }

}
