package com.acopl.microservice_user.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.acopl.microservice_user.dto.SaleDTO;

@FeignClient(name = "msvc-sale", url = "localhost:9090")
public interface clientSale {

    @GetMapping("api/v1/sale/search-by-id/{id}")
    List<SaleDTO> findAllSaleByUser(@PathVariable Long id);

}
