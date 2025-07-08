package repository;

import dtos.quiz.QuizCreateDto;
import dtos.quiz.QuizGetDto;
import dtos.quiz.QuizUpdateDto;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuizRepository extends AbstractRepository<QuizGetDto, QuizCreateDto, QuizUpdateDto> {
    public QuizRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int create(QuizCreateDto dto) {
        String sql = "INSERT INTO quizzes (title, description, creator_id, randomize, is_one_page, immediate_correction, practice_mode) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dto.getTitle());
            stmt.setString(2, dto.getDescription());
            stmt.setLong(3, dto.getCreatorId());
            stmt.setBoolean(4, dto.getRandomize() != null ? dto.getRandomize() : false);
            stmt.setBoolean(5, dto.getIsOnePage() != null ? dto.getIsOnePage() : true);
            stmt.setBoolean(6, dto.getImmediateCorrection() != null ? dto.getImmediateCorrection() : false);
            stmt.setBoolean(7, dto.getPracticeMode() != null ? dto.getPracticeMode() : false);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public QuizGetDto getById(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<QuizGetDto> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(QuizUpdateDto entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(int id) {
        throw new UnsupportedOperationException();
    }
} 