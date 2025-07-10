package loginflow;

import java.sql.SQLException;

public class RegisterService {
    private final UsersRepository rep;

    public RegisterService() throws SQLException {
        rep = new UsersRepository();
    }

    public boolean exists(String username) throws SQLException {
        return rep.userExists(username);
    }

    public void register(String username, String password) throws SQLException {
        rep.add(username, password);
    }
}
