package loginflow;

import temporary.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService() {
        this.usersRepository = new UsersRepository(DatabaseConnection.getDataSource());
    }

    public Users findById(int userId) {
        return usersRepository.findById(userId);
    }

    public Users findByName(String username) {
        return usersRepository.findByName(username);
    }

    public List<Users> lookUpPeople(String keyword) {
        return usersRepository.lookup(keyword);
    }

    public boolean login(String username, String hashedPassword) throws SQLException {
        return usersRepository.testLoginInfo(username, hashedPassword);
    }

    public boolean isAdmin(String username) throws SQLException {
        return usersRepository.isAdmin(username);
    }

    public boolean exists(String username) throws SQLException {
        return usersRepository.userExists(username);
    }

    public void register(String username, String password) throws SQLException {
        usersRepository.add(username, password);
    }

}
