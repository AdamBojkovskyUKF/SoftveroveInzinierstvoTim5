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
    
}
