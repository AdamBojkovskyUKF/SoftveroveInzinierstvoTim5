package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Communication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int account_id_account;
    @Id
    private int account_id_account1;
    @Id
    private int lastchat_message_idchat_message;


    public Communication() {
    }

}
