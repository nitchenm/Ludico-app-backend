package com.acopl.microservice_event.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.acopl.microservice_event.dto.UserDTO;

@FeignClient(name = "microservice-user", url = "http://localhost:8050")
public interface clientUser {

    @GetMapping("/api/v1/users/{id}")
    UserDTO getUserById(@PathVariable Long id);

}
