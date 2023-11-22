package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Report {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int account_id_account;
 private int idreport;
 private String type;
 private String content;
 private String timestamp;

 public Report() {
 }

 //get & set methods
}