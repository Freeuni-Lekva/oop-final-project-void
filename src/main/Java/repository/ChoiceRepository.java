package repository;

import entities.Choice;
import org.apache.commons.dbcp2.BasicDataSource;
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
            stmt.setInt(1, choice.getQuestion_id());
            stmt.setString(2, choice.getChoice_text());
            stmt.setBoolean(3, choice.getIs_correct() != null ? choice.getIs_correct() : false);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    choice.setChoice_id(rs.getInt(1));
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
            stmt.setInt(1, choice.getQuestion_id());
            stmt.setString(2, choice.getChoice_text());
            stmt.setBoolean(3, choice.getIs_correct() != null ? choice.getIs_correct() : false);
            stmt.setInt(4, choice.getChoice_id());
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

    public List<Choice> findByQuestionId(int questionId) throws SQLException {
        String sql = "SELECT * FROM choices WHERE question_id = ?";
        List<Choice> choices = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Choice choice = new Choice();
                    choice.setChoice_id(rs.getInt("choice_id"));
                    choice.setQuestion_id(rs.getInt("question_id"));
                    choice.setChoice_text(rs.getString("choice_text"));
                    choice.setIs_correct(rs.getBoolean("is_correct"));
                    choices.add(choice);
                }
            }
        }
        return choices;
    }
}