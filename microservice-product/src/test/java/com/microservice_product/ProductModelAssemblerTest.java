package com.microservice_product;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import com.microservice_product.assembler.ProductModelAssembler;
import com.microservice_product.dto.ProductDTO;


public class ProductModelAssemblerTest {

    private ProductModelAssembler assembler;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp(){
        assembler = new ProductModelAssembler();
        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Patata");
        productDTO.setQuantity(20);
        productDTO.setPrice(2000);
    }

    @Test
    void testToModel_addsLinks(){
        EntityModel<ProductDTO> model = assembler.toModel(productDTO);

        assertNotNull(model.getContent());
        assertEquals(productDTO, model.getContent());

        Link selfLink = model.getLink("self").orElse(null);
        assertNotNull(selfLink);
        assertTrue(selfLink.getHref().contains("/api/v2/product/1"));

        Link productsLink = model.getLink("products").orElse(null);
        assertNotNull(productsLink);
        assertTrue(productsLink.getHref().contains("/api/v2/product"));
    }

}
