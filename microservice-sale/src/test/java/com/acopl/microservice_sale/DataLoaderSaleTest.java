package com.acopl.microservice_sale;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.acopl.microservice_sale.model.Sale;
import com.acopl.microservice_sale.repository.ISaleRepository;

@SpringBootTest
@ActiveProfiles("test")
public class DataLoaderSaleTest {

    @Autowired
    private DataLoader dataLoader;

    @MockBean
    private ISaleRepository saleRepository;

    @Test
    public void testDataLoaderRunSavesSales() throws Exception {
        // Arrange
        ArgumentCaptor<List<Sale>> salesCaptor = ArgumentCaptor.forClass(List.class);

        // Act
        dataLoader.run();

        // Assert
        verify(saleRepository, times(2)).saveAll(salesCaptor.capture());
        List<Sale> savedSales = salesCaptor.getValue();

        assertEquals(3, savedSales.size());

        for (int i = 0; i < 3; i++) {
            Sale sale = savedSales.get(i);
            assertEquals((long) (i + 1), sale.getClientId());
            assertEquals((long) (i + 1), sale.getProductId());
            assertEquals(200.0 + i, sale.getTotal());
            assertNotNull(sale.getDateTime());
            LocalDateTime saleDateTime = sale.getDateTime().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
            assertTrue(saleDateTime.isBefore(LocalDateTime.now().plusSeconds(1)));
        }
    }

    
}
