package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

 @Entity
 @EntityListeners(AuditingEntityListener.class)
public class Work {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int offer_id_offer;
 private int account_id_account;
 private String contract;
 private String work_log;
 private String state;
 private String feedback_student;
 private String feedback_company;
 private String mark;
 private String completion_year;

 public Work() {
    public int getOffer_id_offer() {
        return this.offer_id_offer;
    }

    public void setOffer_id_offer(int offer_id_offer) {
        this.offer_id_offer = offer_id_offer;
    }

    public int getAccount_id_account() {
        return this.account_id_account;
    }

    public void setAccount_id_account(int account_id_account) {
        this.account_id_account = account_id_account;
    }

    public String getContract() {
        return this.contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getWorkLog() {
        return this.work_log;
    }

    public void setWorkLog(String work_log) {
        this.work_log = work_log;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFeedbackStudent() {
        return this.feedback_student;
    }

    public void setFeedbackStudent(String feedback_student) {
        this.feedback_student = feedback_student;
    }

    public String getFeedbackCompany() {
        return this.feedback_company;
    }

    public void setFeedbackCompany(String feedback_company) {
        this.feedback_company = feedback_company;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCompletionYear() {
        return this.completion_year;
    }

    public void setCompletionYear(String completion_year) {
        this.completion_year = completion_year;
    }
 }
}