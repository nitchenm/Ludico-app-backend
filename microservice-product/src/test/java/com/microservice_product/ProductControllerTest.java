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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice_product.controller.ProductController;
import com.microservice_product.dto.ProductDTO;
import com.microservice_product.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFindAll_withProducts() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Product Test");
        product.setPrice(100.0f);

        when(productService.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Product Test"));
    }

    @Test
    void testFindAll_noProducts() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById_found() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Product Test");

        when(productService.findById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/v1/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testFindById_notFound() throws Exception {
        when(productService.findById(99L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/product/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveProduct() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Product Test");

        when(productService.saveProduct(any(ProductDTO.class))).thenReturn(product);

        mockMvc.perform(post("/api/v1/product")
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

        mockMvc.perform(put("/api/v1/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProduct_notFound() throws Exception {
        ProductDTO product = new ProductDTO();

        when(productService.findById(99L)).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/v1/product/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteByID_success() throws Exception {
        doNothing().when(productService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/product/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteByID_notFound() throws Exception {
        doThrow(new RuntimeException()).when(productService).deleteById(99L);

        mockMvc.perform(delete("/api/v1/product/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStockById_found() throws Exception {
        when(productService.getStockById(1L)).thenReturn(10);

        mockMvc.perform(get("/api/v1/product/1/stock"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void testGetStockById_notFound() throws Exception {
        when(productService.getStockById(99L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/product/99/stock"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductsByIds() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Product Test");

        when(productService.getProductsByIds(1L)).thenReturn(product);

        mockMvc.perform(get("/api/v1/product/Search-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}