package loginflow;

import resources.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersRepository {
    private static final String login = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
    private static final String userExists = "SELECT COUNT(*) FROM users WHERE username = ?";
    private static final String addUser = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
    private static final String isAdmin = "SELECT is_admin FROM users WHERE username = ?";

    public boolean testLoginInfo(String username, String hashedPassword) throws SQLException {
        Connection connection = DatabaseConnection.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(login);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, hashedPassword);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public boolean userExists(String username) throws SQLException {
        Connection conn = DatabaseConnection.getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(userExists);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        return rs.next() && rs.getInt(1) != 0;
    }

    public void add(String username, String hashedPassword) throws SQLException {
        Connection connection = DatabaseConnection.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(addUser);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, hashedPassword);
        preparedStatement.executeUpdate();
    }

    public boolean isAdmin(String username) throws SQLException {
        Connection conn = DatabaseConnection.getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(isAdmin);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        return rs.next() && rs.getBoolean("is_admin");
    }
}
