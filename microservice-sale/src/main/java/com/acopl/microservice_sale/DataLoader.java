package com.acopl.microservice_sale;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.acopl.microservice_sale.model.Sale;
import com.acopl.microservice_sale.repository.ISaleRepository;

import net.datafaker.Faker;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner{
    @Autowired
    private ISaleRepository saleRepository;

    @Override 
    public void run(String... args) throws Exception{
        Faker faker = new Faker();
        Random random = new Random();
        List<Sale> sales = new ArrayList<>();
        
        for (Long i = (long) 0; i< 3 ; i++){
            Sale sale = new Sale();
            sale.setId(i+1);
            sale.setClientId(i +1);
            sale.setProductId(i+1);
            sale.setTotal(i+200);
            sale.setDateTime(new Date());
            sales.add(sale);
        }
        saleRepository.saveAll(sales);
    }
}
