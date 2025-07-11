import choice.Choice;
import choice.ChoiceRepository;
import quiz.QuizService;
import quiz.TakeQuiz.dtos.TakeQuizDto;
import resources.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        ChoiceRepository choiceRepository = new ChoiceRepository(DatabaseConnection.getDataSource());
        List<Choice> list = choiceRepository.findByQuestionId(168);

        QuizService quizService = new QuizService(DatabaseConnection.getDataSource());
        TakeQuizDto getFullQuiz = quizService.getFullQuiz(131);
        System.out.println("BLA");
    }
}
