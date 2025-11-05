package com.acopl.microservice_branch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acopl.microservice_branch.dto.BranchDTO;
import com.acopl.microservice_branch.service.BranchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/branches")
@Tag(name = "Sucursales", description = "Operaciones relacionadas con las sucursales")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping
    @Operation(summary = "Listar todas las sucursales", description = "Obtiene una lista de todas las sucursales registradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sucursales encontrada"),
        @ApiResponse(responseCode = "204", description = "No hay sucursales registradas")
    })
    public ResponseEntity<List<BranchDTO>> listAllBranches() {
        List<BranchDTO> branches = branchService.findAll();
        if (branches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(branches);
    }

    @PostMapping
    @Operation(summary = "Guardar una nueva sucursal", description = "Crea una nueva sucursal con la información proporcionada.")
    @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente")
    public ResponseEntity<BranchDTO> saveBranch(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto BranchDTO que representa la sucursal a crear",
            required = true,
            content = @Content(
                schema = @Schema(implementation = BranchDTO.class),
                examples = @ExampleObject(
                    value = """
                    {
                    "name": "Sucursal Centro",
                    "address": "Av. Principal 123",
                    "city": "Santiago",
                    "country": "Chile"
                    }
                    """
                )
            )
        )
        @RequestBody BranchDTO newBranch
    ) {
        BranchDTO branch = branchService.save(newBranch);
        return ResponseEntity.status(HttpStatus.CREATED).body(branch);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sucursal por ID", description = "Obtiene los detalles de una sucursal específica por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<BranchDTO> findById(
        @Parameter(description = "Código de la sucursal", required = true)
        @PathVariable Long id) {
        try {
            BranchDTO branch = branchService.findById(id);
            return ResponseEntity.ok(branch);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sucursal", description = "Actualiza la información de una sucursal existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<BranchDTO> updateBranch(
        @Parameter(description = "Código de la sucursal", required = true)
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto BranchDTO con los datos actualizados",
            required = true,
            content = @Content(
                schema = @Schema(implementation = BranchDTO.class),
                examples = @ExampleObject(
                    value = """
                    {
                    "name": "Sucursal Centro",
                    "address": "Av. Principal 123",
                    "city": "Santiago",
                    "country": "Chile"
                    }
                    """
                )
            )
        )
        @RequestBody BranchDTO branchDTO) {
        try {
            BranchDTO updatedBranch = branchService.updateBranch(id, branchDTO);
            return ResponseEntity.ok(updatedBranch);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sucursal eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<Void> deleteBranch(
        @Parameter(description = "Código de la sucursal", required = true)
        @PathVariable Long id) {
        try {
            branchService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}