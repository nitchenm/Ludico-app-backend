package com.acopl.microservice_sale.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acopl.microservice_sale.dto.ProductDTO;
import com.acopl.microservice_sale.dto.SaleDTO;
import com.acopl.microservice_sale.service.SaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/sale")
@Tag(name = "Ventas", description = "Operaciones relacionadas a las ventas.")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/create")
    @Operation(summary = "Guardar una nueva venta", description = "Crea una nueva venta con la información proporcionada.")
    @ApiResponse(responseCode = "201", description = "Venta creada exitosamente")
    public ResponseEntity<SaleDTO> saveSale(@RequestBody SaleDTO sale) {
        SaleDTO newSale = saleService.saveSale(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSale);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar venta por ID", description = "Obtiene los detalles de una venta específica por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta encontrada"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public ResponseEntity<SaleDTO> findById(@PathVariable Long id) {
        try {
            SaleDTO saleFound = saleService.findById(id);
            return ResponseEntity.ok(saleFound);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas las ventas", description = "Obtiene una lista de todas las ventas registradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ventas encontrada"),
        @ApiResponse(responseCode = "204", description = "No hay ventas registradas")
    })
    public ResponseEntity<List<SaleDTO>> findAllSales() {
        try {
            List<SaleDTO> saleList = saleService.findAllSales();
            return saleList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(saleList);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta", description = "Elimina una venta existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Venta eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        try {
            saleService.deleteSale(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search-by-id/{id}")
    @Operation(summary = "Buscar ventas por usuario", description = "Obtiene todas las ventas realizadas por un usuario específico.")
    @ApiResponse(responseCode = "200", description = "Ventas encontradas")
    public ResponseEntity<List<SaleDTO>> findAllSaleByUser(@PathVariable Long id) {
        List<SaleDTO> saleList = saleService.findAllSaleByUser(id);
        return ResponseEntity.ok(saleList);
    }

    @GetMapping("/search-products-by-id/{id}")
    @Operation(summary = "Buscar productos por venta", description = "Obtiene los productos asociados a una venta específica.")
    @ApiResponse(responseCode = "200", description = "Productos encontrados")
    public ResponseEntity<ProductDTO> findAllProductsBySale(@PathVariable Long id) {
        ProductDTO productsBySale = saleService.findAllProductsBySale(id);
        return ResponseEntity.ok(productsBySale);
    }
}