package QuizFlow.servlets;

import entities.Quiz;
import loginflow.UsersRepository;
import org.apache.commons.dbcp2.BasicDataSource;
import repository.QuizRepository;
import resources.DatabaseConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/myQuizzes")
public class MyQuizzesServlet extends HttpServlet {
    private QuizRepository quizRepository;
    //private UserRepository userRepository;

    @Override
    public void init() {
        System.out.println("AQ MOVEDITTTTTTTTTTTTTTTT");
        BasicDataSource dataSource = DatabaseConnection.getDataSource();
        quizRepository = new QuizRepository(dataSource);
        //userRepository = new UserRepository(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("AQ MOVEDITTTTTTTTTTTTTTTT");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        try {
            String username = (String) request.getSession().getAttribute("username");
            int userId = new UsersRepository(DatabaseConnection.getDataSource()).findByName(username).getUser_id();
            List<Quiz> quizzes = quizRepository.getQuizzesByCreatorId(userId);
            System.out.println("Amdeni qvizia:: " + quizzes.size());
            Map<Long, String> userIdToUsername = new HashMap<>();
            //String username = userRepository.getUsernameById(userId);
            userIdToUsername.put((long) userId, username);
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("userIdToUsername", userIdToUsername);
            request.getRequestDispatcher("/myQuizzes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading your quizzes.");
        }
    }
} 