package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

 @Entity
 @EntityListeners(AuditingEntityListener.class)
public class Work {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int offer_id_offer;
 private String contract;
 private String work_log;
 private String state;
 private String feedback_student;
 private String feedback_company;
 private String mark;

 public Work() {
 }
 
 //get & set methods
}