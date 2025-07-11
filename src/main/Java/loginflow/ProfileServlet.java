package loginflow;

import quiz.QuizRepository;
import resources.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/userprofile")
public class ProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchedUsername = request.getParameter("user");
        request.setAttribute("searchedUser", searchedUsername);
        System.out.println(searchedUsername);
        QuizRepository quizRepo = new QuizRepository(DatabaseConnection.getDataSource());
        int takenCount = 0;
        try {
            takenCount = quizRepo.getTakenQuizCount(searchedUsername);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("quizTakenCount", takenCount);
        request.getRequestDispatcher("userprofile.jsp").forward(request, response);
    }
}
