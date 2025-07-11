package friendships.exceptions;

public class FriendRequestNotAllowedException extends RuntimeException {
  public FriendRequestNotAllowedException(String message) {
    super(message);
  }
}
