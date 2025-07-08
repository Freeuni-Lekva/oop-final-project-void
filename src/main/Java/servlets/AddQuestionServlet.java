package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import dtos.question.QuestionCreateDto;
import org.apache.commons.dbcp2.BasicDataSource;
import repository.QuestionRepository;
import service.QuestionService;
import resources.DatabaseConnection;

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

        String questionOrderStr = request.getParameter("question_order");
        int questionOrder = 1;
        if (questionOrderStr != null) {
            try {
                questionOrder = Integer.parseInt(questionOrderStr);
            } catch (NumberFormatException e) {
                questionOrder = 1;
            }
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


        BasicDataSource dataSource = DatabaseConnection.getDataSource();
        QuestionRepository questionRepository = new QuestionRepository(dataSource);
        QuestionService questionService = new QuestionService(questionRepository);

        boolean success = false;
        // Build the appropriate DTO and call the service
        switch (questionType) {
            case "questionResponse":
                if (qrQuestion != null && qrAnswer != null && !qrQuestion.isEmpty() && !qrAnswer.isEmpty()) {
                    QuestionCreateDto dto = new QuestionCreateDto(quizId, qrQuestion, "question_response", null, questionOrder, qrAnswer, null, -1);
                    questionService.createQuestion(dto);
                    success = true;
                }
                break;
            case "fillBlank":
                if (fbQuestion != null && fbAnswer != null && !fbQuestion.isEmpty() && !fbAnswer.isEmpty()) {
                    QuestionCreateDto dto = new QuestionCreateDto(quizId, fbQuestion, "fill_blank", null, questionOrder, fbAnswer, null, -1);
                    questionService.createQuestion(dto);
                    success = true;
                }
                break;
            case "multipleChoice":
                if (mcQuestion != null && mcChoice1 != null && mcChoice2 != null && mcChoice3 != null && mcChoice4 != null && mcCorrect != null
                    && !mcQuestion.isEmpty() && !mcChoice1.isEmpty() && !mcChoice2.isEmpty() && !mcChoice3.isEmpty() && !mcChoice4.isEmpty()) {
                    int correctIndex = Integer.parseInt(mcCorrect); // 1-based
                    String[] choices = {mcChoice1, mcChoice2, mcChoice3, mcChoice4};
                    QuestionCreateDto dto = new QuestionCreateDto(quizId, mcQuestion, "multiple_choice", null, questionOrder, null, choices, correctIndex);
                    questionService.createQuestion(dto);
                    success = true;
                }
                break;
            case "pictureResponse":
                if (prQuestion != null && prImageUrl != null && prAnswer != null && !prQuestion.isEmpty() && !prImageUrl.isEmpty() && !prAnswer.isEmpty()) {
                    QuestionCreateDto dto = new QuestionCreateDto(quizId, prQuestion, "picture_response", prImageUrl, questionOrder, prAnswer, null, -1);
                    questionService.createQuestion(dto);
                    success = true;
                }
                break;
        }
        if (success) {
            response.sendRedirect("addQuestion.jsp?quizId=" + quizId + "&nextOrder=" + (questionOrder + 1));
        } else {
            request.setAttribute("errorMessage", "Please fill in all required fields before submitting the form.");
            request.getRequestDispatcher("/addQuestion.jsp?quizId=" + quizId + "&nextOrder=" + questionOrder).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/addQuestion.jsp").forward(request, response);
    }
} 