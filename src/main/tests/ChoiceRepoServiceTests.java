
import choice.Choice;
import choice.ChoiceRepository;
import choice.ChoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resources.DatabaseConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChoiceServiceTest {

    private ChoiceService choiceService;
    private ChoiceRepository choiceRepository;

    @BeforeEach
    void setUp() throws Exception {
        choiceRepository = new ChoiceRepository(DatabaseConnection.getDataSource());
        choiceService = new ChoiceService(choiceRepository);

        try (Connection conn = DatabaseConnection.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM choices");
            stmt.executeUpdate("DELETE FROM questions");
            stmt.executeUpdate("DELETE FROM quizzes");

            // insert dummy quiz and questions
            stmt.executeUpdate("INSERT INTO quizzes (quiz_id, title) VALUES (1, 'Sample Quiz')");
            stmt.executeUpdate("INSERT INTO questions (question_id, quiz_id, question_text) VALUES (1, 1, 'Q1'), (2, 1, 'Q2'), (3, 1, 'Q3')");

            // insert dummy choices
            stmt.executeUpdate("INSERT INTO choices (choice_id, question_id, choice_text, is_correct) VALUES " +
                    "(1, 1, 'Choice A', true), " +
                    "(2, 1, 'Choice B', false), " +
                    "(3, 2, 'Choice C', true), " +
                    "(4, 2, 'Choice D', false), " +
                    "(5, 3, 'Choice E', false)");
        }
    }

    @Test
    void getChoicesByValidQuestionId() {
        List<Choice> choices = choiceService.getChoicesByQuestionId(1);
        assertEquals(2, choices.size());
    }

    @Test
    void getChoicesByInvalidQuestionIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> choiceService.getChoicesByQuestionId(0));
    }

    @Test
    void getChoicesByValidQuizId() {
        List<Choice> choices = choiceService.getChoicesByQuizId(1);
        assertEquals(5, choices.size());
    }

    @Test
    void getChoicesByInvalidQuizIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> choiceService.getChoicesByQuizId(-1));
    }

    @Test
    void getCorrectChoicesReturnsOnlyCorrectOnes() {
        List<Choice> correct = choiceService.getCorrectChoices(List.of(1, 2, 3));
        assertEquals(2, correct.size());
        assertTrue(correct.stream().allMatch(Choice::getIsCorrect));
    }

    @Test
    void getCorrectChoicesWithEmptyListReturnsEmpty() {
        List<Choice> correct = choiceService.getCorrectChoices(List.of());
        assertTrue(correct.isEmpty());
    }
}


class ChoiceRepositoryTest {

    private ChoiceRepository choiceRepository;

    @BeforeEach
    void setUp() throws Exception {
        choiceRepository = new ChoiceRepository(DatabaseConnection.getDataSource());

        try (Connection conn = DatabaseConnection.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM choices");
            stmt.executeUpdate("DELETE FROM questions");
            stmt.executeUpdate("DELETE FROM quizzes");

            stmt.executeUpdate("INSERT INTO quizzes (quiz_id, title) VALUES (1, 'Sample Quiz')");
            stmt.executeUpdate("INSERT INTO questions (question_id, quiz_id, question_text) VALUES (1, 1, 'Q1'), (2, 1, 'Q2')");

            stmt.executeUpdate("INSERT INTO choices (choice_id, question_id, choice_text, is_correct) VALUES " +
                    "(1, 1, 'Choice A', true), " +
                    "(2, 1, 'Choice B', false), " +
                    "(3, 2, 'Choice C', true)");
        }
    }

    @Test
    void testGetByIdExisting() {
        Choice choice = choiceRepository.getById(1);
        assertNotNull(choice);
        assertEquals("Choice A", choice.getChoiceText());
    }

    @Test
    void testGetByIdNonExisting() {
        assertNull(choiceRepository.getById(999));
    }

    @Test
    void testGetAllReturnsAll() {
        List<Choice> all = choiceRepository.getAll();
        assertEquals(3, all.size());
    }

    @Test
    void testCreateNewChoice() {
        Choice choice = Choice.builder()
                .questionId(1)
                .choiceText("New Option")
                .isCorrect(true)
                .build();

        choiceRepository.create(choice);

        assertNotNull(choice.getChoiceId());
        Choice fetched = choiceRepository.getById(choice.getChoiceId());
        assertEquals("New Option", fetched.getChoiceText());
    }

    @Test
    void testUpdateChoice() {
        Choice choice = choiceRepository.getById(1);
        choice.setChoiceText("Updated");
        choiceRepository.update(choice);

        Choice updated = choiceRepository.getById(1);
        assertEquals("Updated", updated.getChoiceText());
    }

    @Test
    void testDeleteChoiceById() {
        choiceRepository.deleteById(1);
        assertNull(choiceRepository.getById(1));
    }

    @Test
    void testGetChoicesByQuestionId() {
        List<Choice> result = choiceRepository.getAllChoicesByQuestionId(1);
        assertEquals(2, result.size());
    }

    @Test
    void testGetChoicesByQuizId() {
        List<Choice> result = choiceRepository.getAllChoicesByQuizId(1);
        assertEquals(3, result.size());
    }

    @Test
    void testFindByQuestionId() throws Exception {
        List<Choice> result = choiceRepository.findByQuestionId(2);
        assertEquals(1, result.size());
        assertEquals("Choice C", result.get(0).getChoiceText());
    }
}
