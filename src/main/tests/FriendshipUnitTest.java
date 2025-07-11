import friendships.FriendshipDto;
import friendships.FriendshipService;
import friendships.exceptions.FriendRequestNotAllowedException;
import friendships.exceptions.FriendRequestNotFoundException;
import friendships.exceptions.FriendshipRequestAlreadyExistsException;
import loginflow.Users;
import loginflow.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import temporary.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipServiceTest {

    private FriendshipService friendshipService;
    private UsersService usersService;

    private Users irakli;
    private Users giga;
    private Users goga;

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM friendships");
            stmt.executeUpdate("DELETE FROM users WHERE username IN ('testIrakli', 'testGiga', 'testGoga')");
        }
        this.usersService = new UsersService();
        this.friendshipService = new FriendshipService();

        try {
            usersService.register("testIrakli", "password");
            usersService.register("testGiga", "password");
            usersService.register("testGoga", "password");
        } catch (SQLException ignored) {
            System.out.println("Users Already Exist");
        }

        this.irakli = usersService.findByName("testIrakli");
        this.giga = usersService.findByName("testGiga");
        this.goga = usersService.findByName("testGoga");
    }

    @Test
    void testSendFriendRequestSuccessfully() {
        friendshipService.sendFriendRequest(irakli, giga);

        FriendshipDto dto = friendshipService.getUserFriendRequest(irakli.getUser_id(), giga.getUser_id());
        assertNotNull(dto);
        assertEquals("pending", dto.getStatus());
    }

    @Test
    void testSendFriendRequestToSelfException() {
        assertThrows(FriendRequestNotAllowedException.class,
                () -> friendshipService.sendFriendRequest(irakli, irakli));
    }
    @Test
    void testUnfriendWhenNotFriendsException() {
        assertThrows(FriendRequestNotFoundException.class,
                () -> friendshipService.unfriendUser(irakli, giga));
    }

    @Test
    void testDuplicateFriendRequestException() {
        friendshipService.sendFriendRequest(irakli, giga);
        assertThrows(FriendshipRequestAlreadyExistsException.class,
                () -> friendshipService.sendFriendRequest(irakli, giga));
    }

    @Test
    void testAcceptFriendRequestSuccessfully() {
        friendshipService.sendFriendRequest(irakli, giga);
        friendshipService.acceptFriendRequest(giga, irakli);

        List<FriendshipDto> friends = friendshipService.getAcceptedFriends(irakli.getUser_id());
        assertEquals(1, friends.size());
        assertEquals("testGiga", friends.get(0).getFriend_name());
    }

    @Test
    void testAcceptNonExistentFriendRequestException() {
        assertThrows(FriendRequestNotFoundException.class,
                () -> friendshipService.acceptFriendRequest(irakli, giga));
    }

    @Test
    void testRejectFriendRequestSuccessfully() {
        friendshipService.sendFriendRequest(giga, irakli);
        friendshipService.rejectFriendRequest(irakli, giga);

        assertNull(friendshipService.getUserFriendRequest(giga.getUser_id(), irakli.getUser_id()));
    }

    @Test
    void testRejectNonExistentFriendRequestException() {
        assertThrows(FriendRequestNotFoundException.class,
                () -> friendshipService.rejectFriendRequest(irakli, giga));
    }

    @Test
    void testUnfriendUserSuccessfully() {
        friendshipService.sendFriendRequest(irakli, giga);
        friendshipService.acceptFriendRequest(giga, irakli);

        friendshipService.unfriendUser(irakli, giga);

        List<FriendshipDto> friends = friendshipService.getAcceptedFriends(irakli.getUser_id());
        assertTrue(friends.isEmpty());
    }



    @Test
    void testGetUserFriendRequests() {
        friendshipService.sendFriendRequest(giga, irakli);
        friendshipService.sendFriendRequest(goga, irakli);

        List<FriendshipDto> requests = friendshipService.getUserFriendRequests(irakli.getUser_id());
        assertEquals(2, requests.size());
    }

    @Test
    void testGetAcceptedFriends() {
        friendshipService.sendFriendRequest(irakli, giga);
        friendshipService.acceptFriendRequest(giga, irakli);

        friendshipService.sendFriendRequest(irakli, goga);
        friendshipService.acceptFriendRequest(goga, irakli);

        List<FriendshipDto> friends = friendshipService.getAcceptedFriends(irakli.getUser_id());
        assertEquals(2, friends.size());
    }

    @Test
    void testGetUserFriendRequestReturnsNullWhenNotExists() {
        FriendshipDto dto = friendshipService.getUserFriendRequest(irakli.getUser_id(), giga.getUser_id());
        assertNull(dto);
    }
}
