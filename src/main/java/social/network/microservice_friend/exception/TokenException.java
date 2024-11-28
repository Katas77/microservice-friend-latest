package social.network.microservice_friend.exception;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
