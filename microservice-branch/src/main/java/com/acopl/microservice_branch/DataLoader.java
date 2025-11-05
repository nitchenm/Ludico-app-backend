package com.acopl.microservice_branch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.acopl.microservice_branch.model.Branch;
import com.acopl.microservice_branch.repository.BranchRepository;

import net.datafaker.Faker;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BranchRepository branchRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        for(Long i = (long) 0; i < 10; i++) {
            Branch branch = new Branch();
            branch.setId(i+1);
            branch.setName(faker.company().name());
            branch.setAddress(faker.address().streetAddress());
            branch.setCity(faker.address().city());
            branch.setCountry(faker.address().country());

            branchRepository.save(branch);
        }
    }
}
