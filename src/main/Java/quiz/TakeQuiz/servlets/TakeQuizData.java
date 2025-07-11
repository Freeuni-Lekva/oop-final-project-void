package quiz.TakeQuiz.servlets;

import com.google.gson.Gson;
import quiz.QuizService;
import quiz.TakeQuiz.dtos.TakeQuizDto;
import resources.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class TakeQuizData extends HttpServlet {
    private QuizService quizService;

    @Override
    public void init() throws ServletException {
        quizService= new QuizService(DatabaseConnection.getDataSource());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quiz ID is required.");
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quiz ID must be an integer.");
            return;
        }

        TakeQuizDto quiz = quizService.getFullQuiz(quizId);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        String json = gson.toJson(quiz);

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
