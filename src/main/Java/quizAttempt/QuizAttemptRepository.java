package quizAttempt;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.*;
import java.util.List;

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
        String sql = "INSERT INTO quiz_attempts (quiz_id, user_id, score, total_questions, time_taken,  attempted_at) " +
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
}
