package SoftveroveInzinierstvoTim5.RestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import SoftveroveInzinierstvoTim5.RestAPI.model.Communication;

public interface CommunicationRepository extends JpaRepository<Communication, Integer>{
    
}
