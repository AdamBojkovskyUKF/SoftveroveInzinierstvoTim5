package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id_account;
 @Id
 private Integer person_id_person;
 @Id
 private int company_id_company;
 @Id
 private int study_program_idstudy_program;


 private String email_address;
 private String password;
 private String role;
 private String study_level;
 private String signup_year;

 public Account() {
 }

 //get & set methods
}