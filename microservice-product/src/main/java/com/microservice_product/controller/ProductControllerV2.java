package com.microservice_product.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice_product.assembler.ProductModelAssembler;
import com.microservice_product.dto.ProductDTO;
import com.microservice_product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v2/product")
@Tag(name = "Productos HATEOAS", description = "Operaciones HATEOAS para productos")
public class ProductControllerV2 {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar todos los productos (HATEOAS)", description = "Obtiene una lista de todos los productos con enlaces HATEOAS.")
     @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductDTO>>> listAllProducts(){
        List<ProductDTO> products = productService.findAll();
        if(products.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<ProductDTO>> productModels = products.stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(
                CollectionModel.of(productModels,
                    linkTo(methodOn(ProductControllerV2.class).listAllProducts()).withSelfRel()
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID (HATEOAS)", description = "Obtiene los detalles de un producto específico con enlaces HATEOAS.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<EntityModel<ProductDTO>> findById(@PathVariable Long id){
        try {
            ProductDTO product = productService.findById(id);
            return ResponseEntity.ok(assembler.toModel(product));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Guardar un nuevo producto (HATEOAS)", description = "Crea un nuevo usuario y retorna el recurso con enlaces HATEOAS.")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    public ResponseEntity<EntityModel<ProductDTO>> saveProduct(@RequestBody ProductDTO product){
        ProductDTO newProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(newProduct));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar productos (HATEOAS)", description = "Actualiza un producto y retorna el recurso con enlaces HATEOAS.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<EntityModel<ProductDTO>> updateProduct(@PathVariable Long id, @RequestBody ProductDTO updateProduct){
        try {
            ProductDTO productToUpdate = productService.findById(id);
            productToUpdate.setName(updateProduct.getName());
            productToUpdate.setQuantity(updateProduct.getQuantity());
            productToUpdate.setPrice(updateProduct.getPrice());
            ProductDTO savedProduct = productService.saveProduct(productToUpdate);
            return ResponseEntity.ok(assembler.toModel(savedProduct));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (HATEOAS)", description = "Elimina un producto existente por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        try {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
