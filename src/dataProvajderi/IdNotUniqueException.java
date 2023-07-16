package dataProvajderi;

@SuppressWarnings("serial")
public class IdNotUniqueException extends RuntimeException {
	
	public IdNotUniqueException() {
        super();
    }

    public IdNotUniqueException(String message) {
        super(message);
    }

    public IdNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdNotUniqueException(Throwable cause) {
        super(cause);
    }

}
