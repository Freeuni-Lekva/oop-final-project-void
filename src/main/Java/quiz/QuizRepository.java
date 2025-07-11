package quiz;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuizRepository extends AbstractRepository<Quiz> {

    public QuizRepository(BasicDataSource dataSource) {
        super(dataSource);
    }


    @Override
    public Quiz getById(long id) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from quizzes where quiz_id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            return Quiz.builder().quizId(resultSet.getInt("quiz_id"))
                    .title(resultSet.getString("title"))
                    .creatorId(resultSet.getInt("creator_id"))
                    .description(resultSet.getString("description"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Quiz> getAll() {
        return List.of();
    }

    @Override
    public void create(Quiz quiz) {

    }

    @Override
    public void update(Quiz quiz) {

    }

    @Override
    public void deleteById(long id) {

    }
}
