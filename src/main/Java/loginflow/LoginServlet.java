package loginflow;

import utils.PasswordHash;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String hashedPassword = PasswordHash.hashPassword(password);
        try {
            UsersService loginService = new UsersService();
            if (loginService.login(username, hashedPassword)) {
                request.getSession().setAttribute("username", username);
                request.getSession().setAttribute("is_admin", loginService.isAdmin(username));
                response.sendRedirect("welcome.jsp");
            } else {
                request.setAttribute("error", "Invalid credentials, please try again.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Unexpected error", e);
        }
    }
}
