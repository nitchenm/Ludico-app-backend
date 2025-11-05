package com.acopl.microservice_sale;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.acopl.microservice_sale.assembler.SaleModelAssembler;
import com.acopl.microservice_sale.controller.SaleControllerV2;
import com.acopl.microservice_sale.dto.SaleDTO;
import com.acopl.microservice_sale.service.SaleService;

@WebMvcTest(SaleControllerV2.class)
public class SaleControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleService saleService;

    @MockBean
    private SaleModelAssembler assembler;

    @Test
    void testListAllSales() throws Exception {
        SaleDTO sale = new SaleDTO();
        sale.setId(1L);
        sale.setClientID(1L);
        sale.setProductID(2L);
        sale.setTotal(100.0f);
        sale.setDateTime(new Date());

        when(saleService.findAllSales()).thenReturn(List.of(sale));
        when(assembler.toModel(any(SaleDTO.class))).thenReturn(EntityModel.of(sale));

        // Realiza la petición y muestra el JSON para ver la estructura real
        var result = mockMvc.perform(get("/api/v2/sales"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        // Ajusta el JSONPath según el nombre real del array (probablemente saleDTOList)
        mockMvc.perform(get("/api/v2/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.saleDTOList[0].id").value(1L));
    }

    @Test
    void testFindById() throws Exception {
        SaleDTO sale = new SaleDTO();
        sale.setId(1L);
        sale.setClientID(1L);
        sale.setProductID(2L);
        sale.setTotal(100.0f);
        sale.setDateTime(new Date());

        when(saleService.findById(1L)).thenReturn(sale);
        when(assembler.toModel(any(SaleDTO.class))).thenReturn(EntityModel.of(sale));

        mockMvc.perform(get("/api/v2/sales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testSaveSale() throws Exception {
        SaleDTO sale = new SaleDTO();
        sale.setId(1L);
        sale.setClientID(1L);
        sale.setProductID(2L);
        sale.setTotal(100.0f);
        sale.setDateTime(new Date());

        when(saleService.saveSale(any(SaleDTO.class))).thenReturn(sale);
        when(assembler.toModel(any(SaleDTO.class))).thenReturn(EntityModel.of(sale));

        mockMvc.perform(post("/api/v2/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"clientID\":1,\"productID\":2,\"total\":100.0,\"dateTime\":\"2024-07-01T00:00:00.000+00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeleteSale() throws Exception {
        Mockito.doNothing().when(saleService).deleteSale(1L);

        mockMvc.perform(delete("/api/v2/sales/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListAllSalesNoContent() throws Exception {
        when(saleService.findAllSales()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/sales"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        when(saleService.findById(99L)).thenThrow(new RuntimeException("No encontrada"));

        mockMvc.perform(get("/api/v2/sales/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSaleNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("No encontrada")).when(saleService).deleteSale(99L);

        mockMvc.perform(delete("/api/v2/sales/99"))
                .andExpect(status().isNotFound());
    }
}