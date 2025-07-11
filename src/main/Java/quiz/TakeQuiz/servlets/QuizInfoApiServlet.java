package quiz.TakeQuiz.servlets;

import com.google.gson.Gson;
import quiz.Quiz;
import quiz.QuizRepository;
import quiz.QuizService;
import resources.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class QuizInfoApiServlet extends HttpServlet {
    private QuizRepository quizRepo;

    @Override
    public void init() throws ServletException {
        quizRepo = new QuizRepository(DatabaseConnection.getDataSource());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quiz ID is missing.");
            return;
        }
        int quizId;
        try {
            quizId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz ID.");
            return;
        }
        Quiz quiz = quizRepo.getById(quizId);
        if (quiz == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found.");
            return;
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        new Gson().toJson(quiz, resp.getWriter());
    }
}
