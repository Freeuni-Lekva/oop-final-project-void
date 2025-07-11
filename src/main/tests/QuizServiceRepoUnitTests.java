import choice.Choice;
import loginflow.Users;
import loginflow.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import question.Question;
import questionBundle.QuestionBundle;
import quiz.Quiz;
import quiz.QuizRepository;
import quiz.QuizService;
import quiz.TakeQuiz.dtos.TakeQuizDto;
import resources.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizRepositoryTest {

    private QuizRepository quizRepository;
    private UsersService usersService;
    private Users testUser;

    @BeforeEach
    void setUp() throws SQLException {
        // Clean up database before each test
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM responses");
            stmt.executeUpdate("DELETE FROM quiz_attempts");
            stmt.executeUpdate("DELETE FROM choices");
            stmt.executeUpdate("DELETE FROM question_answers");
            stmt.executeUpdate("DELETE FROM questions");
            stmt.executeUpdate("DELETE FROM quizzes");
            stmt.executeUpdate("DELETE FROM users WHERE username = 'testUser'");
        }

        this.quizRepository = new QuizRepository(DatabaseConnection.getDataSource());
        this.usersService = new UsersService();

        //creating test user
        try {
            usersService.register("testUser", "password123");
        } catch (SQLException ignored) {
        }
        this.testUser = usersService.findByName("testUser");
    }

    @Test
    void canCreateQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("My First Quiz");
        quiz.setDescription("This is a test quiz");
        quiz.setCreatorId(testUser.getUser_id());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        quizRepository.create(quiz);

        assertTrue(quiz.getQuizId() > 0, "Quiz should have an ID after creation");
    }

    @Test
    void canFindQuizById() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Findable Quiz");
        quiz.setDescription("You can find this quiz");
        quiz.setCreatorId(testUser.getUser_id());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        quizRepository.create(quiz);
        int quizId = quiz.getQuizId();

        Quiz foundQuiz = quizRepository.getById(quizId);

        assertNotNull(foundQuiz, "Should find the quiz");
        assertEquals("Findable Quiz", foundQuiz.getTitle());
        assertEquals("You can find this quiz", foundQuiz.getDescription());
        assertEquals(testUser.getUser_id(), foundQuiz.getCreatorId());
    }

    @Test
    void returnsNullWhenQuizDoesNotExist() {
        Quiz quiz = quizRepository.getById(999999);
        assertNull(quiz, "Should return null for non-existent quiz");
    }

    @Test
    void canGetAllQuizzes() {
        Quiz quiz1 = new Quiz();
        quiz1.setTitle("Quiz One");
        quiz1.setDescription("First quiz");
        quiz1.setCreatorId(testUser.getUser_id());
        quiz1.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Quiz quiz2 = new Quiz();
        quiz2.setTitle("Quiz Two");
        quiz2.setDescription("Second quiz");
        quiz2.setCreatorId(testUser.getUser_id());
        quiz2.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        quizRepository.create(quiz1);
        quizRepository.create(quiz2);

        List<Quiz> allQuizzes = quizRepository.getAll();

        assertTrue(allQuizzes.size() >= 2, "Should have at least 2 quizzes");

        boolean foundQuizOne = allQuizzes.stream().anyMatch(q -> "Quiz One".equals(q.getTitle()));
        boolean foundQuizTwo = allQuizzes.stream().anyMatch(q -> "Quiz Two".equals(q.getTitle()));

        assertTrue(foundQuizOne, "Should find Quiz One");
        assertTrue(foundQuizTwo, "Should find Quiz Two");
    }

    @Test
    void canUpdateQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Original Title");
        quiz.setDescription("Original Description");
        quiz.setCreatorId(testUser.getUser_id());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        quizRepository.create(quiz);

        quiz.setTitle("Updated Title");
        quiz.setDescription("Updated Description");
        quizRepository.update(quiz);

        Quiz updatedQuiz = quizRepository.getById(quiz.getQuizId());
        assertEquals("Updated Title", updatedQuiz.getTitle());
        assertEquals("Updated Description", updatedQuiz.getDescription());
    }

    @Test
    void canDeleteQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Quiz to Delete");
        quiz.setDescription("This quiz will be deleted");
        quiz.setCreatorId(testUser.getUser_id());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        quizRepository.create(quiz);
        int quizId = quiz.getQuizId();

        quizRepository.deleteById(quizId);

        Quiz deletedQuiz = quizRepository.getById(quizId);
        assertNull(deletedQuiz, "Quiz should be deleted");
    }

    @Test
    void canGetQuizzesByCreator() {
        Quiz quiz1 = new Quiz();
        quiz1.setTitle("Creator Quiz 1");
        quiz1.setDescription("Made by test user");
        quiz1.setCreatorId(testUser.getUser_id());
        quiz1.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Quiz quiz2 = new Quiz();
        quiz2.setTitle("Creator Quiz 2");
        quiz2.setDescription("Also made by test user");
        quiz2.setCreatorId(testUser.getUser_id());
        quiz2.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        quizRepository.create(quiz1);
        quizRepository.create(quiz2);

        List<Quiz> userQuizzes = quizRepository.getQuizzesByCreatorId(testUser.getUser_id());

        assertEquals(2, userQuizzes.size(), "Should find 2 quizzes for this user");
        assertTrue(userQuizzes.stream().anyMatch(q -> "Creator Quiz 1".equals(q.getTitle())));
        assertTrue(userQuizzes.stream().anyMatch(q -> "Creator Quiz 2".equals(q.getTitle())));
    }

    @Test
    void returnsEmptyListWhenUserHasNoQuizzes() {
        List<Quiz> userQuizzes = quizRepository.getQuizzesByCreatorId(999999);
        assertTrue(userQuizzes.isEmpty(), "Should return empty list for user with no quizzes");
    }

    @Test
    void canGetQuestionsForQuiz() throws SQLException {
        Quiz quiz = new Quiz();
        quiz.setTitle("Quiz with Questions");
        quiz.setDescription("This quiz has questions");
        quiz.setCreatorId(testUser.getUser_id());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        quizRepository.create(quiz);

        // Add some questions manually
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(String.format(
                    "INSERT INTO questions (quiz_id, question_text, type, question_order) VALUES (%d, 'What is 2+2?', 'multiple_choice', 1)",
                    quiz.getQuizId()));
            stmt.executeUpdate(String.format(
                    "INSERT INTO questions (quiz_id, question_text, type, question_order) VALUES (%d, 'What is 3+3?', 'multiple_choice', 2)",
                    quiz.getQuizId()));
        }

        List<Question> questions = quizRepository.getQuestionsByQuizId(quiz.getQuizId());

        assertEquals(2, questions.size(), "Should have 2 questions");
        assertEquals("What is 2+2?", questions.get(0).getQuestionText());
        assertEquals("What is 3+3?", questions.get(1).getQuestionText());
        assertEquals(1, questions.get(0).getQuestionOrder());
        assertEquals(2, questions.get(1).getQuestionOrder());
    }

    @Test
    void returnsEmptyListWhenQuizHasNoQuestions() throws SQLException {
        List<Question> questions = quizRepository.getQuestionsByQuizId(999999);
        assertTrue(questions.isEmpty(), "Should return empty list for quiz with no questions");
    }
}

class QuizServiceTest {

    private QuizService quizService;
    private UsersService usersService;
    private Users testUser;

    @BeforeEach
    void setUp() throws SQLException {
        // Clean up database before each test
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM responses");
            stmt.executeUpdate("DELETE FROM quiz_attempts");
            stmt.executeUpdate("DELETE FROM choices");
            stmt.executeUpdate("DELETE FROM question_answers");
            stmt.executeUpdate("DELETE FROM questions");
            stmt.executeUpdate("DELETE FROM quizzes");
            stmt.executeUpdate("DELETE FROM users WHERE username = 'serviceTestUser'");
        }

        this.quizService = new QuizService(DatabaseConnection.getDataSource());
        this.usersService = new UsersService();

        // Create test user
        try {
            usersService.register("serviceTestUser", "password123");
        } catch (SQLException ignored) {
            // User already exists
        }
        this.testUser = usersService.findByName("serviceTestUser");
    }

    @Test
    void canCreateQuizWithQuestions() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setTitle("Geography Quiz");
        quiz.setDescription("Test your geography knowledge");
        quiz.setCreatorId(testUser.getUser_id());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Create question bundle
        QuestionBundle bundle = new QuestionBundle();

        Question question = new Question();
        question.setQuestionText("What is the capital of France?");
        question.setType("multiple_choice");
        question.setQuestionOrder(1);
        bundle.setQuestion(question);

        List<Choice> choices = new ArrayList<>();
        Choice correctChoice = new Choice();
        correctChoice.setChoiceText("Paris");
        correctChoice.setIsCorrect(true);

        Choice wrongChoice = new Choice();
        wrongChoice.setChoiceText("London");
        wrongChoice.setIsCorrect(false);

        choices.add(correctChoice);
        choices.add(wrongChoice);
        bundle.setChoices(choices);

        List<QuestionBundle> bundles = new ArrayList<>();
        bundles.add(bundle);

        quizService.createQuizWithQuestions(quiz, bundles);

        assertTrue(quiz.getQuizId() > 0, "Quiz should have an ID after creation");
    }

    @Test
    void canCreateQuizWithMultipleQuestions() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setTitle("Math Quiz");
        quiz.setDescription("Basic math questions");
        quiz.setCreatorId(testUser.getUser_id());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        List<QuestionBundle> bundles = new ArrayList<>();

        // First question
        QuestionBundle bundle1 = new QuestionBundle();
        Question question1 = new Question();
        question1.setQuestionText("What is 2+2?");
        question1.setType("multiple_choice");
        question1.setQuestionOrder(1);
        bundle1.setQuestion(question1);

        List<Choice> choices1 = new ArrayList<>();
        Choice choice1 = new Choice();
        choice1.setChoiceText("4");
        choice1.setIsCorrect(true);
        choices1.add(choice1);
        bundle1.setChoices(choices1);

        QuestionBundle bundle2 = new QuestionBundle();
        Question question2 = new Question();
        question2.setQuestionText("What is 5+5?");
        question2.setType("multiple_choice");
        question2.setQuestionOrder(2);
        bundle2.setQuestion(question2);

        List<Choice> choices2 = new ArrayList<>();
        Choice choice2 = new Choice();
        choice2.setChoiceText("10");
        choice2.setIsCorrect(true);
        choices2.add(choice2);
        bundle2.setChoices(choices2);

        bundles.add(bundle1);
        bundles.add(bundle2);

        quizService.createQuizWithQuestions(quiz, bundles);

        assertTrue(quiz.getQuizId() > 0, "Quiz should be created");
    }

    @Test
    void canCreateQuizWithNoQuestions() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setTitle("Empty Quiz");
        quiz.setDescription("Quiz with no questions yet");
        quiz.setCreatorId(testUser.getUser_id());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        List<QuestionBundle> bundles = new ArrayList<>();

        quizService.createQuizWithQuestions(quiz, bundles);

        assertTrue(quiz.getQuizId() > 0, "Should create quiz even with no questions");
    }

    @Test
    void canGetFullQuiz() {
        // Create quiz with questions and choices in database
        Quiz quiz = new Quiz();
        quiz.setTitle("Complete Quiz");
        quiz.setDescription("Quiz with everything");
        quiz.setCreatorId(testUser.getUser_id());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        QuizRepository quizRepo = new QuizRepository(DatabaseConnection.getDataSource());
        quizRepo.create(quiz);

        // Add questions and choices manually
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(String.format(
                    "INSERT INTO questions (quiz_id, question_text, type, question_order) VALUES (%d, 'Test Question', 'multiple_choice', 1)",
                    quiz.getQuizId()));

            var rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            int questionId = rs.getInt(1);

            stmt.executeUpdate(String.format(
                    "INSERT INTO choices (question_id, choice_text, is_correct) VALUES (%d, 'Correct Answer', true)",
                    questionId));
            stmt.executeUpdate(String.format(
                    "INSERT INTO choices (question_id, choice_text, is_correct) VALUES (%d, 'Wrong Answer', false)",
                    questionId));

        } catch (SQLException e) {
            fail("Could not set up test data");
        }

        TakeQuizDto fullQuiz = quizService.getFullQuiz(quiz.getQuizId());

        assertNotNull(fullQuiz, "Should return full quiz");
        assertEquals("Complete Quiz", fullQuiz.getTitle());
        assertEquals("Quiz with everything", fullQuiz.getDescription());
        assertFalse(fullQuiz.getQuestions().isEmpty(), "Should have questions");
    }

    @Test
    void canGetEmptyQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Empty Quiz");
        quiz.setDescription("No questions here");
        quiz.setCreatorId(testUser.getUser_id());
        quiz.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        QuizRepository quizRepo = new QuizRepository(DatabaseConnection.getDataSource());
        quizRepo.create(quiz);

        TakeQuizDto fullQuiz = quizService.getFullQuiz(quiz.getQuizId());

        assertNotNull(fullQuiz, "Should return quiz even if empty");
        assertEquals("Empty Quiz", fullQuiz.getTitle());
        assertTrue(fullQuiz.getQuestions().isEmpty(), "Should have no questions");
    }
}