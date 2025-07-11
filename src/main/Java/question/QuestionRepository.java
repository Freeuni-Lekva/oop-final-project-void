package question;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository extends AbstractRepository<Question> {
    public QuestionRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Question getById(long id) {
        return null;
    }

    @Override
    public List<Question> getAll() {
        return List.of();
    }

    @Override
    public void create(Question question) {

    }

    @Override
    public void update(Question question) {

    }

    @Override
    public void deleteById(long id) {

    }

    public List<Question> getAllByQuizId(int id) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from questions where quiz_id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<Question> questions = new ArrayList<>();
            while (resultSet.next()) {
                Question question = Question.builder()
                        .questionId(resultSet.getInt("question_id"))
                        .questionOrder(resultSet.getInt("question_order"))
                        .questionText(resultSet.getString("question_text"))
                        .imageUrl(resultSet.getString("image_url"))
                        .quizId(resultSet.getInt("quiz_id"))
                        .type(resultSet.getString("type"))
                        .build();

                questions.add(question);
            }
            return questions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
