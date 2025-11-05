package com.acopl.microservice_sale;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MicroserviceSaleApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring arranca correctamente.
    }

    @Test
    void mainMethodRuns() {
        MicroserviceSaleApplication.main(new String[] {});
        // Si no lanza excepción, el test pasa.
    }
}