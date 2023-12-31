package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Subject_for_Practice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idsubject_for_practice;
    private int study_program_idstudy_program;

    private int credits;
    private String name;


    public Subject_for_Practice() {
    }

    public int getIdsubject_for_practice() {
        return this.idsubject_for_practice;
    }

    public void setIdsubject_for_practice(int idsubject_for_practice) {
        this.idsubject_for_practice = idsubject_for_practice;
    }

    public int getStudy_program_idstudy_program() {
        return this.study_program_idstudy_program;
    }

    public void setStudy_program_idstudy_program(int study_program_idstudy_program) {
        this.study_program_idstudy_program = study_program_idstudy_program;
    }

    public int getCredits() {
        return this.credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
