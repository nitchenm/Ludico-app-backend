package com.acopl.microservice_branch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acopl.microservice_branch.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

}
