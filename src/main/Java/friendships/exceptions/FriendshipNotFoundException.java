package friendships.exceptions;

public class FriendshipNotFoundException extends RuntimeException {
    public FriendshipNotFoundException(String message) {
        super(message);
    }
}
