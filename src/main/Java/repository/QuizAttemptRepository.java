package repository;

import entities.QuizAttempt;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.*;

public class QuizAttemptRepository extends AbstractRepository<QuizAttempt> {
    public QuizAttemptRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    //do not need these
    @Override
    public QuizAttempt getById(long id) {
        return null;
    }

    @Override
    public List<QuizAttempt> getAll() {
        return Collections.emptyList();
    }

    @Override
    public void create(QuizAttempt quizAttempt) {

    }

    @Override
    public void update(QuizAttempt quizAttempt) {

    }

    @Override
    public void deleteById(long id) {

    }

    //------------------------------------------------------------------------------------------------------------------
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
        QuizAttempt attempt = new QuizAttempt();
        attempt.setAttempt_id((int) rs.getLong("attempt_id"));
        attempt.setQuiz_id((int) rs.getLong("quiz_id"));
        attempt.setUser_id((int) rs.getLong("user_id"));
        attempt.setScore(rs.getFloat("score"));
        attempt.setTotal_questions(rs.getInt("total_questions"));
        attempt.setTime_taken(rs.getInt("time_taken"));
        attempt.setIs_practice(rs.getBoolean("is_practice"));
        attempt.setAttempted_at(Timestamp.valueOf(rs.getTimestamp("attempted_at").toLocalDateTime()));
        return attempt;
    }
}
