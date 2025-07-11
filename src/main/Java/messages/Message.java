package messages;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Message {
    private Integer message_id;
    private Integer sender_id;
    private Integer receiver_id;
    private String type;
    private String content;
    private Integer quiz_id;
    private Timestamp sent_at;

} 