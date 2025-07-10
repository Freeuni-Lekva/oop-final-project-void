package friendships;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Users;
import friendships.exceptions.FriendRequestNotFoundException;
import friendships.exceptions.FriendshipNotFoundException;
import friendships.exceptions.FriendshipRequestAlreadyExistsException;
import temporary.UserDto;
import temporary.UsersService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/users/friends/*")
public class FriendsServelet extends HttpServlet {
    private final FriendshipService friendsService = new FriendshipService();
    private final UsersService usersService = new UsersService();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //  if (!checkIfSessionExists(req, resp)) return;

        String pathInfo = req.getPathInfo();
        if ("/lookup".equals(pathInfo)) {
            String search = req.getParameter("search");
//            List<UserDto> userDto = usersService.lookUpPeople(search);
              List<Users> userDto = usersService.lookUpPeople(search);

            returnListAsJson(userDto, resp);
        }

        else if ("/requests".equals(pathInfo)) {
            Integer currentUserId = (Integer) req.getSession().getAttribute("user_id");

            List<FriendshipDto> friendRequests = friendsService.getUserFriendRequests(currentUserId);
            returnListAsJson(friendRequests, resp);
        }

        else if (pathInfo == null || req.getPathInfo().equals("/")) {
//            try{
//            Integer currentUserId = (Integer) req.getSession().getAttribute("user_id");
//            }catch (NumberFormatException e){
//                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//            }
            int currentUserId = 1;
            List<FriendshipDto> friendsDto = friendsService.getAcceptedFriends(currentUserId);

            returnListAsJson(friendsDto, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown route");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  //      if (!checkIfSessionExists(req, resp)) return;
//            Integer currentUserId = (Integer) req.getSession().getAttribute("user_id");
        int currentUserId = 2;
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
     //   if (!checkIfSessionExists(req, resp)) return;
        Integer currentUserId = (Integer) req.getSession().getAttribute("user_id");

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
        //if (!checkIfSessionExists(req, resp)) return;
//      Integer currentUserId = (Integer) req.getSession().getAttribute("user_id");
            int currentUserId=1;
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || req.getPathInfo().equals("/")) {
            String friend_name = req.getParameter("friend_name");
            Users friend = usersService.findByName(friend_name);
            Users currentUser = usersService.findById(currentUserId);

            try {
                friendsService.rejectFriendRequest(currentUser, friend);
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
        if (session == null) {
            resp.sendRedirect("/login");
            return false;
        }
        return true;
    }


    private <T> void returnListAsJson(List<T> data, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = objectMapper.writeValueAsString(data);
        resp.getWriter().write(json);
    }
}
