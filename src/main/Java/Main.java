import choice.Choice;
import choice.ChoiceRepository;
import quiz.QuizRepository;
import quiz.QuizService;
import quiz.TakeQuiz.dtos.TakeQuizDto;
import resources.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        var k = new QuizRepository(DatabaseConnection.getDataSource()).getById(1);
        System.out.println("BLA");
    }
}
