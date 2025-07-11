package messages;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository extends AbstractRepository<Message> {

    public MessageRepository(BasicDataSource dataSource) {
        super(dataSource);
    }


    @Override
    public Message getById(long id) {
        return null;
    }

    @Override
    public List<Message> getAll() {
        return List.of();
    }

    public List<Message> getAllByReceiverId(int receiverId) {
        List<Message> messages = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT message_id, sender_id, receiver_id, type, content, quiz_id, sent_at FROM messages WHERE receiver_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, receiverId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Message message = new Message();
                message.setMessage_id(resultSet.getInt("message_id"));
                message.setSender_id(resultSet.getInt("sender_id"));
                message.setReceiver_id(resultSet.getInt("receiver_id"));
                message.setType(resultSet.getString("type"));
                message.setContent(resultSet.getString("content"));

                Object quizIdObj = resultSet.getObject("quiz_id");
                if (quizIdObj != null) {
                    message.setQuiz_id(((Number) quizIdObj).intValue());
                } else {
                    message.setQuiz_id(null);
                }

                message.setSent_at(resultSet.getTimestamp("sent_at"));

                messages.add(message);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return messages;
    }



    @Override
    public void create(Message message) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO messages (sender_id, receiver_id, type, content, quiz_id) " +
                         "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message.getSender_id());
            preparedStatement.setInt(2, message.getReceiver_id());
            preparedStatement.setString(3, message.getType());
            preparedStatement.setString(4, message.getContent());

            if (message.getQuiz_id() != null) {
                preparedStatement.setInt(5, message.getQuiz_id());
            } else {
                preparedStatement.setNull(5, java.sql.Types.INTEGER);
            }


            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Message message) {

    }

    @Override
    public void deleteById(long id) {

    }
}
