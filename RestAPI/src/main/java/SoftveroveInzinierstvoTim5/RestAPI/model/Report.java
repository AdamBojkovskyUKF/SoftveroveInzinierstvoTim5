package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Report {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int account_id_account;
 private int idreport;
 private String type;
 private JSON content;
 private String timestamp;

 public Report() {
    public int getAccount_id_account() {
        return account_id_account;
    }

    public void setAccount_id_account(int account_id_account) {
        this.account_id_account = account_id_account;
    }

    public int getIdReport() {
        return this.idreport;
    }

    public void setIdReport(int idreport) {
        this.idreport = idreport;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSON getContent() {
        return this.content;
    }

    public void setContent(JSON content) {
        this.content = content;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimeStamp(String timestamp) {
        this.timestamp = timestamp;
    }
 }
}