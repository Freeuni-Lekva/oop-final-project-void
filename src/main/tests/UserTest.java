import user.User;
import repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resources.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private UserRepository userRepo;

    private User testUser;

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM users WHERE username LIKE 'testuser%' OR username LIKE 'adminuser%' OR username LIKE 'user1%' OR username LIKE 'user2%' OR username LIKE 'updateduser%' OR username LIKE 'user@%' OR username LIKE 'A%'");
            stmt.executeUpdate("DELETE FROM users WHERE user_id IN (999, 888, 777)");
        }

        this.userRepo = new UserRepository(DatabaseConnection.getDataSource());

        this.testUser = new User();
        this.testUser.setUsername("testuser");
        this.testUser.setPassword_hash("hashedpassword123");
        this.testUser.setSalt("salt123");
        this.testUser.setIs_admin(false);
        this.testUser.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Test
    void testCreateUserSuccessfully() throws SQLException {
        testUser.setUsername("testuser_create_" + System.currentTimeMillis());
        userRepo.create(testUser);

        assertNotNull(testUser.getUser_id());
        assertEquals(testUser.getUsername(), testUser.getUsername());
        assertEquals("hashedpassword123", testUser.getPassword_hash());
        assertEquals("salt123", testUser.getSalt());
        assertFalse(testUser.getIs_admin());
        assertNotNull(testUser.getCreated_at());

        User retrievedUser = userRepo.getById(testUser.getUser_id());
        assertNotNull(retrievedUser);
        assertEquals(testUser.getUser_id(), retrievedUser.getUser_id());
        assertEquals(testUser.getUsername(), retrievedUser.getUsername());
        assertEquals(testUser.getPassword_hash(), retrievedUser.getPassword_hash());
        assertEquals(testUser.getSalt(), retrievedUser.getSalt());
        assertEquals(testUser.getIs_admin(), retrievedUser.getIs_admin());
    }

    @Test
    void testCreateUserWithAdminPrivileges() throws SQLException {
        User adminUser = new User();
        adminUser.setUsername("adminuser_" + System.currentTimeMillis());
        adminUser.setPassword_hash("adminhash123");
        adminUser.setSalt("adminsalt123");
        adminUser.setIs_admin(true);
        adminUser.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));

        userRepo.create(adminUser);

        assertNotNull(adminUser.getUser_id());
        assertTrue(adminUser.getIs_admin());

        User retrievedUser = userRepo.getById(adminUser.getUser_id());
        assertNotNull(retrievedUser);
        assertTrue(retrievedUser.getIs_admin());
    }

    @Test
    void testGetUserByIdSuccessfully() throws SQLException {
        testUser.setUsername("testuser_get_" + System.currentTimeMillis());
        userRepo.create(testUser);
        int userId = testUser.getUser_id();

        User retrievedUser = userRepo.getById(userId);
        assertNotNull(retrievedUser);
        assertEquals(userId, retrievedUser.getUser_id());
        assertEquals(testUser.getUsername(), retrievedUser.getUsername());
        assertEquals("hashedpassword123", retrievedUser.getPassword_hash());
        assertEquals("salt123", retrievedUser.getSalt());
        assertFalse(retrievedUser.getIs_admin());
    }

    @Test
    void testGetUserByIdWithNonExistentId() throws SQLException {
        User retrievedUser = userRepo.getById(99999);
        assertNull(retrievedUser);
    }

    @Test
    void testGetAllUsers() throws SQLException {
        User user1 = new User();
        user1.setUsername("user1_" + System.currentTimeMillis());
        user1.setPassword_hash("hash1");
        user1.setSalt("salt1");
        user1.setIs_admin(false);
        user1.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));

        User user2 = new User();
        user2.setUsername("user2_" + System.currentTimeMillis());
        user2.setPassword_hash("hash2");
        user2.setSalt("salt2");
        user2.setIs_admin(true);
        user2.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));

        userRepo.create(user1);
        userRepo.create(user2);

        List<User> allUsers = userRepo.getAll();
        assertTrue(allUsers.size() >= 2);
        assertTrue(allUsers.stream().anyMatch(u -> user1.getUsername().equals(u.getUsername())));
        assertTrue(allUsers.stream().anyMatch(u -> user2.getUsername().equals(u.getUsername())));
        assertTrue(allUsers.stream().anyMatch(User::getIs_admin));
        assertTrue(allUsers.stream().anyMatch(u -> !u.getIs_admin()));
    }

    @Test
    void testUpdateUserSuccessfully() throws SQLException {
        testUser.setUsername("testuser_update_" + System.currentTimeMillis());
        userRepo.create(testUser);
        int userId = testUser.getUser_id();

        User updatedUser = new User();
        updatedUser.setUser_id(userId);
        updatedUser.setUsername("updateduser_" + System.currentTimeMillis());
        updatedUser.setPassword_hash("updatedhash123");
        updatedUser.setSalt("updatedsalt123");
        updatedUser.setIs_admin(true);
        updatedUser.setCreated_at(testUser.getCreated_at());

        userRepo.update(updatedUser);

        User retrievedUser = userRepo.getById(userId);
        assertNotNull(retrievedUser);
        assertEquals(updatedUser.getUsername(), retrievedUser.getUsername());
        assertEquals("updatedhash123", retrievedUser.getPassword_hash());
        assertEquals("updatedsalt123", retrievedUser.getSalt());
        assertTrue(retrievedUser.getIs_admin());
    }

    @Test
    void testUpdateUserWithNonExistentId() throws SQLException {
        User nonExistentUser = new User();
        nonExistentUser.setUser_id(99999);
        nonExistentUser.setUsername("nonexistent");
        nonExistentUser.setPassword_hash("hash");
        nonExistentUser.setSalt("salt");
        nonExistentUser.setIs_admin(false);
        nonExistentUser.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));

        userRepo.update(nonExistentUser);

        User retrievedUser = userRepo.getById(99999);
        assertNull(retrievedUser);
    }

    @Test
    void testDeleteUserByIdSuccessfully() throws SQLException {
        testUser.setUsername("testuser_delete_" + System.currentTimeMillis());
        userRepo.create(testUser);
        int userId = testUser.getUser_id();

        assertNotNull(userRepo.getById(userId));

        userRepo.deleteById(userId);

        assertNull(userRepo.getById(userId));
    }

    @Test
    void testGetUsernameByIdSuccessfully() throws SQLException {
        testUser.setUsername("testuser_username_" + System.currentTimeMillis());
        userRepo.create(testUser);
        int userId = testUser.getUser_id();

        String username = userRepo.getUsernameById((long) userId);
        assertNotNull(username);
        assertEquals(testUser.getUsername(), username);
    }

    @Test
    void testGetUsernameByIdWithNonExistentId() throws SQLException {
        String username = userRepo.getUsernameById(99999L);
        assertNull(username);
    }


    @Test
    void testUserWithLongUsername() throws SQLException {
        String uniqueSuffix = "_" + System.currentTimeMillis();
        String longUsername = "LongUsernameTest" + uniqueSuffix;
        User longUsernameUser = new User();
        longUsernameUser.setUsername(longUsername);
        longUsernameUser.setPassword_hash("hash123");
        longUsernameUser.setSalt("salt123");
        longUsernameUser.setIs_admin(false);
        longUsernameUser.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));

        userRepo.create(longUsernameUser);
        assertNotNull(longUsernameUser.getUser_id());

        User retrievedUser = userRepo.getById(longUsernameUser.getUser_id());
        assertNotNull(retrievedUser);
        assertEquals(longUsername, retrievedUser.getUsername());
    }

    @Test
    void testUserWithSpecialCharacters() throws SQLException {
        String uniqueSuffix = "_" + System.currentTimeMillis();
        String specialUsername = "user@#$%^&*()_+-=[]{}|;':\",./<>?" + uniqueSuffix;
        String specialPassword = "pass@#$%^&*()_+-=[]{}|;':\",./<>?" + uniqueSuffix;
        String specialSalt = "salt@#$%^&*()_+-=[]{}|;':\",./<>?" + uniqueSuffix;
        
        User specialCharUser = new User();
        specialCharUser.setUsername(specialUsername);
        specialCharUser.setPassword_hash(specialPassword);
        specialCharUser.setSalt(specialSalt);
        specialCharUser.setIs_admin(false);
        specialCharUser.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));

        userRepo.create(specialCharUser);
        assertNotNull(specialCharUser.getUser_id());

        User retrievedUser = userRepo.getById(specialCharUser.getUser_id());
        assertNotNull(retrievedUser);
        assertEquals(specialUsername, retrievedUser.getUsername());
        assertEquals(specialPassword, retrievedUser.getPassword_hash());
        assertEquals(specialSalt, retrievedUser.getSalt());
    }

} 