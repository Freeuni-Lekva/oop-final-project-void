package loginflow;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;
import temporary.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersRepository extends AbstractRepository<Users> {
    private static final String login = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
    private static final String userExists = "SELECT COUNT(*) FROM users WHERE username = ?";
    private static final String addUser = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
    private static final String isAdmin = "SELECT is_admin FROM users WHERE username = ?";

    public UsersRepository(BasicDataSource dataSource) {
        super(dataSource);
    }


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
        try (Connection connection = DatabaseConnection.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(addUser);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.executeUpdate();
        }
    }

    public boolean isAdmin(String username) throws SQLException {
        Connection conn = DatabaseConnection.getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(isAdmin);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        return rs.next() && rs.getBoolean("is_admin");
    }

    public Users findById(int userId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Users findByName(String username) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Users> lookup(String keyword) {
        List<Users> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM users WHERE username LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    private Users mapUser(ResultSet rs) throws SQLException {
        return Users.builder()
                .user_id(rs.getInt("user_id"))
                .username(rs.getString("username"))
                .password_hash(rs.getString("password_hash"))
                .salt(rs.getString("salt"))
                .is_admin(rs.getBoolean("is_admin"))
                .created_at(rs.getTimestamp("created_at"))
                .build();
    }

    @Override
    public List<Users> getAll() {
        throw new UnsupportedOperationException("Not needed.");
    }

    @Override
    public void create(Users entity) {
        throw new UnsupportedOperationException("Use registration logic.");
    }

    @Override
    public void update(Users entity) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Users getById(long id) {
        return findById((int) id);
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
