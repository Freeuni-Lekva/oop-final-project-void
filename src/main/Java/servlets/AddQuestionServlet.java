package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import dao.QuestionDAO;
import java.sql.SQLException;

@WebServlet("/addQuestion")
public class AddQuestionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quizIdStr = request.getParameter("quizId");
        String questionType = request.getParameter("questionType");
        int quizId = -1;
        try {
            quizId = Integer.parseInt(quizIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid quiz ID.");
            return;
        }

        // all the possible question choices
        String qrQuestion = request.getParameter("qr-question");
        String qrAnswer = request.getParameter("qr-answer");
        String fbQuestion = request.getParameter("fb-question");
        String fbAnswer = request.getParameter("fb-answer");
        String mcQuestion = request.getParameter("mc-question");
        String mcChoice1 = request.getParameter("mc-choice1");
        String mcChoice2 = request.getParameter("mc-choice2");
        String mcChoice3 = request.getParameter("mc-choice3");
        String mcChoice4 = request.getParameter("mc-choice4");
        String mcCorrect = request.getParameter("mc-correct");
        String prQuestion = request.getParameter("pr-question");
        String prImageUrl = request.getParameter("pr-imageUrl");
        String prAnswer = request.getParameter("pr-answer");

        try {
            int questionId = -1;
            switch (questionType) {
                case "questionResponse":
                    if (qrQuestion != null && qrAnswer != null && !qrQuestion.isEmpty() && !qrAnswer.isEmpty()) {
                        questionId = QuestionDAO.insertQuestionResponse(quizId, qrQuestion, qrAnswer);
                    }
                    break;
                case "fillBlank":
                    if (fbQuestion != null && fbAnswer != null && !fbQuestion.isEmpty() && !fbAnswer.isEmpty()) {
                        questionId = QuestionDAO.insertFillBlank(quizId, fbQuestion, fbAnswer);
                    }
                    break;
                case "multipleChoice":
                    if (mcQuestion != null && mcChoice1 != null && mcChoice2 != null && mcChoice3 != null && mcChoice4 != null && mcCorrect != null
                        && !mcQuestion.isEmpty() && !mcChoice1.isEmpty() && !mcChoice2.isEmpty() && !mcChoice3.isEmpty() && !mcChoice4.isEmpty()) {
                        String[] choices = {mcChoice1, mcChoice2, mcChoice3, mcChoice4};
                        int correctIndex = Integer.parseInt(mcCorrect);
                        questionId = QuestionDAO.insertMultipleChoice(quizId, mcQuestion, choices, correctIndex);
                    }
                    break;
                case "pictureResponse":
                    if (prQuestion != null && prImageUrl != null && prAnswer != null && !prQuestion.isEmpty() && !prImageUrl.isEmpty() && !prAnswer.isEmpty()) {
                        questionId = QuestionDAO.insertPictureResponse(quizId, prQuestion, prImageUrl, prAnswer);
                    }
                    break;
            }
            if (questionId > 0) {
                response.sendRedirect("addQuestion.jsp?quizId=" + quizId);
            } else {
                response.getWriter().write("Error: Question could not be added. Please check your input.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/addQuestion.jsp").forward(request, response);
    }
} 