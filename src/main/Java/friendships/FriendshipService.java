package friendships;

import friendships.exceptions.FriendRequestNotAllowedException;
import friendships.exceptions.FriendRequestNotFoundException;
import friendships.exceptions.FriendshipRequestAlreadyExistsException;
import friendships.exceptions.UserNotFoundException;
import loginflow.Users;
import loginflow.UsersService;
import org.apache.commons.lang3.tuple.Pair;
import temporary.DatabaseConnection;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FriendshipService {
    private final FriendshipRepository friendsRepository = new FriendshipRepository(DatabaseConnection.getDataSource());
    private final UsersService usersService = new UsersService();

    public List<FriendshipDto> getAcceptedFriends(Integer currentUserId) {
        List<Friendship> usersFriends = friendsRepository.getUsersFriendships(currentUserId);

        List<Pair<Integer, Timestamp>> friendsPrimaryKeyList = usersFriends.stream()
                .map(friend -> {
                    if (friend.getRequester_id().equals(currentUserId)) {
                        return Pair.of(friend.getReceiver_id(), friend.getRequested_at());
                    }
                    return Pair.of(friend.getRequester_id(), friend.getRequested_at());
                }).collect(Collectors.toList());

        List<FriendshipDto> friendsDto = getFriendshipDtosFromUserIds(friendsPrimaryKeyList);
        return friendsDto;
    }

    public void unfriendUser(Users currentUser, Users friend) {
        Friendship currentFriendship = friendsRepository.findFriendshipByUsersId(currentUser.getUser_id(), friend.getUser_id());
        if (currentFriendship == null || Objects.equals(currentFriendship.getStatus(), "pending")) {
            throw new FriendRequestNotFoundException("Users are not Friends");
        }

        friendsRepository.deleteFriendshipByUsersId(currentUser.getUser_id(), friend.getUser_id());
    }

    public void rejectFriendRequest(Users currentUser, Users friend) throws FriendRequestNotFoundException {
        Friendship currentFriendship = friendsRepository.findFriendshipByUsersId(currentUser.getUser_id(), friend.getUser_id());
        if (currentFriendship == null || !Objects.equals(currentFriendship.getStatus(), "pending")) {
            throw new FriendRequestNotFoundException("No pending Friend Request was found");
        }

        friendsRepository.deleteFriendshipByUsersId(currentUser.getUser_id(), friend.getUser_id());
    }

    public void acceptFriendRequest(Users currentUser, Users friend) throws FriendRequestNotFoundException {
        Friendship currentFriendship = friendsRepository.findFriendshipByUsersId(currentUser.getUser_id(), friend.getUser_id());
        if (currentFriendship == null || !Objects.equals(currentFriendship.getStatus(), "pending")) {
            throw new FriendRequestNotFoundException("No Friend Request was found");
        }

        currentFriendship.setStatus("accepted");
        friendsRepository.update(currentFriendship);
    }

    public void sendFriendRequest(Users currentUser, Users friend) throws FriendshipRequestAlreadyExistsException {
        if (currentUser.getUser_id().equals(friend.getUser_id())) {
            throw new FriendRequestNotAllowedException("You can't send friend requests to yourself");
        }

        Friendship currentFriendship = friendsRepository.findFriendshipByUsersId(currentUser.getUser_id(), friend.getUser_id());
        if (currentFriendship != null && currentFriendship.getRequester_id().equals(currentUser.getUser_id())) {
            throw new FriendshipRequestAlreadyExistsException("Friendship Request already exists");
        } else if (currentFriendship != null) {
            acceptFriendRequest(currentUser, friend);
            return;
        }

        currentFriendship = Friendship.builder()
                .requester_id(currentUser.getUser_id())
                .receiver_id(friend.getUser_id()).build();

        friendsRepository.create(currentFriendship);
    }


    public List<FriendshipDto> getUserFriendRequests(Integer currentUserId) {
        if (usersService.findById(currentUserId) == null) throw new UserNotFoundException("Not valid user credentials");

        List<Friendship> usersFriendRequests = friendsRepository.getUsersFriendRequests(currentUserId);

        List<Pair<Integer, Timestamp>> friendsPrimaryKeyList = usersFriendRequests.stream()
                .map(friend -> {
                    return Pair.of(friend.getRequester_id(), friend.getRequested_at());
                }).collect(Collectors.toList());

        List<FriendshipDto> friendsDto = getFriendshipDtosFromUserIds(friendsPrimaryKeyList);
        return friendsDto;
    }

    public FriendshipDto getUserFriendRequest(Integer currentUser, Integer friend) {
        Friendship current = friendsRepository.findFriendshipByUsersId(currentUser, friend);

        if (current == null) {
            return null;
        }
        return FriendshipDto.builder()
                .friend_requested_at(current.getRequested_at().toString())
                .status(current.getStatus())
                .build();
    }


    private List<FriendshipDto> getFriendshipDtosFromUserIds(List<Pair<Integer, Timestamp>> friendsPrimaryKeyList) {
        return friendsPrimaryKeyList.stream()
                .map(pair -> FriendshipDto.fromUser(usersService.findById(pair.getLeft()), pair.getRight()))
                .collect(Collectors.toList());
    }
}
