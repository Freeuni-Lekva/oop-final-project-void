package friendships;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendshipRepository extends AbstractRepository<Friendship> {
    public FriendshipRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    public List<Friendship> getUsersFriendRequests(Integer currentUserId) {
        List<Friendship> friends = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM friendships WHERE receiver_id = ? and status = \"pending\"";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, currentUserId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                friends.add(createFriendship(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return friends;
    }

    public List<Friendship> getUsersFriendships(Integer currentUserId) {
        List<Friendship> friends = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM friendships WHERE (requester_id = ? OR receiver_id = ?) and status = \"accepted\"";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, currentUserId);
            preparedStatement.setInt(2, currentUserId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                friends.add(createFriendship(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return friends;
    }


    @Override
    public List<Friendship> getAll() {
        List<Friendship> friends = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM friendships";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                friends.add(createFriendship(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return friends;
    }

    @Override
    public void create(Friendship friend) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO friendships (requester_id, receiver_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, friend.getRequester_id());
            preparedStatement.setLong(2, friend.getReceiver_id());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public Friendship findFriendshipByUsersId(long currentUserId, long receiverId) {
        Friendship friendship = null;

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM friendships WHERE (requester_id = ? AND receiver_id = ?) OR (requester_id = ? AND receiver_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, currentUserId);
            preparedStatement.setLong(2, receiverId);
            preparedStatement.setLong(3, receiverId);
            preparedStatement.setLong(4, currentUserId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                friendship = createFriendship(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendship;
    }


    @Override
    public void update(Friendship friend) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE friendships SET status = ?, requested_at = ? WHERE (requester_id = ? AND receiver_id = ?) OR (requester_id = ? AND receiver_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, friend.getStatus());
            preparedStatement.setTimestamp(2, friend.getRequested_at());
            preparedStatement.setLong(3, friend.getRequester_id());
            preparedStatement.setLong(4, friend.getReceiver_id());
            preparedStatement.setLong(5, friend.getReceiver_id());
            preparedStatement.setLong(6, friend.getRequester_id());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteFriendshipByUsersId(Integer currentUserId, Integer receiverId) {
        try (Connection connection = dataSource.getConnection()) {

            String sql = "DELETE FROM friendships WHERE (requester_id = ? AND receiver_id = ?) OR (requester_id = ? AND receiver_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, currentUserId);
            preparedStatement.setLong(2, receiverId);
            preparedStatement.setLong(3, receiverId);
            preparedStatement.setLong(4, currentUserId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Friendship createFriendship(ResultSet resultSet) throws SQLException {
        return Friendship.builder()
                .requester_id(resultSet.getInt("requester_id"))
                .receiver_id(resultSet.getInt("receiver_id"))
                .status(resultSet.getString("status"))
                .requested_at(resultSet.getTimestamp("requested_at")).build();
    }


    @Override
    public Friendship getById(long id) {
        throw new UnsupportedOperationException("Not supported, we have 2 primary keys");
    }


    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException("Not supported, we have 2 primary keys");
    }
}
