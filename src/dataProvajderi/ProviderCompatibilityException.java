package dataProvajderi;

@SuppressWarnings("serial")
public class ProviderCompatibilityException extends RuntimeException{

	public ProviderCompatibilityException() {
        super();
    }

    public ProviderCompatibilityException(String message) {
        super(message);
    }

    public ProviderCompatibilityException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProviderCompatibilityException(Throwable cause) {
        super(cause);
    }
    
}
