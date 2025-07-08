package friends;

import com.google.gson.Gson;
import entities.Users;
import friends.exceptions.AlreadyFriendsException;
import friends.exceptions.FriendNotFoundException;
import friends.exceptions.FriendRequestNotFoundException;

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
    private final UsersService usersService = new UsersService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;

        String pathInfo = req.getPathInfo();
        if ("/lookup".equals(pathInfo)) {
            String search = req.getParameter("search");
            List<UserDto> userDto = usersService.lookUpPeople(search);

            returnListAsJson(userDto, resp);
        } else if (pathInfo == null || req.getPathInfo().equals("/")) {
            Integer user_id = Integer.valueOf(req.getParameter("user_id"));
            List<FriendsDto> friendsDto = friendsService.getAllFriends(user_id);

            returnListAsJson(friendsDto, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown route");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;
        Integer currentUserId = Integer.valueOf(req.getParameter("user_id"));

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || req.getPathInfo().equals("/")) {
            String friend_name = req.getParameter("friend_name");
            Users friend = usersService.findByName(friend_name);
            Users currentUser = usersService.findById(currentUserId);

            try {
                friendsService.sendFriendRequest(currentUser, friend);
            } catch (AlreadyFriendsException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }

            resp.getWriter().write("Sending Friend Request To " + friend_name + " Was Successful");
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown route");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;
        Integer currentUserId = Integer.valueOf(req.getParameter("user_id"));

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || req.getPathInfo().equals("/")) {
            String friend_name = req.getParameter("friend_name");
            Users friend = usersService.findByName(friend_name);
            Users currentUser = usersService.findById(currentUserId);

            try{
            friendsService.acceptFriendRequest(currentUser, friend);
            }catch (FriendRequestNotFoundException e){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }

            resp.getWriter().write("Accepting Friend Request from " + friend_name + " Was Successful");
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown route");

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;
        Integer currentUserId = Integer.valueOf(req.getParameter("user_id"));

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || req.getPathInfo().equals("/")) {
            String friend_name = req.getParameter("friend_name");
            Users friend = usersService.findByName(friend_name);
            Users currentUser = usersService.findById(currentUserId);

            try{
                friendsService.rejectFriendRequest(currentUser, friend);
            }catch (FriendRequestNotFoundException e){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }

            resp.getWriter().write("Friend Request Reject To " + friend_name + " Was Successful");
            return;
        }

        else if("/unfriend".equals(pathInfo)) {
            String friend_name = req.getParameter("friend_name");
            Users friend = usersService.findByName(friend_name);
            Users currentUser = usersService.findById(currentUserId);

            try{
                friendsService.unfriendUser(currentUser, friend);
            }catch (FriendNotFoundException e){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }

            resp.getWriter().write("Unfriend Request To " + friend_name + " Was Successful");
            return;
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown route");

    }

    private boolean checkIfSessionExists(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect("/login");
            return false;
        }
        return true;
    }


    private <T> void returnListAsJson(List<T> data, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String json = gson.toJson(data);
        resp.getWriter().write(json);
    }
}
