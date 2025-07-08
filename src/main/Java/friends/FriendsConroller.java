package friends;

import com.google.gson.Gson;
import entities.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/users/friends/*")
public class FriendsConroller extends HttpServlet {
    private final FriendsService friendsService = new FriendsService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;

        String pathInfo = req.getPathInfo();
        if("/lookup".equals(pathInfo)) {
           String search = req.getParameter("search");
           List<UserDto> userDto = friendsService.lookUpPeople(search);

           returnListAsJson(userDto, resp);
       }

       else if(pathInfo==null || req.getPathInfo().equals("/")) {
           Integer user_id = Integer.valueOf(req.getParameter("user_id"));
           List<FriendsDto> friendsDto = friendsService.getAllFriends(user_id);

           returnListAsJson(friendsDto, resp);
       }

       else {
           resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown route");
       }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;

        super.doPut(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;


        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;

        super.doDelete(req, resp);
    }

    private boolean checkIfSessionExists(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect("/login");
            return false;
        }
        return true;
    }


    private <T> void returnListAsJson(List<T> data,HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String json = gson.toJson(data);
        resp.getWriter().write(json);
    }
}
