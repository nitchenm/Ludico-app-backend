package com.acopl.microservice_sale;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.acopl.microservice_sale.controller.SaleController;
import com.acopl.microservice_sale.dto.ProductDTO;
import com.acopl.microservice_sale.dto.SaleDTO;
import com.acopl.microservice_sale.service.SaleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SaleController.class)
public class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleService saleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveSale() throws Exception {
        SaleDTO sale = new SaleDTO();
        sale.setId(1L);
        sale.setClientID(1L);
        sale.setProductID(2L);
        sale.setTotal(100.0f);
        sale.setDateTime(new Date());

        when(saleService.saveSale(any(SaleDTO.class))).thenReturn(sale);

        mockMvc.perform(post("/api/v1/sale/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sale)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testFindByIdSuccess() throws Exception {
        SaleDTO sale = new SaleDTO();
        sale.setId(1L);
        sale.setClientID(1L);
        sale.setProductID(2L);
        sale.setTotal(100.0f);
        sale.setDateTime(new Date());

        when(saleService.findById(1L)).thenReturn(sale);

        mockMvc.perform(get("/api/v1/sale/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        when(saleService.findById(99L)).thenThrow(new RuntimeException("Sale not found"));

        mockMvc.perform(get("/api/v1/sale/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAllSalesWithResults() throws Exception {
        SaleDTO sale = new SaleDTO();
        sale.setId(1L);
        sale.setClientID(1L);
        sale.setProductID(2L);
        sale.setTotal(100.0f);
        sale.setDateTime(new Date());

        when(saleService.findAllSales()).thenReturn(List.of(sale));

        mockMvc.perform(get("/api/v1/sale"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testFindAllSalesNoContent() throws Exception {
        when(saleService.findAllSales()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/sale"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteSaleSuccess() throws Exception {
        doNothing().when(saleService).deleteSale(1L);

        mockMvc.perform(delete("/api/v1/sale/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteSaleNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Sale not found")).when(saleService).deleteSale(99L);

        mockMvc.perform(delete("/api/v1/sale/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAllSaleByUser() throws Exception {
        SaleDTO sale = new SaleDTO();
        sale.setId(1L);
        sale.setClientID(1L);
        sale.setProductID(2L);
        sale.setTotal(100.0f);
        sale.setDateTime(new Date());

        when(saleService.findAllSaleByUser(1L)).thenReturn(List.of(sale));

        mockMvc.perform(get("/api/v1/sale/search-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientID").value(1L));
    }

    @Test
    void testFindAllProductsBySale() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(2L);
        product.setName("Producto Test");
        product.setPrice(100.0f);
        product.setQuantity(2);

        when(saleService.findAllProductsBySale(1L)).thenReturn(product);

        mockMvc.perform(get("/api/v1/sale/search-products-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void testFindAllSalesThrowsException() throws Exception {
        when(saleService.findAllSales()).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/api/v1/sale"))
                .andExpect(status().isNoContent());
    }
}