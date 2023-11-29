package SoftveroveInzinierstvoTim5.RestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SoftveroveInzinierstvoTim5.RestAPI.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer>{
    
} 
