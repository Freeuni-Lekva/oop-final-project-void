package choice;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChoiceRepository extends AbstractRepository<Choice> {
    public ChoiceRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Choice getById(long id) {
        String sql = "SELECT * FROM choices WHERE choice_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Choice(
                            rs.getInt("choice_id"),
                            rs.getInt("question_id"),
                            rs.getString("choice_text"),
                            rs.getBoolean("is_correct")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Choice> getAll() {
        List<Choice> choices = new ArrayList<>();
        String sql = "SELECT * FROM choices";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                choices.add(new Choice(
                        rs.getInt("choice_id"),
                        rs.getInt("question_id"),
                        rs.getString("choice_text"),
                        rs.getBoolean("is_correct")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return choices;
    }

    @Override
    public void create(Choice choice) {
        String sql = "INSERT INTO choices (question_id, choice_text, is_correct) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, choice.getQuestionId());
            stmt.setString(2, choice.getChoiceText());
            stmt.setBoolean(3, choice.getIsCorrect() != null ? choice.getIsCorrect() : false);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    choice.setChoiceId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Choice choice) {
        String sql = "UPDATE choices SET question_id=?, choice_text=?, is_correct=? WHERE choice_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, choice.getQuestionId());
            stmt.setString(2, choice.getChoiceText());
            stmt.setBoolean(3, choice.getIsCorrect() != null ? choice.getIsCorrect() : false);
            stmt.setInt(4, choice.getChoiceId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM choices WHERE choice_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Choice> getAllChoicesByQuestionId(int id) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from choices where question_id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<Choice> choices = new ArrayList<>();
            while (resultSet.next()) {
                Choice c = Choice.builder()
                        .questionId(id)
                        .choiceId(resultSet.getInt("choice_id"))
                        .isCorrect(resultSet.getBoolean("is_correct"))
                        .choiceText(resultSet.getString("choice_text"))
                        .build();

                choices.add(c);
            }
            System.out.println("AQ MOVIDAA " +  choices.size());
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
                        .questionId(resultSet.getInt("question_id"))
                        .choiceId(resultSet.getInt("choice_id"))
                        .isCorrect(resultSet.getBoolean("is_correct"))
                        .choiceText(resultSet.getString("choice_text"))
                        .build();
                choices.add(c);
            }

            return choices;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Choice> findByQuestionId(int questionId) throws SQLException {
        String sql = "SELECT * FROM choices WHERE question_id = ?";
        List<Choice> choices = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Choice choice = new Choice(-1,-1,"", false);
                    choice.setChoiceId(rs.getInt("choice_id"));
                    choice.setQuestionId(rs.getInt("question_id"));
                    choice.setChoiceText(rs.getString("choice_text"));
                    choice.setIsCorrect(rs.getBoolean("is_correct"));
                    choices.add(choice);
                }
            }
        }
        return choices;
    }


}
