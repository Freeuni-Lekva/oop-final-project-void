package response;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.*;
import java.util.List;

public class ResponseRepository extends AbstractRepository<Response> {
    @Override
    public Response getById(long id) {
        return null;
    }

    @Override
    public List<Response> getAll() {
        return List.of();
    }

    @Override
    public void create(Response response) {
        String sql = "INSERT INTO responses (attempt_id, question_id, response_text) " +
                "VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, response.getAttemptId());
            stmt.setLong(2, response.getQuestionId());
            stmt.setString(3, response.getResponseText());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Inserting response failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    response.setResponseId((int) generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Inserting response failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting response into database", e);
        }
    }


    @Override
    public void update(Response response) {

    }

    @Override
    public void deleteById(long id) {

    }

    public ResponseRepository(BasicDataSource dataSource) {
        super(dataSource);
    }
}
