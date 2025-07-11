import quizAttempt.QuizAttempt;
import quizAttempt.QuizAttemptRepository;
import quizAttempt.QuizAttemptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resources.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QuizAttemptTest {

    private QuizAttemptService quizAttemptService;
    private QuizAttemptRepository quizAttemptRepo;

    private QuizAttempt testQuizAttempt;

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM quiz_attempts WHERE attempt_id IN (999, 888, 777)");
            stmt.executeUpdate("DELETE FROM quizzes WHERE quiz_id IN (999, 888)");
            stmt.executeUpdate("DELETE FROM users WHERE user_id IN (1, 2)");
            stmt.executeUpdate("INSERT INTO users (user_id, username, password_hash) VALUES (1, 'testuser1', 'password1')");
            stmt.executeUpdate("INSERT INTO users (user_id, username, password_hash) VALUES (2, 'testuser2', 'password2')");
            stmt.executeUpdate("INSERT INTO quizzes (quiz_id, title, description, creator_id) VALUES (999, 'Test Quiz 1', 'Test Description 1', 1)");
            stmt.executeUpdate("INSERT INTO quizzes (quiz_id, title, description, creator_id) VALUES (888, 'Test Quiz 2', 'Test Description 2', 1)");
        }

        this.quizAttemptRepo = new QuizAttemptRepository(DatabaseConnection.getDataSource());
        this.quizAttemptService = new QuizAttemptService(quizAttemptRepo);

        this.testQuizAttempt = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(8)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }


    @Test
    void testCreateQuizAttemptWithNullAttempt() {
        assertThrows(IllegalArgumentException.class, () -> {
            quizAttemptService.createQuizAttempt(null);
        });
    }

    @Test
    void testCreateQuizAttemptWithInvalidQuizId() {
        QuizAttempt invalidAttempt = QuizAttempt.builder()
                .quizId(0)
                .userId(1)
                .score(80)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            quizAttemptService.createQuizAttempt(invalidAttempt);
        });
    }

    @Test
    void testCreateQuizAttemptWithInvalidUserId() {
        QuizAttempt invalidAttempt = QuizAttempt.builder()
                .quizId(999)
                .userId(0)
                .score(80)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            quizAttemptService.createQuizAttempt(invalidAttempt);
        });
    }

    @Test
    void testCreateQuizAttemptWithNegativeTotalQuestions() {
        QuizAttempt invalidAttempt = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(80)
                .totalQuestions(-1)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            quizAttemptService.createQuizAttempt(invalidAttempt);
        });
    }

    @Test
    void testCreateQuizAttemptWithInvalidScore() {
        QuizAttempt invalidAttempt = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(15)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            quizAttemptService.createQuizAttempt(invalidAttempt);
        });
    }

    @Test
    void testCreateQuizAttemptWithNegativeTimeTaken() {
        QuizAttempt invalidAttempt = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(80)
                .totalQuestions(10)
                .timeTaken(-1)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            quizAttemptService.createQuizAttempt(invalidAttempt);
        });
    }

    @Test
    void testCreateQuizAttemptWithNullAttemptedAt() {
        QuizAttempt invalidAttempt = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(80)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            quizAttemptService.createQuizAttempt(invalidAttempt);
        });
    }


    @Test
    void testGetQuizAttemptByIdWithNonExistentId() throws SQLException {
        QuizAttempt retrievedAttempt = quizAttemptRepo.getById(99999);
        assertNull(retrievedAttempt);
    }



    @Test
    void testGetAttemptsByUserAndQuiz() throws SQLException {
        QuizAttempt attempt1 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(80)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        QuizAttempt attempt2 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(90)
                .totalQuestions(10)
                .timeTaken(250)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        quizAttemptRepo.create(attempt1);
        quizAttemptRepo.create(attempt2);

        List<QuizAttempt> attempts = quizAttemptRepo.getAttemptsByUserAndQuiz(1L, 999L, "score");
        assertEquals(2, attempts.size());
        assertEquals(90, attempts.get(0).getScore());
        assertEquals(80, attempts.get(1).getScore());
    }

    @Test
    void testGetAttemptsByUserAndQuizWithTimeSort() throws SQLException {
        QuizAttempt attempt1 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(80)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now().minusHours(1)))
                .build();
        QuizAttempt attempt2 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(90)
                .totalQuestions(10)
                .timeTaken(250)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        quizAttemptRepo.create(attempt1);
        quizAttemptRepo.create(attempt2);

        List<QuizAttempt> attempts = quizAttemptRepo.getAttemptsByUserAndQuiz(1L, 999L, "time");
        assertEquals(2, attempts.size());
        assertTrue(attempts.get(0).getAttemptedAt().after(attempts.get(1).getAttemptedAt()));
    }

    @Test
    void testGetTopPerformers() throws SQLException {
        QuizAttempt attempt1 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(70)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        QuizAttempt attempt2 = QuizAttempt.builder()
                .quizId(999)
                .userId(2)
                .score(90)
                .totalQuestions(10)
                .timeTaken(250)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        QuizAttempt attempt3 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(85)
                .totalQuestions(10)
                .timeTaken(280)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        quizAttemptRepo.create(attempt1);
        quizAttemptRepo.create(attempt2);
        quizAttemptRepo.create(attempt3);

        List<QuizAttempt> topPerformers = quizAttemptRepo.getTopPerformers(999L, 2, null);
        assertEquals(2, topPerformers.size());
        assertEquals(90, topPerformers.get(0).getScore());
        assertEquals(85, topPerformers.get(1).getScore());
    }

    @Test
    void testGetRecentAttempts() throws SQLException {
        QuizAttempt attempt1 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(80)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now().minusHours(2)))
                .build();
        QuizAttempt attempt2 = QuizAttempt.builder()
                .quizId(999)
                .userId(2)
                .score(90)
                .totalQuestions(10)
                .timeTaken(250)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now().minusHours(1)))
                .build();
        QuizAttempt attempt3 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(85)
                .totalQuestions(10)
                .timeTaken(280)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        quizAttemptRepo.create(attempt1);
        quizAttemptRepo.create(attempt2);
        quizAttemptRepo.create(attempt3);

        List<QuizAttempt> recentAttempts = quizAttemptRepo.getRecentAttempts(999L, 2);
        assertEquals(2, recentAttempts.size());
        assertTrue(recentAttempts.get(0).getAttemptedAt().after(recentAttempts.get(1).getAttemptedAt()));
    }

    @Test
    void testGetAllAttemptsByUser() throws SQLException {
        QuizAttempt attempt1 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(80)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        QuizAttempt attempt2 = QuizAttempt.builder()
                .quizId(888)
                .userId(1)
                .score(90)
                .totalQuestions(10)
                .timeTaken(250)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        QuizAttempt attempt3 = QuizAttempt.builder()
                .quizId(999)
                .userId(2)
                .score(85)
                .totalQuestions(10)
                .timeTaken(280)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        quizAttemptRepo.create(attempt1);
        quizAttemptRepo.create(attempt2);
        quizAttemptRepo.create(attempt3);

        List<QuizAttempt> userAttempts = quizAttemptRepo.getAllAttemptsByUser(1L);
        assertEquals(2, userAttempts.size());
        assertTrue(userAttempts.stream().allMatch(a -> a.getUserId() == 1));
    }

    @Test
    void testGetQuizStats() throws SQLException {
        QuizAttempt attempt1 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(80)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        QuizAttempt attempt2 = QuizAttempt.builder()
                .quizId(999)
                .userId(2)
                .score(90)
                .totalQuestions(10)
                .timeTaken(250)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        QuizAttempt attempt3 = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(85)
                .totalQuestions(10)
                .timeTaken(280)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        quizAttemptRepo.create(attempt1);
        quizAttemptRepo.create(attempt2);
        quizAttemptRepo.create(attempt3);

        Map<String, Object> stats = quizAttemptRepo.getQuizStats(999L);
        assertEquals(3, stats.get("total_attempts"));
        assertEquals(85.0, stats.get("average_score"));
        assertEquals(90.0, stats.get("highest_score"));
    }

    @Test
    void testGetQuizStatsForEmptyQuiz() throws SQLException {
        Map<String, Object> stats = quizAttemptRepo.getQuizStats(99999L);
        assertEquals(0, stats.get("total_attempts"));
        assertEquals(0.0, stats.get("average_score"));
        assertEquals(0.0, stats.get("highest_score"));
    }

    @Test
    void testGetAttemptsByUserAndQuizReturnsEmptyForNonExistentUser() throws SQLException {
        List<QuizAttempt> attempts = quizAttemptRepo.getAttemptsByUserAndQuiz(99999L, 999L, "score");
        assertTrue(attempts.isEmpty());
    }

    @Test
    void testGetAttemptsByUserAndQuizReturnsEmptyForNonExistentQuiz() throws SQLException {
        List<QuizAttempt> attempts = quizAttemptRepo.getAttemptsByUserAndQuiz(1L, 99999L, "score");
        assertTrue(attempts.isEmpty());
    }

    @Test
    void testGetAllAttemptsByUserReturnsEmptyForNonExistentUser() throws SQLException {
        List<QuizAttempt> attempts = quizAttemptRepo.getAllAttemptsByUser(99999L);
        assertTrue(attempts.isEmpty());
    }

    @Test
    void testQuizAttemptWithZeroScore() throws SQLException {
        QuizAttempt zeroScoreAttempt = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(0)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        QuizAttempt createdAttempt = quizAttemptService.createQuizAttempt(zeroScoreAttempt);
        assertNotNull(createdAttempt);
        assertEquals(0, createdAttempt.getScore());
    }

    @Test
    void testQuizAttemptWithPerfectScore() throws SQLException {
        QuizAttempt perfectScoreAttempt = QuizAttempt.builder()
                .quizId(999)
                .userId(1)
                .score(10)
                .totalQuestions(10)
                .timeTaken(300)
                .attemptedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        QuizAttempt createdAttempt = quizAttemptService.createQuizAttempt(perfectScoreAttempt);
        assertNotNull(createdAttempt);
        assertEquals(10, createdAttempt.getScore());
    }
} 