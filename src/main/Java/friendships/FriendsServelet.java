package friendships;

import com.fasterxml.jackson.databind.ObjectMapper;
import friendships.exceptions.UserNotFoundException;
import loginflow.UserDto;
import loginflow.Users;
import friendships.exceptions.FriendRequestNotFoundException;
import friendships.exceptions.FriendshipNotFoundException;
import friendships.exceptions.FriendshipRequestAlreadyExistsException;
import loginflow.UsersService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/users/friends/*")
public class FriendsServelet extends HttpServlet {
    private final FriendshipService friendsService = new FriendshipService();
    private final UsersService usersService = new UsersService();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;
        String currentUsersName = (String) req.getSession().getAttribute("username");
        Integer currentUserId = usersService.findByName(currentUsersName).getUser_id();

        String pathInfo = req.getPathInfo();
        if ("/lookup".equals(pathInfo)) {
            String search = req.getParameter("search");
            List<Users> users = usersService.lookUpPeople(search);
            List<UserDto> names = users.stream().map(user -> new UserDto(user.getUsername())).collect(Collectors.toList());
            returnListAsJson(users, resp);
        }

        else if("/info".equals(pathInfo)) {
            String friendName = req.getParameter("friend_name");
            if (friendName == null || friendName.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing friend_name parameter");
                return;
            }

            Integer friendsUserId = usersService.findByName(friendName).getUser_id();
            if (friendsUserId == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid friend_name parameter");
            }

            FriendshipDto status = friendsService.getUserFriendRequest(currentUserId, friendsUserId);
            returnListAsJson(status,resp);
        }

        else if ("/requests".equals(pathInfo)) {

            List<FriendshipDto> friendRequests = friendsService.getUserFriendRequests(currentUserId);
            returnListAsJson(friendRequests, resp);
        }

        else if (pathInfo == null || req.getPathInfo().equals("/")) {
            List<FriendshipDto> friendsDto = friendsService.getAcceptedFriends(currentUserId);

            returnListAsJson(friendsDto, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown route");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;
        String currentUsersName = (String) req.getSession().getAttribute("username");
        Integer currentUserId = usersService.findByName(currentUsersName).getUser_id();

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || req.getPathInfo().equals("/")) {
            String friend_name = req.getParameter("friend_name");
            Users friend = usersService.findByName(friend_name);
            Users currentUser = usersService.findById(currentUserId);

            try {
                friendsService.sendFriendRequest(currentUser, friend);
            } catch (FriendshipRequestAlreadyExistsException e) {
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
        String currentUsersName = (String) req.getSession().getAttribute("username");
        Integer currentUserId = usersService.findByName(currentUsersName).getUser_id();

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || req.getPathInfo().equals("/")) {
            String friend_name = req.getParameter("friend_name");
            Users friend = usersService.findByName(friend_name);
            Users currentUser = usersService.findById(currentUserId);

            String state= req.getParameter("state");

            if("true".equals(state)) {
                try {
                    friendsService.acceptFriendRequest(currentUser, friend);
                } catch (FriendRequestNotFoundException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                }
            }else if("false".equals(state)) {
                try {
                    friendsService.rejectFriendRequest(currentUser, friend);
                } catch (FriendRequestNotFoundException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                }
            }
            resp.getWriter().write("Updating Friend Request from " + friend_name + " Was Successful");
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown route");

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkIfSessionExists(req, resp)) return;
        String currentUsersName = (String) req.getSession().getAttribute("username");
        Integer currentUserId = usersService.findByName(currentUsersName).getUser_id();

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || req.getPathInfo().equals("/")) {
            String friend_name = req.getParameter("friend_name");
            Users friend = usersService.findByName(friend_name);
            Users currentUser = usersService.findById(currentUserId);

            try {
                friendsService.unfriendUser(currentUser, friend);
            } catch (FriendRequestNotFoundException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }

            resp.getWriter().write("Friend Delete Request Towards " + friend_name + " Was Successful");
            return;
        } else if ("/unfriend".equals(pathInfo)) {
            String friend_name = req.getParameter("friend_name");
            Users friend = usersService.findByName(friend_name);
            Users currentUser = usersService.findById(currentUserId);

            try {
                friendsService.unfriendUser(currentUser, friend);
            } catch (FriendshipNotFoundException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }

            resp.getWriter().write("Unfriend Request To " + friend_name + " Was Successful");
            return;
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown route");

    }

    private boolean checkIfSessionExists(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        String currentUsersName = (String) req.getSession().getAttribute("username");
        if (session == null || currentUsersName==null) {
            resp.sendRedirect(req.getContextPath()+ "/login.jsp");
            return false;
        }
        return true;
    }


    private <T> void returnListAsJson(T data, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = objectMapper.writeValueAsString(data);
        resp.getWriter().write(json);
    }
}
