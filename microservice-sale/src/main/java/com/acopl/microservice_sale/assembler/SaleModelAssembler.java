package com.acopl.microservice_sale.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.acopl.microservice_sale.controller.SaleControllerV2;
import com.acopl.microservice_sale.dto.SaleDTO;

@Component
public class SaleModelAssembler implements RepresentationModelAssembler<SaleDTO, EntityModel<SaleDTO>>{

    @Override
    @NonNull
    public EntityModel<SaleDTO> toModel(@NonNull SaleDTO branch) {
        return EntityModel.of(branch,
            linkTo(methodOn(SaleControllerV2.class).findById(branch.getId())).withSelfRel(),
            linkTo(methodOn(SaleControllerV2.class).listAllSales()).withRel("branches")
        );
    }
}
