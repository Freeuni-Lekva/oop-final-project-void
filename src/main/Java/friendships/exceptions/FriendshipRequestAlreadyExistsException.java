package friendships.exceptions;

public class FriendshipRequestAlreadyExistsException extends RuntimeException {
    public FriendshipRequestAlreadyExistsException(String message) {
        super(message);
    }
}
