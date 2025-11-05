package com.acopl.microservice_sale;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acopl.microservice_sale.client.ClientProduct;
import com.acopl.microservice_sale.dto.ProductDTO;
import com.acopl.microservice_sale.dto.SaleDTO;
import com.acopl.microservice_sale.model.Sale;
import com.acopl.microservice_sale.repository.ISaleRepository;
import com.acopl.microservice_sale.service.SaleService;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private ISaleRepository saleRepository;

    @Mock
    private ClientProduct clientProduct;

    private Sale sale;
    private SaleDTO saleDTO;

    @BeforeEach
    void setUp() {
        sale = new Sale();
        sale.setId(1L);
        sale.setClientId(1L);
        sale.setProductId(2L);
        sale.setTotal(100.0f);
        sale.setDateTime(new Date());

        saleDTO = new SaleDTO();
        saleDTO.setId(1L);
        saleDTO.setClientID(1L);
        saleDTO.setProductID(2L);
        saleDTO.setTotal(100.0f);
        saleDTO.setDateTime(sale.getDateTime());
    }

    @Test
    void testFindAllSales() {
        when(saleRepository.findAll()).thenReturn(List.of(sale));
        List<SaleDTO> result = saleService.findAllSales();
        assertEquals(1, result.size());
        assertEquals(sale.getId(), result.get(0).getId());
    }

    @Test
    void testFindById() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        SaleDTO result = saleService.findById(1L);
        assertEquals(sale.getId(), result.getId());
        assertEquals(sale.getClientId(), result.getClientID());
    }

    @Test
    void testSaveSale() {
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        SaleDTO result = saleService.saveSale(saleDTO);
        assertEquals(saleDTO.getId(), result.getId());
        assertEquals(saleDTO.getClientID(), result.getClientID());
    }

    @Test
    void testDeleteSale() {
        doNothing().when(saleRepository).deleteById(1L);
        assertDoesNotThrow(() -> saleService.deleteSale(1L));
        verify(saleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllSaleByUser() {
        when(saleRepository.findByClientId(1L)).thenReturn(List.of(sale));
        List<SaleDTO> result = saleService.findAllSaleByUser(1L);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getClientID());
    }

    @Test
    void testFindAllProductsBySale() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(2L);
        productDTO.setName("Producto Test");
        productDTO.setPrice(100.0f);
        productDTO.setQuantity(2);
        when(clientProduct.getProductsByIds(1L)).thenReturn(productDTO);

        ProductDTO result = saleService.findAllProductsBySale(1L);
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Producto Test", result.getName());
    }

    @Test
    void testFindAllSalesThrowsException() {
        when(saleRepository.findAll()).thenThrow(new RuntimeException("DB error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> saleService.findAllSales());
        assertTrue(ex.getMessage().contains("No se encontraron ventas"));
    }

    @Test
    void testFindAllSaleByUserThrowsException() {
        when(saleRepository.findByClientId(1L)).thenThrow(new RuntimeException("DB error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> saleService.findAllSaleByUser(1L));
        assertTrue(ex.getMessage().contains("No se encontraron ventas"));
    }
}