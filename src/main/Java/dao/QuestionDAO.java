package dao;

import resources.DatabaseConnection;

import java.sql.*;

public class QuestionDAO {

    //----------------------------------------------------------------------------------------------------------------------
    public static int insertQuestionResponse(int quizId, String question, String answer) throws SQLException {
        int questionId = insertQuestion(quizId, "question_response", question, null);
        if (questionId > 0) insertQuestionAnswer(questionId, answer);
        return questionId;
    }

    public static int insertFillBlank(int quizId, String question, String answer) throws SQLException {
        int questionId = insertQuestion(quizId, "fill_blank", question, null);
        if (questionId > 0) insertQuestionAnswer(questionId, answer);
        return questionId;
    }

    public static int insertPictureResponse(int quizId, String question, String imageUrl, String answer) throws SQLException {
        int questionId = insertQuestion(quizId, "picture_response", question, imageUrl);
        if (questionId > 0) insertQuestionAnswer(questionId, answer);
        return questionId;
    }

    public static int insertMultipleChoice(int quizId, String question, String[] choices, int correctIndex) throws SQLException {
        int questionId = insertQuestion(quizId, "multiple_choice", question, null);
        if (questionId > 0) insertChoices(questionId, choices, correctIndex);
        return questionId;
    }
    //---------------------------------------------------------------------------------

    private static int insertQuestion(int quizId, String type, String questionText, String imageUrl) throws SQLException {
        String sql = imageUrl == null ?
                "INSERT INTO questions (quiz_id, type, question_text) VALUES (?, ?, ?)" :
                "INSERT INTO questions (quiz_id, type, question_text, image_url) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, quizId);
            stmt.setString(2, type);
            stmt.setString(3, questionText);
            if (imageUrl != null) {
                stmt.setString(4, imageUrl);
            }

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    private static void insertQuestionAnswer(int questionId, String answer) throws SQLException {
        String sql = "INSERT INTO question_answers (question_id, answer_text) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, questionId);
            stmt.setString(2, answer);
            stmt.executeUpdate();
        }
    }

    private static void insertChoices(int questionId, String[] choices, int correctIndex) throws SQLException {
        String sql = "INSERT INTO choices (question_id, choice_text, is_correct) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getDataSource().getConnection()) {
            for (int i = 0; i < choices.length; i++) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, questionId);
                    stmt.setString(2, choices[i]);
                    stmt.setBoolean(3, (i + 1) == correctIndex);
                    stmt.executeUpdate();
                }
            }
        }
    }
}
