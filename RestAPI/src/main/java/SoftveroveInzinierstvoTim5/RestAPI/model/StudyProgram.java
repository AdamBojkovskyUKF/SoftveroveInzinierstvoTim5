package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class StudyProgram {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int idstudy_program;
 private String name;

 public StudyProgram() {
    public int getIdStudy_program() {
        return this.idstudy_program;
    }

    public void setIdStudy_program(int idstudy_program) {
        this.idstudy_program = idstudy_program;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
 }
}