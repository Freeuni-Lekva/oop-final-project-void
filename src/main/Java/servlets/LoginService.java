package servlets;

import java.sql.SQLException;

public class LoginService {
    private final Repository rep;

    public LoginService() {
        rep = new Repository();
    }

    public boolean login(String username, String hashedPassword) throws SQLException {
        return rep.testLoginInfo(username, hashedPassword);
    }

    public boolean isAdmin(String username) throws SQLException {
        return rep.isAdmin(username);
    }
}
