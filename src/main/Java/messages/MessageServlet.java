package messages;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/message")
public class MessageServlet extends HttpServlet {

    private final MessageService messageService = new MessageService(); // your service
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MessageDto messageDto = objectMapper.readValue(req.getInputStream(), MessageDto.class);
//        String senderIdParam = req.getSession().getAttribute("userId").toString();
//
//        Integer senderId = Integer.valueOf(senderIdParam);
        Integer senderId = 2;

        messageService.save(messageDto, senderId);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Message saved");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String userId = req.getSession().getAttribute("userId").toString();
//
//        int receiverId = Integer.parseInt(userId);

        int receiverId = 1;

        List<MessageDto> messages = messageService.getAllByReceiverId(receiverId);

        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), messages);
    }
}