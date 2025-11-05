package com.acopl.microservice_swagger_central;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MicroserviceSwaggerCentralApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring arranca correctamente.
    }

    @Test
    void mainMethodRuns() {
        MicroserviceSwaggerCentralApplication.main(new String[] {});
        // Si no lanza excepción, el test pasa.
    }
}