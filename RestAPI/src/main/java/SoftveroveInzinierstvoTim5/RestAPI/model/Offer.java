package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Offer {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int person_id_person;
 private int id_offer;
 private String position;
 private String description;
 private String contract_type;

 public Offer() {
 }

 //get & set methods
}