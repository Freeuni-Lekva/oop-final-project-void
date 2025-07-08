package servlets;

import dtos.quiz.QuizGetDto;
import repository.QuestionRepository;
import repository.QuizRepository;
import org.apache.commons.dbcp2.BasicDataSource;
import resources.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/finishQuiz")
public class FinishQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quizIdStr = request.getParameter("quizId");
        int quizId = -1;
        try {
            quizId = Integer.parseInt(quizIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid quiz ID.");
            return;
        }

        BasicDataSource dataSource = DatabaseConnection.getDataSource();
        QuizRepository quizRepository = new QuizRepository(dataSource);
        QuizGetDto quiz = quizRepository.getById(quizId);
        if (quiz == null) {
            response.getWriter().write("Quiz not found.");
            return;
        }

        QuestionRepository questionRepository = new QuestionRepository(dataSource);
        int questionCount = questionRepository.countQuestionsForQuiz(quizId);

        if (questionCount == 0) {
            request.setAttribute("errorMessage", "You must add at least one question before finishing your quiz.");
            request.getRequestDispatcher("/addQuestion.jsp?quizId=" + quizId).forward(request, response);
        } else {
            request.getRequestDispatcher("/quizSuccess.jsp").forward(request, response);
        }
    }
} 