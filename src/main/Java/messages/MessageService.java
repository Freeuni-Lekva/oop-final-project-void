package messages;

import friendships.Friendship;
import friendships.FriendshipDto;
import friendships.FriendshipRepository;
import friendships.exceptions.UserNotFoundException;
import loginflow.Users;
import loginflow.UsersService;
import resources.DatabaseConnection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService {
    private final MessageRepository messageRepository = new MessageRepository(DatabaseConnection.getDataSource());
    private final UsersService usersService = new UsersService();


    public List<MessageDto> getAllByReceiverId(int receiverId) {
        List<Message> messages = messageRepository.getAllByReceiverId(receiverId);
        messages.sort(Comparator.comparing(Message::getSent_at));

        List<MessageDto> messageDtos = messages.stream()
                .map(message -> {
                    String senderUsername = usersService.findById(message.getSender_id()).getUsername(); // You must have this method
                    return new MessageDto(
                            senderUsername,
                            message.getType(),
                            message.getContent(),
                            message.getSent_at()
                    );
                })
                .toList();

        return  messageDtos;
    }

    public void save(MessageDto messageDto, Integer senderId) {
        Users reciverUser = usersService.findByName(messageDto.getSenderUsername());

        if(reciverUser == null) {
            throw new UserNotFoundException("User not found");
        }

        Message message = Message.builder()
                .sender_id(senderId)
                .receiver_id(reciverUser.getUser_id())
                .type(messageDto.getType())
                .content(messageDto.getContent())
                .build();

        messageRepository.create(message);
    }

}
