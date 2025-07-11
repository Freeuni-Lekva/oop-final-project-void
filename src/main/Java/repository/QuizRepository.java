package repository;

import entities.Question;
import entities.Quiz;
import org.apache.commons.dbcp2.BasicDataSource;
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
                        rs.getBoolean("randomize"),
                        rs.getBoolean("is_one_page"),
                        rs.getBoolean("immediate_correction"),
                        rs.getBoolean("practice_mode"),
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
                    rs.getBoolean("randomize"),
                    rs.getBoolean("is_one_page"),
                    rs.getBoolean("immediate_correction"),
                    rs.getBoolean("practice_mode"),
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
        String sql = "INSERT INTO quizzes (title, description, creator_id, randomize, is_one_page, immediate_correction, practice_mode, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, quiz.getTitle());
            stmt.setString(2, quiz.getDescription());
            stmt.setInt(3, quiz.getCreator_id());
            stmt.setBoolean(4, quiz.getRandomize() != null ? quiz.getRandomize() : false);
            stmt.setBoolean(5, quiz.getIs_one_page() != null ? quiz.getIs_one_page() : true);
            stmt.setBoolean(6, quiz.getImmediate_correction() != null ? quiz.getImmediate_correction() : false);
            stmt.setBoolean(7, quiz.getPractice_mode() != null ? quiz.getPractice_mode() : false);
            stmt.setTimestamp(8, quiz.getCreated_at());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    quiz.setQuiz_id(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Quiz quiz) {
        String sql = "UPDATE quizzes SET title=?, description=?, creator_id=?, randomize=?, is_one_page=?, immediate_correction=?, practice_mode=? WHERE quiz_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, quiz.getTitle());
            stmt.setString(2, quiz.getDescription());
            stmt.setInt(3, quiz.getCreator_id());
            stmt.setBoolean(4, quiz.getRandomize() != null ? quiz.getRandomize() : false);
            stmt.setBoolean(5, quiz.getIs_one_page() != null ? quiz.getIs_one_page() : true);
            stmt.setBoolean(6, quiz.getImmediate_correction() != null ? quiz.getImmediate_correction() : false);
            stmt.setBoolean(7, quiz.getPractice_mode() != null ? quiz.getPractice_mode() : false);
            stmt.setInt(8, quiz.getQuiz_id());
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
                    q.setQuestion_id(rs.getInt("question_id"));
                    q.setQuiz_id(rs.getInt("quiz_id"));
                    q.setQuestion_text(rs.getString("question_text"));
                    q.setType(rs.getString("type"));
                    q.setImage_url(rs.getString("image_url")); // nullable
                    q.setQuestion_order(rs.getInt("question_order"));
                    questions.add(q);
                }
            }
        }

        return questions;
    }
} 