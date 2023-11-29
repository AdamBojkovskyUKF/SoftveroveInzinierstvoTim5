package SoftveroveInzinierstvoTim5.RestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SoftveroveInzinierstvoTim5.RestAPI.model.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Integer>{
    
}
