package temporary;

import entities.Users;
import temporary.DatabaseConnection;

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
}
