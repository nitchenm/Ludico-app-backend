package com.acopl.microservice_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acopl.microservice_user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
