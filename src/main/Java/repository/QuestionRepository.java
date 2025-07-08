package repository;

import dtos.question.QuestionCreateDto;
import dtos.question.QuestionGetDto;
import dtos.question.QuestionUpdateDto;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestionRepository extends AbstractRepository<QuestionGetDto, QuestionCreateDto, QuestionUpdateDto> {
    public QuestionRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int create(QuestionCreateDto dto) {
        String sql;
        boolean hasImage = dto.getImageUrl() != null && !dto.getImageUrl().isEmpty();
        if (hasImage) {
            sql = "INSERT INTO questions (quiz_id, question_text, type, image_url, question_order) VALUES (?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO questions (quiz_id, question_text, type, question_order) VALUES (?, ?, ?, ?)";
        }
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, dto.getQuizId());
            stmt.setString(2, dto.getQuestionText());
            stmt.setString(3, dto.getType());
            int paramIndex = 4;
            if (hasImage) {
                stmt.setString(4, dto.getImageUrl());
                paramIndex = 5;
            }
            stmt.setInt(paramIndex, dto.getQuestionOrder());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int questionId = rs.getInt(1);
                    // Insert answer or choices depending on type
                    if (dto.getType().equals("question_response") || dto.getType().equals("fill_blank") || dto.getType().equals("picture_response")) {
                        if (dto.getAnswer() != null) {
                            String answerSql = "INSERT INTO question_answers (question_id, answer_text) VALUES (?, ?)";
                            try (PreparedStatement answerStmt = conn.prepareStatement(answerSql)) {
                                answerStmt.setInt(1, questionId);
                                answerStmt.setString(2, dto.getAnswer());
                                answerStmt.executeUpdate();
                            }
                        }
                    } else if (dto.getType().equals("multiple_choice")) {
                        String choiceSql = "INSERT INTO choices (question_id, choice_text, is_correct) VALUES (?, ?, ?)";
                        String[] choices = dto.getChoices();
                        int correctIndex = dto.getCorrectIndex();
                        for (int i = 0; i < choices.length; i++) {
                            try (PreparedStatement choiceStmt = conn.prepareStatement(choiceSql)) {
                                choiceStmt.setInt(1, questionId);
                                choiceStmt.setString(2, choices[i]);
                                choiceStmt.setBoolean(3, (i + 1) == correctIndex);
                                choiceStmt.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public QuestionGetDto getById(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<QuestionGetDto> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(QuestionUpdateDto entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(int id) {
        throw new UnsupportedOperationException();
    }
} 