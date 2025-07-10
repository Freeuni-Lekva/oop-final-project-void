package temporary;

import entities.Users;
import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersRepository extends AbstractRepository<Users> {
    public UsersRepository(BasicDataSource dataSource) {
        super(dataSource);
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
