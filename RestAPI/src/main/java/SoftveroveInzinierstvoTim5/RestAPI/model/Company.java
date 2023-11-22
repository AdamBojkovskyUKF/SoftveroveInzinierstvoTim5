package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Company {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id_company;
 private int person_id_person;
 private String name;
 private String address;

 public Company() {
 }

 //get & set methods
}