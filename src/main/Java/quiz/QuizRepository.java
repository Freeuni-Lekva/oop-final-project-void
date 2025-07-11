package quiz;

import org.apache.commons.dbcp2.BasicDataSource;
import question.Question;
import repository.AbstractRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizRepository extends AbstractRepository<Quiz> {
    public QuizRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Quiz getById(long id) {
        String sql = "SELECT * FROM quizzes WHERE quiz_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Quiz(
                        rs.getInt("quiz_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("creator_id"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Quiz> getAll() {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM quizzes";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                quizzes.add(new Quiz(
                    rs.getInt("quiz_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("creator_id"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    @Override
    public void create(Quiz quiz) {
        String sql = "INSERT INTO quizzes (title, description, creator_id, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, quiz.getTitle());
            stmt.setString(2, quiz.getDescription());
            stmt.setInt(3, quiz.getCreatorId());
            stmt.setTimestamp(4, quiz.getCreatedAt());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    quiz.setQuizId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Quiz quiz) {
        String sql = "UPDATE quizzes SET title=?, description=?, creator_id=? WHERE quiz_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, quiz.getTitle());
            stmt.setString(2, quiz.getDescription());
            stmt.setInt(3, quiz.getCreatorId());
            stmt.setInt(4, quiz.getQuizId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM quizzes WHERE quiz_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getQuestionsByQuizId(int quizId) throws SQLException {
        String sql = "SELECT * FROM questions WHERE quiz_id = ? ORDER BY question_order";
        List<Question> questions = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Question q = new Question();
                    q.setQuestionId(rs.getInt("question_id"));
                    q.setQuizId(rs.getInt("quiz_id"));
                    q.setQuestionText(rs.getString("question_text"));
                    q.setType(rs.getString("type"));
                    q.setImageUrl(rs.getString("image_url"));
                    q.setQuestionOrder(rs.getInt("question_order"));
                    questions.add(q);
                }
            }
        }
        return questions;
    }

    public List<Quiz> getQuizzesByCreatorId(long userId) {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM quizzes WHERE creator_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    quizzes.add(makeNewQuiz(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }
    private Quiz makeNewQuiz(ResultSet rs) throws SQLException {
        return new Quiz(
                rs.getInt("quiz_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("creator_id"),
                rs.getTimestamp("created_at"));
    }

    public int getTakenQuizCount(String username) throws SQLException {
        String query = "SELECT COUNT(*) AS taken_count FROM quiz_attempts WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("taken_count");
            }
        }
        return 0;
    }
} 