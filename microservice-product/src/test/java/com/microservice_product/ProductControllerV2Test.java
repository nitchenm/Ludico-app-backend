package com.microservice_product;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice_product.assembler.ProductModelAssembler;
import com.microservice_product.controller.ProductControllerV2;
import com.microservice_product.dto.ProductDTO;
import com.microservice_product.service.ProductService;

@WebMvcTest(ProductControllerV2.class)
class ProductControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListAllProducts_withProducts() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Product Test");

        when(productService.findAll()).thenReturn(List.of(product));
        when(assembler.toModel(any(ProductDTO.class))).thenReturn(EntityModel.of(product));

        mockMvc.perform(get("/api/v2/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productDTOList[0]").exists())
                .andExpect(jsonPath("$._embedded.productDTOList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.productDTOList[0].name").value("Product Test"));
    }

    @Test
    void testListAllProducts_noProducts() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/product"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById_found() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Product Test");

        when(productService.findById(1L)).thenReturn(product);
        when(assembler.toModel(any(ProductDTO.class))).thenReturn(EntityModel.of(product));

        mockMvc.perform(get("/api/v2/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testFindById_notFound() throws Exception {
        when(productService.findById(99L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v2/product/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveProduct() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Product Test");

        when(productService.saveProduct(any(ProductDTO.class))).thenReturn(product);
        when(assembler.toModel(any(ProductDTO.class))).thenReturn(EntityModel.of(product));

        mockMvc.perform(post("/api/v2/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateProduct_found() throws Exception {
        ProductDTO existingProduct = new ProductDTO();
        existingProduct.setId(1L);
        existingProduct.setName("Old Product");

        ProductDTO updatedProduct = new ProductDTO();
        updatedProduct.setName("Updated Product");

        when(productService.findById(1L)).thenReturn(existingProduct);
        when(productService.saveProduct(any(ProductDTO.class))).thenReturn(existingProduct);
        when(assembler.toModel(any(ProductDTO.class))).thenReturn(EntityModel.of(existingProduct));

        mockMvc.perform(put("/api/v2/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProduct_notFound() throws Exception {
        ProductDTO product = new ProductDTO();

        when(productService.findById(99L)).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/v2/product/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProduct_success() throws Exception {
        doNothing().when(productService).deleteById(1L);

        mockMvc.perform(delete("/api/v2/product/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProduct_notFound() throws Exception {
        doThrow(new RuntimeException()).when(productService).deleteById(99L);

        mockMvc.perform(delete("/api/v2/product/99"))
                .andExpect(status().isNotFound());
    }
}