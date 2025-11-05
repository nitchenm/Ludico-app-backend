package com.acopl.microservice_user.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acopl.microservice_user.assembler.UserModelAssembler;
import com.acopl.microservice_user.dto.UserDTO;
import com.acopl.microservice_user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/users")
@Tag(name = "Usuarios HATEOAS", description = "Operaciones HATEOAS para usuarios")
public class UserControllerV2 {

    @Autowired
    private UserService userService;

    @Autowired
    private UserModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios (HATEOAS)", description = "Obtiene una lista de todos los usuarios con enlaces HATEOAS.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados")
    })
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> listAllUsers() {
        List<UserDTO> users = userService.findall();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<UserDTO>> userModels = users.stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(
                CollectionModel.of(userModels,
                        linkTo(methodOn(UserControllerV2.class).listAllUsers()).withSelfRel()
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID (HATEOAS)", description = "Obtiene los detalles de un usuario específico con enlaces HATEOAS.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<UserDTO>> findById(@PathVariable Long id) {
        try {
            UserDTO user = userService.findById(id);
            return ResponseEntity.ok(assembler.toModel(user));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Guardar un nuevo usuario (HATEOAS)", description = "Crea un nuevo usuario y retorna el recurso con enlaces HATEOAS.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    public ResponseEntity<EntityModel<UserDTO>> saveUser(@RequestBody UserDTO user) {
        UserDTO newUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(newUser));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario (HATEOAS)", description = "Actualiza un usuario y retorna el recurso con enlaces HATEOAS.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserDTO updatedUser) {
        try {
            UserDTO userToUpdate = userService.findById(id);
            userToUpdate.setName(updatedUser.getName());
            userToUpdate.setEmail(updatedUser.getEmail());
            userToUpdate.setRol(updatedUser.getRol());
            UserDTO savedUser = userService.saveUser(userToUpdate);
            return ResponseEntity.ok(assembler.toModel(savedUser));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario (HATEOAS)", description = "Elimina un usuario existente por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}