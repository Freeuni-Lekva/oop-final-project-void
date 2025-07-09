package servlets;

import java.sql.SQLException;

public class RegisterService {
    private final Repository rep;

    public RegisterService() throws SQLException {
        rep = new Repository();
    }

    public boolean exists(String username) throws SQLException {
        return rep.userExists(username);
    }

    public void register(String username, String password) throws SQLException {
        rep.add(username, password);
    }
}
