package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Subject_for_Practice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idsubject_for_practice;
    @Id
    private int study_program_idstudy_program;

    private int credits;
    private String name;


    public Subject_for_Practice() {
    }

}
