package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
    public int getPerson_id_person() {
        return this.person_id_person;
    }

    public void setPerson_id_person(int person_id_person) {
        this.person_id_person = person_id_person;
    }

    public int getId_offer() {
        return this.id_offer;
    }

    public void setId_offer(int id_offer) {
        this.id_offer = id_offer;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContract_type() {
        return this.contract_type;
    }
    
    public void setContract_type(String contract_type) {
        this.contract_type = contract_type;
    }
 }
}