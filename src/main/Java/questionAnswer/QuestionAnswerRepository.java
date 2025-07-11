package questionAnswer;

import org.apache.commons.dbcp2.BasicDataSource;
import repository.AbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionAnswerRepository extends AbstractRepository<QuestionAnswer> {
    public QuestionAnswerRepository(BasicDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public QuestionAnswer getById(long id) {
        return null;
    }

    @Override
    public List<QuestionAnswer> getAll() {
        return List.of();
    }

    @Override
    public void create(QuestionAnswer questionAnswer) {

    }

    @Override
    public void update(QuestionAnswer questionAnswer) {

    }

    @Override
    public void deleteById(long id) {

    }

    public List<QuestionAnswer> getCorrectAnswersByQuestionIds(List<Integer> questionIds) {
        if (questionIds == null || questionIds.isEmpty()) {
            return List.of();
        }

        String placeholders = questionIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        String sql = "SELECT * FROM question_answers WHERE question_id IN (" + placeholders + ")";

        List<QuestionAnswer> answers = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < questionIds.size(); i++) {
                stmt.setInt(i + 1, questionIds.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                QuestionAnswer qa = QuestionAnswer.builder()
                        .answerId(rs.getInt("answer_id"))
                        .questionId(rs.getInt("question_id"))
                        .answerText(rs.getString("answer_text"))
                        .build();

                answers.add(qa);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching correct answers", e);
        }
        return answers;
    }
}
