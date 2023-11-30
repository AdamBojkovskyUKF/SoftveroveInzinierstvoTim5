package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Offer {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id_offer;
 private Integer overseer_id_person;
 private int company_id_company;
 private String position;
 private String description;
 private String contract_type;

 public Offer() {
 }
 
    public int getId_offer() {
        return this.id_offer;
    }

    public void setId_offer(int id_offer) {
        this.id_offer = id_offer;
    }

    public Integer getOverseer_id_person() {
        return this.overseer_id_person;
    }

    public void setOverseer_id_person(Integer overseer_id_person) {
        this.overseer_id_person = overseer_id_person;
    }

    public int getCompany_id_company() {
        return this.company_id_company;
    }

    public void setCompany_id_company(int company_id_company) {
        this.company_id_company = company_id_company;
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
