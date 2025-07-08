package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import dtos.quiz.QuizCreateDto;
import org.apache.commons.dbcp2.BasicDataSource;
import repository.QuizRepository;
import service.QuizService;
import resources.DatabaseConnection;

@WebServlet("/createQuiz")
public class CreateQuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String creatorIdStr = request.getParameter("creator_id");
        if (creatorIdStr == null || creatorIdStr.equals("null")) {
            response.getWriter().write("Error: User is not logged in or userId is missing from session.");
            return;
        }
        long creatorId = Long.parseLong(creatorIdStr);

        // For now, use default values for quiz settings (can be extended to read from form)
        Boolean randomize = false;
        Boolean isOnePage = true;
        Boolean immediateCorrection = false;
        Boolean practiceMode = false;

        QuizCreateDto dto = new QuizCreateDto(title, description, creatorId, randomize, isOnePage, immediateCorrection, practiceMode);
        BasicDataSource dataSource = DatabaseConnection.getDataSource();
        QuizRepository quizRepository = new QuizRepository(dataSource);
        QuizService quizService = new QuizService(quizRepository);
        int quizId = quizService.createQuiz(dto);
        if (quizId > 0) {
            response.sendRedirect("addQuestion.jsp?quizId=" + quizId);
        } else {
            response.getWriter().write("Error: Quiz could not be created.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            request.getSession().setAttribute("userId", 1L); //simulates for now
        }
        request.getRequestDispatcher("/createQuiz.jsp").forward(request, response);
    }
}
