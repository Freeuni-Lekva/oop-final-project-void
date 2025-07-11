package question;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository extends AbstractRepository<Question> {
    public QuestionRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Question getById(long id) {
        String sql = "SELECT * FROM questions WHERE question_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Question(
                            rs.getInt("question_id"),
                            rs.getInt("quiz_id"),
                            rs.getString("question_text"),
                            rs.getString("type"),
                            rs.getString("image_url"),
                            rs.getInt("question_order")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Question> getAll() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                questions.add(new Question(
                        rs.getInt("question_id"),
                        rs.getInt("quiz_id"),
                        rs.getString("question_text"),
                        rs.getString("type"),
                        rs.getString("image_url"),
                        rs.getInt("question_order")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    @Override
    public void create(Question question) {
        String sql = "INSERT INTO questions (quiz_id, question_text, type, image_url, question_order) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, question.getQuizId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, question.getType());
            String imageUrl = question.getImageUrl();
            stmt.setString(4, (imageUrl != null && !imageUrl.isEmpty()) ? imageUrl : null);
            stmt.setInt(5, question.getQuestionOrder());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    question.setQuestionId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void update(Question question) {
        String sql = "UPDATE questions SET quiz_id=?, question_text=?, type=?, image_url=?, question_order=? WHERE question_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, question.getQuizId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, question.getType());
            stmt.setString(4, question.getImageUrl());
            stmt.setInt(5, question.getQuestionOrder());
            stmt.setInt(6, question.getQuestionId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM questions WHERE question_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getAllByQuizId(int id) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from questions where quiz_id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<Question> questions = new ArrayList<>();
            while (resultSet.next()) {
                Question question = Question.builder()
                        .questionId(resultSet.getInt("question_id"))
                        .questionOrder(resultSet.getInt("question_order"))
                        .questionText(resultSet.getString("question_text"))
                        .imageUrl(resultSet.getString("image_url"))
                        .quizId(resultSet.getInt("quiz_id"))
                        .type(resultSet.getString("type"))
                        .build();

                questions.add(question);
            }
            return questions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
