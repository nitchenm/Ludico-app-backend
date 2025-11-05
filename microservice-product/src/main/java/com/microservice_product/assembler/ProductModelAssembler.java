package com.microservice_product.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.microservice_product.controller.ProductControllerV2;
import com.microservice_product.dto.ProductDTO;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<ProductDTO, EntityModel<ProductDTO>>{

    @Override
    @NonNull
    public EntityModel<ProductDTO> toModel(@NonNull ProductDTO product){
        return EntityModel.of(product,
                linkTo(methodOn(ProductControllerV2.class).findById(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductControllerV2.class).listAllProducts()).withRel("products")
        );
    }

}
