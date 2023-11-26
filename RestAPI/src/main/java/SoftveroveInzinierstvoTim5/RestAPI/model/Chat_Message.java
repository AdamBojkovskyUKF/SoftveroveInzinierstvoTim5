package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Chat_Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idchat_message;
    @Id
    private int chat_message_idchat_message;

    private String content;
    private String timestamp;


    public Chat_Message() {
    }

}
