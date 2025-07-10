package loginflow;

import java.sql.SQLException;

public class LoginService {
    private final UsersRepository rep;

    public LoginService() {
        rep = new UsersRepository();
    }

    public boolean login(String username, String hashedPassword) throws SQLException {
        return rep.testLoginInfo(username, hashedPassword);
    }

    public boolean isAdmin(String username) throws SQLException {
        return rep.isAdmin(username);
    }
}
