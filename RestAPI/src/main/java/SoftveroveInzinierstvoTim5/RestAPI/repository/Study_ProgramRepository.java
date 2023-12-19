package SoftveroveInzinierstvoTim5.RestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SoftveroveInzinierstvoTim5.RestAPI.model.Study_Program;

@Repository
public interface Study_ProgramRepository extends JpaRepository<Study_Program, Integer>{
    
}
