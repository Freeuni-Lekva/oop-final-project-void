package quiz.TakeQuiz.servlets;

//import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TakeQuizServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object username = (session != null) ? session.getAttribute("userId") : null;

        if (username == null) {
            resp.sendRedirect("../login.jsp");
            return;
        }


        RequestDispatcher dispatcher = req.getRequestDispatcher("/takeQuiz/takeQuiz.html");
        dispatcher.forward(req, resp);
    }
}
