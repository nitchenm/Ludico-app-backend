package com.acopl.microservice_branch.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.acopl.microservice_branch.controller.BranchControllerV2;
import com.acopl.microservice_branch.dto.BranchDTO;

@Component
public class BranchModelAssembler implements RepresentationModelAssembler<BranchDTO, EntityModel<BranchDTO>> {

    @Override
    @NonNull
    public EntityModel<BranchDTO> toModel(@NonNull BranchDTO branch) {
        return EntityModel.of(branch,
            linkTo(methodOn(BranchControllerV2.class).findById(branch.getId())).withSelfRel(),
            linkTo(methodOn(BranchControllerV2.class).listAllBranches()).withRel("branches")
        );
    }
}