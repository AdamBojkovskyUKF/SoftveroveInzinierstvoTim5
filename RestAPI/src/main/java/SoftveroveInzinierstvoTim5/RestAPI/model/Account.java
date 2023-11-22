package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer person_id_person;

 private int work_offer_id;
 private int company_id_company;
 private int id_account;
 private String email_address;
 private String password;
 private String role;
 private String study_level;
 private String signup_year;

 public Account() {
 }

 //get & set methods
}