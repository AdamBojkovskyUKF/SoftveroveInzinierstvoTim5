package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Communication {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int account_id_account;
 private int account_id_account1;
 private JSON messages;

 public Communication() {
    public int getAccount_id_account() {
        return this.account_id_account;
    }

    public void setAccount_id_account(int account_id_account) {
        this.account_id_account = account_id_account;
    }

    public int getAccount_id_account1() {
        return this.account_id_account1;
    }

    public void setAccount_id_account1(int account_id_account1) {
        this.account_id_account1 = account_id_account1;
    }

    public JSON getMessages() {
        return this.messages;
    }

    public void setMessages(JSON messages) {
        this.messages = messages;
    }
 }
}