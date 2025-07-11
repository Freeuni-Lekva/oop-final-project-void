package quizAttempt;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizAttemptRepository extends AbstractRepository<QuizAttempt> {
    @Override
    public QuizAttempt getById(long id) {
        return null;
    }

    @Override
    public List<QuizAttempt> getAll() {
        return List.of();
    }

    @Override
    public void create(QuizAttempt quizAttempt) {
        String sql = "INSERT INTO quiz_attempts (quiz_id, user_id, score, total_questions, time_taken, attempted_at) " +
                "VALUES (?, ?, ?, ?, ?,  ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, quizAttempt.getQuizId());
            stmt.setLong(2, quizAttempt.getUserId());
            stmt.setFloat(3, quizAttempt.getScore());
            stmt.setInt(4, quizAttempt.getTotalQuestions());
            stmt.setInt(5, quizAttempt.getTimeTaken());
            stmt.setTimestamp(6, quizAttempt.getAttemptedAt());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating quiz attempt failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    quizAttempt.setAttemptId((int) generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating quiz attempt failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting quiz attempt", e);
        }
    }


    @Override
    public void update(QuizAttempt quizAttempt) {

    }

    @Override
    public void deleteById(long id) {

    }

    public QuizAttemptRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    public List<QuizAttempt> getAttemptsByUserAndQuiz(Long userId, long quizId, String sort) {
        List<QuizAttempt> attempts = new ArrayList<>();
        String query = "SELECT * FROM quiz_attempts WHERE user_id = ? AND quiz_id = ? ORDER BY ";

        if ("score".equalsIgnoreCase(sort)) {
            query += "score DESC";
        } else {
            query += "attempted_at DESC";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    attempts.add(makeQuizAttempt(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attempts;
    }

    public List<QuizAttempt> getTopPerformers(long quizId, int limit, Timestamp since) {
        List<QuizAttempt> attempts = new ArrayList<>();
        String query = "SELECT * FROM quiz_attempts WHERE quiz_id = ? "
                + (since != null ? "AND attempted_at >= ? " : "")
                + "ORDER BY score DESC LIMIT ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, quizId);
            int paramIndex = 2;

            if (since != null) {
                stmt.setTimestamp(paramIndex++, since);
            }
            stmt.setInt(paramIndex, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    attempts.add(makeQuizAttempt(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attempts;
    }

    public List<QuizAttempt> getRecentAttempts(long quizId, int limit) {
        List<QuizAttempt> attempts = new ArrayList<>();
        String query = "SELECT * FROM quiz_attempts WHERE quiz_id = ? ORDER BY attempted_at DESC LIMIT ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, quizId);
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    attempts.add(makeQuizAttempt(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attempts;
    }

    public List<QuizAttempt> getAllAttemptsByUser(Long userId) {
        List<QuizAttempt> attempts = new ArrayList<>();
        String query = "SELECT * FROM quiz_attempts WHERE user_id = ? ORDER BY attempted_at DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    attempts.add(makeQuizAttempt(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attempts;
    }

    public Map<String, Object> getQuizStats(long quizId) {
        Map<String, Object> stats = new HashMap<>();
        String query = "SELECT COUNT(*) AS total_attempts, AVG(score) AS average_score, MAX(score) AS highest_score " +
                "FROM quiz_attempts WHERE quiz_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("total_attempts", rs.getInt("total_attempts"));
                    stats.put("average_score", rs.getDouble("average_score"));
                    stats.put("highest_score", rs.getDouble("highest_score"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    private QuizAttempt makeQuizAttempt(ResultSet rs) throws SQLException {
        QuizAttempt attempt = new QuizAttempt(-1,-1,-1,-1,-1,-1,null);
        attempt.setAttemptId((int) rs.getLong("attempt_id"));
        attempt.setQuizId((int) rs.getLong("quiz_id"));
        attempt.setUserId((int) rs.getLong("user_id"));
        attempt.setScore((int) rs.getFloat("score"));
        attempt.setTotalQuestions(rs.getInt("total_questions"));
        attempt.setTimeTaken(rs.getInt("time_taken"));
        attempt.setAttemptedAt(Timestamp.valueOf(rs.getTimestamp("attempted_at").toLocalDateTime()));
        return attempt;
    }

}
