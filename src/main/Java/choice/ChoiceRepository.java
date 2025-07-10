package choice;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChoiceRepository extends AbstractRepository<Choice> {
    public ChoiceRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Choice getById(long id) {
        return null;
    }

    @Override
    public List<Choice> getAll() {
        return List.of();
    }

    @Override
    public void create(Choice choice) {

    }

    @Override
    public void update(Choice choice) {

    }

    @Override
    public void deleteById(long id) {

    }

    public List<Choice> getAllChoicesByQuestionId(int id) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from choices where question_id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<Choice> choices = new ArrayList<>();
            while (resultSet.next()) {
                Choice c = Choice.builder()
                        .question_id(id)
                        .choice_id(resultSet.getInt("choice_id"))
                        .is_correct(resultSet.getBoolean("is_correct"))
                        .choice_text(resultSet.getString("choice_text"))
                        .build();

                choices.add(c);
            }
            return choices;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Choice> getAllChoicesByQuizId(int quizId) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT c.* FROM choices c " +
                            "JOIN questions q ON c.question_id = q.question_id " +
                            "WHERE q.quiz_id = ?"
            );
            statement.setInt(1, quizId);

            ResultSet resultSet = statement.executeQuery();
            List<Choice> choices = new ArrayList<>();

            while (resultSet.next()) {
                Choice c = Choice.builder()
                        .question_id(resultSet.getInt("question_id"))
                        .choice_id(resultSet.getInt("choice_id"))
                        .is_correct(resultSet.getBoolean("is_correct"))
                        .choice_text(resultSet.getString("choice_text"))
                        .build();
                choices.add(c);
            }

            return choices;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}
