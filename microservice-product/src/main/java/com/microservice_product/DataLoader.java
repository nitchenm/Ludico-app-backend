package com.microservice_product;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.microservice_product.model.Product;
import com.microservice_product.repository.ProductRepository;

import net.datafaker.Faker;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception{
        Faker faker = new Faker();
        //Random random = new Random();

        
        //Generamos la data falsa de product
        for(Long i = (long) 0; i < 3; i++){
            Product product = new Product();
            product.setId(i+1);
            product.setName(faker.name().fullName());
            product.setQuantity(faker.number().numberBetween(1, 500));
            product.setPrice(faker.number().numberBetween(100,500));
            productRepository.save(product);
        }
    }

}
