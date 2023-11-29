package SoftveroveInzinierstvoTim5.RestAPI.repository;

import SoftveroveInzinierstvoTim5.RestAPI.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer>{
    
}