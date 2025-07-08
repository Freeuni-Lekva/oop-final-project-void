package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import dao.QuizDAO;
import java.sql.SQLException;

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

        try {
            int quizId = QuizDAO.insertQuiz(title, description, creatorId);
            if (quizId > 0) {
                response.sendRedirect("addQuestion.jsp?quizId=" + quizId);
            } else {
                response.getWriter().write("Error: Quiz could not be created.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Database error: " + e.getMessage());
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
