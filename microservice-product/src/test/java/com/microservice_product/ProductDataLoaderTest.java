package com.microservice_product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;



import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.microservice_product.model.Product;
import com.microservice_product.repository.ProductRepository;

@SpringBootTest
@ActiveProfiles("test")
public class ProductDataLoaderTest {

   @Mock
   private ProductRepository productRepository;

   @InjectMocks
   private DataLoader dataLoader;

   @Test
   void testRunInsertsProducts () throws Exception{
        MockitoAnnotations.openMocks(this);

        dataLoader.run();

        verify(productRepository, times(3)).save(any(Product.class));

    }


}
