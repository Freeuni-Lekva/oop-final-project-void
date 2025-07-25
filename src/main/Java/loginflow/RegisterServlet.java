package loginflow;

import utils.PasswordHash;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String hashedPassword = PasswordHash.hashPassword(password);
        try {
            UsersService registerService = new UsersService();
            boolean exists = registerService.exists(username);
            if (exists) {
                request.setAttribute("error", "Username already exists. Please choose another.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            registerService.register(username, hashedPassword);
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            response.sendRedirect("welcome.jsp");
        } catch (SQLException e) {
            throw new ServletException("Unexpected error", e);
        }
    }
}
