package SoftveroveInzinierstvoTim5.RestAPI.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Study_Program {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idstudy_program;
    
    private String name;

    public Study_Program() {
    }

    public int getIdstudy_program() {
        return this.idstudy_program;
    }

    public void setIdstudy_program(int idstudy_program) {
        this.idstudy_program = idstudy_program;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
