package repository;

import entities.QuestionAnswer;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionAnswerRepository extends AbstractRepository<QuestionAnswer> {
    public QuestionAnswerRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public QuestionAnswer getById(long id) {
        String sql = "SELECT * FROM question_answers WHERE answer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new QuestionAnswer(
                        rs.getInt("answer_id"),
                        rs.getInt("question_id"),
                        rs.getString("answer_text"),
                        rs.getInt("answer_order")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<QuestionAnswer> getAll() {
        List<QuestionAnswer> answers = new ArrayList<>();
        String sql = "SELECT * FROM question_answers";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                answers.add(new QuestionAnswer(
                    rs.getInt("answer_id"),
                    rs.getInt("question_id"),
                    rs.getString("answer_text"),
                    rs.getInt("answer_order")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

    @Override
    public void create(QuestionAnswer answer) {
        String sql = "INSERT INTO question_answers (question_id, answer_text, answer_order) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, answer.getQuestion_id());
            stmt.setString(2, answer.getAnswer_text());
            stmt.setInt(3, answer.getAnswer_order() != null ? answer.getAnswer_order() : 1);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    answer.setAnswer_id(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(QuestionAnswer answer) {
        String sql = "UPDATE question_answers SET question_id=?, answer_text=?, answer_order=? WHERE answer_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, answer.getQuestion_id());
            stmt.setString(2, answer.getAnswer_text());
            stmt.setInt(3, answer.getAnswer_order() != null ? answer.getAnswer_order() : 1);
            stmt.setInt(4, answer.getAnswer_id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM question_answers WHERE answer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public QuestionAnswer findByQuestionId(int questionId) throws SQLException {
        String sql = "SELECT * FROM question_answers WHERE question_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    QuestionAnswer answer = new QuestionAnswer();
                    answer.setAnswer_id(rs.getInt("answer_id"));
                    answer.setQuestion_id(rs.getInt("question_id"));
                    answer.setAnswer_text(rs.getString("answer_text"));
                    return answer;
                }
            }
        }
        return null;
    }
} 