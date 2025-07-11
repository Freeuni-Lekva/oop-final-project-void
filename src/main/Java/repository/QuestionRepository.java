package repository;

import entities.Question;
import org.apache.commons.dbcp2.BasicDataSource;
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
                        rs.getString("question_text"),
                        rs.getInt("quiz_id"),
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
                    rs.getString("question_text"),
                    rs.getInt("quiz_id"),
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

            stmt.setInt(1, question.getQuiz_id());
            stmt.setString(2, question.getQuestion_text());
            stmt.setString(3, question.getType());
            String imageUrl = question.getImage_url();
            stmt.setString(4, (imageUrl != null && !imageUrl.isEmpty()) ? imageUrl : null);
            stmt.setInt(5, question.getQuestion_order());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    question.setQuestion_id(rs.getInt(1));
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
            stmt.setInt(1, question.getQuiz_id());
            stmt.setString(2, question.getQuestion_text());
            stmt.setString(3, question.getType());
            stmt.setString(4, question.getImage_url());
            stmt.setInt(5, question.getQuestion_order());
            stmt.setInt(6, question.getQuestion_id());
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
} 