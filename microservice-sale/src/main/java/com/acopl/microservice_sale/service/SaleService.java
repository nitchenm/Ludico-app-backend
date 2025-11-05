package com.acopl.microservice_sale.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acopl.microservice_sale.client.ClientProduct;
import com.acopl.microservice_sale.dto.ProductDTO;
import com.acopl.microservice_sale.dto.SaleDTO;
import com.acopl.microservice_sale.model.Sale;
import com.acopl.microservice_sale.repository.ISaleRepository;

@Service
public class SaleService {

    @Autowired
    private ISaleRepository saleRepository;

    @Autowired
    private ClientProduct clientProduct;

    public List<SaleDTO> findAllSales() {
        try {
            List<Sale> saleList = saleRepository.findAll();
            return saleList.stream()
                    .map(this::mapToSaleDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("No se encontraron ventas: " + e.getMessage());
        }
    }

    public SaleDTO findById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        return mapToSaleDTO(sale);
    }

    public SaleDTO saveSale(SaleDTO saleDTO) {
        Sale newSale = new Sale();
        newSale.setClientId(saleDTO.getClientID());
        newSale.setDateTime(saleDTO.getDateTime());
        newSale.setId(saleDTO.getId());
        newSale.setProductId(saleDTO.getProductID());
        newSale.setTotal(saleDTO.getTotal());

        saleRepository.save(newSale);

        return mapToSaleDTO(newSale);
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    public List<SaleDTO> findAllSaleByUser(Long id) {
        try {
            List<Sale> saleList = saleRepository.findByClientId(id);
            return saleList.stream()
                    .map(this::mapToSaleDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("No se encontraron ventas: " + e.getMessage());
        }
    }

    public ProductDTO findAllProductsBySale(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        return clientProduct.getProductsByIds(saleId);
    }

    private SaleDTO mapToSaleDTO(Sale sale) {
        SaleDTO saleDto = new SaleDTO();
        saleDto.setDateTime(sale.getDateTime());
        saleDto.setId(sale.getId());
        saleDto.setProductID(sale.getProductId());
        saleDto.setTotal(sale.getTotal());
        saleDto.setClientID(sale.getClientId());
        return saleDto;
    }
}