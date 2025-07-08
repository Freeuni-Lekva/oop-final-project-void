package servlets;

import resources.DatabaseConnection;
import utils.PasswordHash;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String hashedPassword = PasswordHash.hashPassword(password);
        try{
            Connection connection = DatabaseConnection.getDataSource().getConnection();
            String query = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                request.getSession().setAttribute("username", username);
                request.getSession().setAttribute("is_admin", resultSet.getBoolean("is_admin"));
                response.sendRedirect("welcome.jsp");
            }else{
                request.setAttribute("error", "Invalid credentials, please try again.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }catch(SQLException e){
            throw new ServletException("Unexpected error",e);
        }
    }
}
