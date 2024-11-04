package kun.uz.exceptions;

public class AppBadAuthorizationException extends RuntimeException {
    public AppBadAuthorizationException(String message) {
        super(message);
    }
}
