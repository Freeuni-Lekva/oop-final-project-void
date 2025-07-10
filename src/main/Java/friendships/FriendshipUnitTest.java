//package friendships;
//
//import loginflow.Users;
//import friendships.exceptions.FriendRequestNotFoundException;
//import friendships.exceptions.FriendshipRequestAlreadyExistsException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import temporary.UsersService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class FriendshipUnitTest {
//
//    private FriendshipRepository friendshipRepository;
//    private UsersService usersService;
//    private FriendshipService friendshipService;
//
//    @BeforeEach
//    public void setUp() {
//
//    }
//
//    @Test
//    public void testSendFriendRequest_success() {
//        Users user1 = new Users();
//        user1.setUser_id(1);
//
//        Users user2 = new Users();
//        user2.setUser_id(2);
//
//        when(friendshipRepository.findFriendshipByUsersId(1, 2)).thenReturn(null);
//
//        assertDoesNotThrow(() -> {
//            friendshipService.sendFriendRequest(user1, user2);
//        });
//
//        verify(friendshipRepository, times(1)).create(any(Friendship.class));
//    }
//
//    @Test
//    public void testSendFriendRequest_alreadyExists() {
//        Users user1 = new Users();
//        user1.setUser_id(1);
//
//        Users user2 = new Users();
//        user2.setUser_id(2);
//
//        when(friendshipRepository.findFriendshipByUsersId(1, 2)).thenReturn(Friendship.builder().build());
//
//        assertThrows(FriendshipRequestAlreadyExistsException.class, () -> {
//            friendshipService.sendFriendRequest(user1, user2);
//        });
//
//        verify(friendshipRepository, never()).create(any());
//    }
//
//    @Test
//    public void testUnfriendUser_notFriends() {
//        Users user1 = new Users();
//        user1.setUser_id(1);
//
//        Users user2 = new Users();
//        user2.setUser_id(2);
//
//        when(friendshipRepository.findFriendshipByUsersId(1, 2)).thenReturn(
//                Friendship.builder().status("pending").build()
//        );
//
//        assertThrows(FriendRequestNotFoundException.class, () -> {
//            friendshipService.unfriendUser(user1, user2);
//        });
//    }
//
//    @Test
//    public void testUnfriendUser_success() {
//        Users user1 = new Users();
//        user1.setUser_id(1);
//
//        Users user2 = new Users();
//        user2.setUser_id(2);
//
//        when(friendshipRepository.findFriendshipByUsersId(1, 2)).thenReturn(
//                Friendship.builder().status("accepted").build()
//        );
//
//        assertDoesNotThrow(() -> {
//            friendshipService.unfriendUser(user1, user2);
//        });
//
//        verify(friendshipRepository).deleteFriendshipByUsersId(1, 2);
//    }
//
//    @Test
//    public void testGetAcceptedFriends_returnsList() {
//        when(friendshipRepository.getUsersFriendships(1)).thenReturn(new ArrayList<>());
//
//        List<FriendshipDto> result = friendshipService.getAcceptedFriends(1);
//
//        assertNotNull(result);
//        assertEquals(0, result.size());
//    }
//
//    // Add more tests for reject and accept logic if needed
//}
