package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)

 private int person_id_person;
 private int company_id_company;
 private int study_program_idstudy_program;
 private int id_account;
 private String email_address;
 private String password;
 private String role;
 private String study_level;
 private String signup_year;
 private String institute;

 public Account() {
    public int getPerson_id_person() {
        return this.person_id_person;
    }

    public void setPerson_id_person(int person_id_person) {
        this.person_id_person = person_id_person;
    }

    public int getCompany_id_company() {
        return this.company_id_company;
    }

    public void setCompany_id_company(int company_id_company) {
        this.company_id_company = company_id_company;
    }

    public int getStudy_program_idstudy_program() {
        return this.study_program_idstudy_program;
    }

    public void setStudy_program_idstudy_program(int study_program_idstudy_program) {
        this.study_program_idstudy_program = study_program_idstudy_program;
    }

    public int getId_account() {
        return this.id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public String getEmail_address() {
        return this.email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStudyLevel() {
        return this.study_level;
    }

    public void setStudyLevel() {
        this.study_level = study_level;
    }

    public String getSignupYear() {
        return this.signup_year;
    }

    public void setSignupYear(String signup_year) {
        this.signup_year = signup_year;
    }

    public String getInstitute() {
        return this.institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }
 }
}