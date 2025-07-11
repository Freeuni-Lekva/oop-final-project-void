import choice.Choice;
import choice.ChoiceRepository;
import question.Question;
import question.QuestionRepository;
import question.QuestionService;
import questionAnswer.QuestionAnswer;
import questionAnswer.QuestionAnswerRepository;
import questionBundle.QuestionBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resources.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    private QuestionService questionService;
    private QuestionRepository questionRepo;
    private ChoiceRepository choiceRepo;
    private QuestionAnswerRepository answerRepo;

    private Question testQuestion;
    private QuestionAnswer testAnswer;
    private List<Choice> testChoices;

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM choices");
            stmt.executeUpdate("DELETE FROM question_answers");
            stmt.executeUpdate("DELETE FROM questions WHERE question_text LIKE 'Test Question%'");
            stmt.executeUpdate("DELETE FROM quizzes WHERE quiz_id = 999");
            stmt.executeUpdate("DELETE FROM users WHERE user_id = 1");
            stmt.executeUpdate("INSERT INTO users (user_id, username, password_hash) VALUES (1, 'testuser', 'password')");
            stmt.executeUpdate("INSERT INTO quizzes (quiz_id, title, description, creator_id) VALUES (999, 'Test Quiz', 'Test Description', 1)");
        }

        this.questionRepo = new QuestionRepository(DatabaseConnection.getDataSource());
        this.choiceRepo = new ChoiceRepository(DatabaseConnection.getDataSource());
        this.answerRepo = new QuestionAnswerRepository(DatabaseConnection.getDataSource());
        this.questionService = new QuestionService(questionRepo, answerRepo, choiceRepo);

        this.testQuestion = Question.builder()
                .quizId(999)
                .questionText("Test Question 1")
                .type("multiple_choice")
                .imageUrl(null)
                .questionOrder(1)
                .build();

        this.testAnswer = QuestionAnswer.builder()
                .questionId(null)
                .answerText("Test Answer")
                .build();

        this.testChoices = new ArrayList<>();
        testChoices.add(Choice.builder()
                .questionId(null)
                .choiceText("Choice A")
                .isCorrect(true)
                .build());
        testChoices.add(Choice.builder()
                .questionId(null)
                .choiceText("Choice B")
                .isCorrect(false)
                .build());
        testChoices.add(Choice.builder()
                .questionId(null)
                .choiceText("Choice C")
                .isCorrect(false)
                .build());
    }

    @Test
    void testCreateQuestionWithAnswersAndChoicesSuccessfully() throws SQLException {
        QuestionBundle bundle = new QuestionBundle(testQuestion, testAnswer, testChoices);
        questionService.createQuestionWithAnswersAndChoices(bundle, 999);

        Question createdQuestion = questionRepo.getById(testQuestion.getQuestionId());
        assertNotNull(createdQuestion);
        assertEquals("Test Question 1", createdQuestion.getQuestionText());
        assertEquals(999, createdQuestion.getQuizId());

        QuestionAnswer createdAnswer = answerRepo.findByQuestionId(createdQuestion.getQuestionId());
        assertNotNull(createdAnswer);
        assertEquals("Test Answer", createdAnswer.getAnswerText());

        List<Choice> createdChoices = choiceRepo.findByQuestionId(createdQuestion.getQuestionId());
        assertEquals(3, createdChoices.size());
        assertTrue(createdChoices.stream().anyMatch(Choice::getIsCorrect));
        assertEquals(2, createdChoices.stream().filter(c -> !c.getIsCorrect()).count());
    }

    @Test
    void testCreateQuestionWithoutAnswer() throws SQLException {
        QuestionBundle bundle = new QuestionBundle(testQuestion, null, testChoices);
        questionService.createQuestionWithAnswersAndChoices(bundle, 999);

        Question createdQuestion = questionRepo.getById(testQuestion.getQuestionId());
        assertNotNull(createdQuestion);

        QuestionAnswer createdAnswer = answerRepo.findByQuestionId(createdQuestion.getQuestionId());
        assertNull(createdAnswer);

        List<Choice> createdChoices = choiceRepo.findByQuestionId(createdQuestion.getQuestionId());
        assertEquals(3, createdChoices.size());
    }


    @Test
    void testGetQuestionBundleSuccessfully() throws Exception {
        QuestionBundle bundle = new QuestionBundle(testQuestion, testAnswer, testChoices);
        questionService.createQuestionWithAnswersAndChoices(bundle, 999);
        QuestionBundle retrievedBundle = questionService.getQuestionBundle(testQuestion.getQuestionId());

        assertNotNull(retrievedBundle);
        assertEquals("Test Question 1", retrievedBundle.getQuestion().getQuestionText());
        assertEquals("Test Answer", retrievedBundle.getAnswer().getAnswerText());
        assertEquals(3, retrievedBundle.getChoices().size());
    }

    @Test
    void testGetQuestionBundleWithNonExistentQuestion() throws Exception {
        QuestionBundle bundle = questionService.getQuestionBundle(99999);
        assertNull(bundle.getQuestion());
        assertNull(bundle.getAnswer());
        assertTrue(bundle.getChoices().isEmpty());
    }

    @Test
    void testDeleteQuestionSuccessfully() throws Exception {
        QuestionBundle bundle = new QuestionBundle(testQuestion, testAnswer, testChoices);
        questionService.createQuestionWithAnswersAndChoices(bundle, 999);

        int questionId = testQuestion.getQuestionId();
        assertNotNull(questionRepo.getById(questionId));
        assertNotNull(answerRepo.findByQuestionId(questionId));
        assertFalse(choiceRepo.findByQuestionId(questionId).isEmpty());

        questionService.deleteQuestion(questionId);
        assertNull(questionRepo.getById(questionId));
        assertNull(answerRepo.findByQuestionId(questionId));
        assertTrue(choiceRepo.findByQuestionId(questionId).isEmpty());
    }

    @Test
    void testGetAllByQuizId() {
        Question question1 = Question.builder()
                .quizId(999)
                .questionText("Test Question 1")
                .type("multiple_choice")
                .questionOrder(1)
                .build();

        Question question2 = Question.builder()
                .quizId(999)
                .questionText("Test Question 2")
                .type("fill_blank")
                .questionOrder(2)
                .build();

        questionRepo.create(question1);
        questionRepo.create(question2);

        List<Question> questions = questionRepo.getAllByQuizId(999);
        assertEquals(2, questions.size());
        assertTrue(questions.stream().anyMatch(q -> q.getQuestionText().equals("Test Question 1")));
        assertTrue(questions.stream().anyMatch(q -> q.getQuestionText().equals("Test Question 2")));
    }

    @Test
    void testGetAllByQuizIdReturnsEmptyForNonExistentQuiz() {
        List<Question> questions = questionRepo.getAllByQuizId(999);
        assertTrue(questions.isEmpty());
    }

    @Test
    void testFindByQuestionIdReturnsNullForNonExistentQuestion() throws SQLException {
        QuestionAnswer answer = answerRepo.findByQuestionId(999);
        assertNull(answer);
    }

    @Test
    void testFindByQuestionIdReturnsEmptyListForNonExistentQuestion() throws SQLException {
        List<Choice> choices = choiceRepo.findByQuestionId(999);
        assertTrue(choices.isEmpty());
    }

    @Test
    void testQuestionWithImageUrl() {
        Question questionWithImage = Question.builder()
                .quizId(999)
                .questionText("Test Question with Image")
                .type("picture_response")
                .imageUrl("https://example.com/image.jpg")
                .questionOrder(1)
                .build();

        questionRepo.create(questionWithImage);

        Question retrievedQuestion = questionRepo.getById(questionWithImage.getQuestionId());
        assertNotNull(retrievedQuestion);
        assertEquals("https://example.com/image.jpg", retrievedQuestion.getImageUrl());
    }

    @Test
    void testQuestionWithNullImageUrl() {
        Question questionWithoutImage = Question.builder()
                .quizId(999)
                .questionText("Test Question without Image")
                .type("multiple_choice")
                .imageUrl(null)
                .questionOrder(1)
                .build();

        questionRepo.create(questionWithoutImage);

        Question retrievedQuestion = questionRepo.getById(questionWithoutImage.getQuestionId());
        assertNotNull(retrievedQuestion);
        assertNull(retrievedQuestion.getImageUrl());
    }

    @Test
    void testUpdateQuestionSuccessfully() throws SQLException {
        // Create a question first
        questionRepo.create(testQuestion);
        int questionId = testQuestion.getQuestionId();
        
        // Update the question
        Question updatedQuestion = Question.builder()
                .questionId(questionId)
                .quizId(999)
                .questionText("Updated Test Question")
                .type("fill_blank")
                .imageUrl("https://example.com/updated-image.jpg")
                .questionOrder(2)
                .build();
        
        questionRepo.update(updatedQuestion);
        
        // Verify the update
        Question retrievedQuestion = questionRepo.getById(questionId);
        assertNotNull(retrievedQuestion);
        assertEquals("Updated Test Question", retrievedQuestion.getQuestionText());
        assertEquals("fill_blank", retrievedQuestion.getType());
        assertEquals("https://example.com/updated-image.jpg", retrievedQuestion.getImageUrl());
        assertEquals(2, retrievedQuestion.getQuestionOrder());
    }

    @Test
    void testUpdateQuestionWithNonExistentId() throws SQLException {
        Question nonExistentQuestion = Question.builder()
                .questionId(99999)
                .quizId(999)
                .questionText("Non-existent Question")
                .type("multiple_choice")
                .questionOrder(1)
                .build();
        
        // Should not throw exception, but should not affect any rows
        questionRepo.update(nonExistentQuestion);
        
        // Verify no question was created
        Question retrievedQuestion = questionRepo.getById(99999);
        assertNull(retrievedQuestion);
    }

    @Test
    void testUpdateQuestionWithAnswersAndChoicesSuccessfully() throws Exception {
        // Create initial question bundle
        QuestionBundle initialBundle = new QuestionBundle(testQuestion, testAnswer, testChoices);
        questionService.createQuestionWithAnswersAndChoices(initialBundle, 999);
        
        int questionId = testQuestion.getQuestionId();
        
        // Create updated question bundle
        Question updatedQuestion = Question.builder()
                .questionId(questionId)
                .quizId(999)
                .questionText("Updated Question Text")
                .type("fill_blank")
                .imageUrl("https://example.com/updated-image.jpg")
                .questionOrder(3)
                .build();
        
        QuestionAnswer updatedAnswer = QuestionAnswer.builder()
                .questionId(questionId)
                .answerText("Updated Answer Text")
                .build();
        
        List<Choice> updatedChoices = new ArrayList<>();
        updatedChoices.add(Choice.builder()
                .questionId(questionId)
                .choiceText("Updated Choice A")
                .isCorrect(true)
                .build());
        updatedChoices.add(Choice.builder()
                .questionId(questionId)
                .choiceText("Updated Choice B")
                .isCorrect(false)
                .build());
        
        QuestionBundle updatedBundle = new QuestionBundle(updatedQuestion, updatedAnswer, updatedChoices);
        
        // Update the question bundle
        questionService.updateQuestionWithAnswersAndChoices(updatedBundle);
        
        // Verify the update
        QuestionBundle retrievedBundle = questionService.getQuestionBundle(questionId);
        assertNotNull(retrievedBundle);
        assertEquals("Updated Question Text", retrievedBundle.getQuestion().getQuestionText());
        assertEquals("fill_blank", retrievedBundle.getQuestion().getType());
        assertEquals("https://example.com/updated-image.jpg", retrievedBundle.getQuestion().getImageUrl());
        assertEquals(3, retrievedBundle.getQuestion().getQuestionOrder());
        
        assertNotNull(retrievedBundle.getAnswer());
        assertEquals("Updated Answer Text", retrievedBundle.getAnswer().getAnswerText());
        
        assertEquals(2, retrievedBundle.getChoices().size());
        assertTrue(retrievedBundle.getChoices().stream().anyMatch(Choice::getIsCorrect));
        assertEquals(1, retrievedBundle.getChoices().stream().filter(c -> !c.getIsCorrect()).count());
    }

    @Test
    void testUpdateQuestionWithAnswersAndChoicesWithoutAnswer() throws Exception {
        // Create initial question bundle with answer
        QuestionBundle initialBundle = new QuestionBundle(testQuestion, testAnswer, testChoices);
        questionService.createQuestionWithAnswersAndChoices(initialBundle, 999);
        
        int questionId = testQuestion.getQuestionId();
        
        // Create updated question bundle without answer
        Question updatedQuestion = Question.builder()
                .questionId(questionId)
                .quizId(999)
                .questionText("Updated Question Without Answer")
                .type("multiple_choice")
                .questionOrder(1)
                .build();
        
        List<Choice> updatedChoices = new ArrayList<>();
        updatedChoices.add(Choice.builder()
                .questionId(questionId)
                .choiceText("New Choice A")
                .isCorrect(true)
                .build());
        
        QuestionBundle updatedBundle = new QuestionBundle(updatedQuestion, null, updatedChoices);
        
        // Update the question bundle
        questionService.updateQuestionWithAnswersAndChoices(updatedBundle);
        
        // Verify the update
        QuestionBundle retrievedBundle = questionService.getQuestionBundle(questionId);
        assertNotNull(retrievedBundle);
        assertEquals("Updated Question Without Answer", retrievedBundle.getQuestion().getQuestionText());
        assertNull(retrievedBundle.getAnswer());
        assertEquals(1, retrievedBundle.getChoices().size());
        assertTrue(retrievedBundle.getChoices().get(0).getIsCorrect());
    }

    @Test
    void testUpdateQuestionWithAnswersAndChoicesWithNewChoices() throws Exception {
        // Create initial question bundle
        QuestionBundle initialBundle = new QuestionBundle(testQuestion, testAnswer, testChoices);
        questionService.createQuestionWithAnswersAndChoices(initialBundle, 999);
        
        int questionId = testQuestion.getQuestionId();
        
        // Create updated question bundle with different choices
        Question updatedQuestion = Question.builder()
                .questionId(questionId)
                .quizId(999)
                .questionText("Updated Question with New Choices")
                .type("multiple_choice")
                .questionOrder(1)
                .build();
        
        List<Choice> newChoices = new ArrayList<>();
        newChoices.add(Choice.builder()
                .questionId(questionId)
                .choiceText("New Choice 1")
                .isCorrect(false)
                .build());
        newChoices.add(Choice.builder()
                .questionId(questionId)
                .choiceText("New Choice 2")
                .isCorrect(true)
                .build());
        newChoices.add(Choice.builder()
                .questionId(questionId)
                .choiceText("New Choice 3")
                .isCorrect(false)
                .build());
        newChoices.add(Choice.builder()
                .questionId(questionId)
                .choiceText("New Choice 4")
                .isCorrect(false)
                .build());
        
        QuestionBundle updatedBundle = new QuestionBundle(updatedQuestion, testAnswer, newChoices);
        
        // Update the question bundle
        questionService.updateQuestionWithAnswersAndChoices(updatedBundle);
        
        // Verify the update
        QuestionBundle retrievedBundle = questionService.getQuestionBundle(questionId);
        assertNotNull(retrievedBundle);
        assertEquals("Updated Question with New Choices", retrievedBundle.getQuestion().getQuestionText());
        assertEquals(4, retrievedBundle.getChoices().size());
        assertEquals(1, retrievedBundle.getChoices().stream().filter(Choice::getIsCorrect).count());
        assertEquals(3, retrievedBundle.getChoices().stream().filter(c -> !c.getIsCorrect()).count());
    }

    @Test
    void testUpdateQuestionWithAnswersAndChoicesWithEmptyChoices() throws Exception {
        // Create initial question bundle
        QuestionBundle initialBundle = new QuestionBundle(testQuestion, testAnswer, testChoices);
        questionService.createQuestionWithAnswersAndChoices(initialBundle, 999);
        
        int questionId = testQuestion.getQuestionId();
        
        // Create updated question bundle with empty choices
        Question updatedQuestion = Question.builder()
                .questionId(questionId)
                .quizId(999)
                .questionText("Updated Question with Empty Choices")
                .type("fill_blank")
                .questionOrder(1)
                .build();
        
        List<Choice> emptyChoices = new ArrayList<>();
        QuestionBundle updatedBundle = new QuestionBundle(updatedQuestion, testAnswer, emptyChoices);
        
        // Update the question bundle
        questionService.updateQuestionWithAnswersAndChoices(updatedBundle);
        
        // Verify the update
        QuestionBundle retrievedBundle = questionService.getQuestionBundle(questionId);
        assertNotNull(retrievedBundle);
        assertEquals("Updated Question with Empty Choices", retrievedBundle.getQuestion().getQuestionText());
        assertTrue(retrievedBundle.getChoices().isEmpty());
    }

}