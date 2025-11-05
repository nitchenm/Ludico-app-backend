package com.acopl.microservice_sale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acopl.microservice_sale.model.Sale;


@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {

    
    List<Sale> findByClientId(Long clientId);


    
}
