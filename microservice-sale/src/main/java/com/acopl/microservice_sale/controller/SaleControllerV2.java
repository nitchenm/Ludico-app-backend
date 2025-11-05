package com.acopl.microservice_sale.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acopl.microservice_sale.assembler.SaleModelAssembler;
import com.acopl.microservice_sale.dto.SaleDTO;
import com.acopl.microservice_sale.service.SaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/sales")
@Tag(name = "Ventas HATEOAS", description = "Operaciones HATEOAS para ventas")
public class SaleControllerV2 {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleModelAssembler assembler;

    @Operation(summary = "Listar todas las ventas", description = "Obtiene una lista de todas las ventas con enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ventas encontrada",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "No hay ventas registradas")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<SaleDTO>>> listAllSales() {
        List<SaleDTO> sales = saleService.findAllSales();
        if (sales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<SaleDTO>> saleModels = sales.stream()
            .map(assembler::toModel)
            .toList();
        return ResponseEntity.ok(
            CollectionModel.of(saleModels,
                linkTo(methodOn(SaleControllerV2.class).listAllSales()).withSelfRel()
            )
        );
    }

    @Operation(summary = "Buscar venta por ID", description = "Obtiene una venta específica por su ID con enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SaleDTO>> findById(
            @Parameter(description = "ID de la venta a buscar") @PathVariable Long id) {
        try {
            SaleDTO sale = saleService.findById(id);
            return ResponseEntity.ok(assembler.toModel(sale));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear una nueva venta", description = "Registra una nueva venta y retorna la entidad creada con enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Venta creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleDTO.class)))
    })
    @PostMapping
    public ResponseEntity<EntityModel<SaleDTO>> saveSale(
            @Parameter(description = "Datos de la nueva venta") @RequestBody SaleDTO newSale) {
        SaleDTO sale = saleService.saveSale(newSale);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(assembler.toModel(sale));
    }

    @Operation(summary = "Eliminar una venta", description = "Elimina una venta por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Venta eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(
            @Parameter(description = "ID de la venta a eliminar") @PathVariable Long id) {
        try {
            saleService.deleteSale(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}