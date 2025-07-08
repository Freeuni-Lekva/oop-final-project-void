package friends;

import entities.Users;
import friends.exceptions.AlreadyFriendsException;
import friends.exceptions.FriendNotFoundException;
import friends.exceptions.FriendRequestNotFoundException;

import java.util.List;

public class FriendsService {

    public List<FriendsDto> getAllFriends(Integer currentUserId) {
        return null;
    }

    public void unfriendUser(Users currentUser, Users friend)throws FriendNotFoundException {

    }

    public void rejectFriendRequest(Users currentUser, Users friend)throws FriendRequestNotFoundException {

    }

    public void acceptFriendRequest(Users currentUser, Users friend)throws FriendRequestNotFoundException {

    }

    public void sendFriendRequest(Users currentUser, Users friend)throws AlreadyFriendsException {

    }
}
