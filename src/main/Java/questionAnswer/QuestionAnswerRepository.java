package questionAnswer;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                            rs.getString("answer_text")
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
                        rs.getString("answer_text")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

    @Override
    public void create(QuestionAnswer answer) {
        String sql = "INSERT INTO question_answers (question_id, answer_text) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, answer.getQuestionId());
            stmt.setString(2, answer.getAnswerText());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    answer.setAnswerId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(QuestionAnswer answer) {
        String sql = "UPDATE question_answers SET question_id=?, answer_text=? WHERE answer_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, answer.getQuestionId());
            stmt.setString(2, answer.getAnswerText());
            stmt.setInt(3, answer.getAnswerId());
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

    public void deleteByQuestionId(int questionId) {
        String sql = "DELETE FROM question_answers WHERE question_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<QuestionAnswer> getCorrectAnswersByQuestionIds(List<Integer> questionIds) {
        if (questionIds == null || questionIds.isEmpty()) {
            return List.of();
        }

        String placeholders = questionIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        String sql = "SELECT * FROM question_answers WHERE question_id IN (" + placeholders + ")";

        List<QuestionAnswer> answers = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < questionIds.size(); i++) {
                stmt.setInt(i + 1, questionIds.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                QuestionAnswer qa = QuestionAnswer.builder()
                        .answerId(rs.getInt("answer_id"))
                        .questionId(rs.getInt("question_id"))
                        .answerText(rs.getString("answer_text"))
                        .build();

                answers.add(qa);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching correct answers", e);
        }
        return answers;
    }

    public QuestionAnswer findByQuestionId(int questionId) throws SQLException {
        String sql = "SELECT * FROM question_answers WHERE question_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    QuestionAnswer answer = new QuestionAnswer();
                    answer.setAnswerId(rs.getInt("answer_id"));
                    answer.setQuestionId(rs.getInt("question_id"));
                    answer.setAnswerText(rs.getString("answer_text"));
                    return answer;
                }
            }
        }
        return null;
    }
}
