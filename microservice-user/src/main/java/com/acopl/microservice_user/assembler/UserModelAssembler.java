package com.acopl.microservice_user.assembler;

import com.acopl.microservice_user.controller.UserControllerV2;
import com.acopl.microservice_user.dto.UserDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    @Override
    @NonNull
    public EntityModel<UserDTO> toModel(@NonNull UserDTO user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserControllerV2.class).findById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserControllerV2.class).listAllUsers()).withRel("users")
        );
    }
}