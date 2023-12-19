package SoftveroveInzinierstvoTim5.RestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SoftveroveInzinierstvoTim5.RestAPI.model.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer>{
    
}
