package com.acopl.microservice_user.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acopl.microservice_user.dto.SaleDTO;
import com.acopl.microservice_user.dto.UserDTO;
import com.acopl.microservice_user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados")
    })
    public ResponseEntity<List<UserDTO>> listAllUsers() {
        List<UserDTO> users = userService.findall();
        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene los detalles de un usuario específico por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UserDTO> findById(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        try {
            UserDTO user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Guardar un nuevo usuario", description = "Crea un nuevo usuario con la información proporcionada.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    public ResponseEntity<UserDTO> saveUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto UserDTO que representa el usuario a crear", required = true, content = @Content(schema = @Schema(implementation = UserDTO.class), examples = @ExampleObject(value = """
                    {
                    "name": "Juan Pérez",
                    "email": "juan@email.com",
                    "rol": "USER"
                    }
                    """))) @RequestBody UserDTO user) {
        UserDTO newUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id,
            @RequestBody UserDTO updatedUser) {
        try {
            UserDTO userToUpdate = userService.findById(id);

            userToUpdate.setName(updatedUser.getName());
            userToUpdate.setEmail(updatedUser.getEmail());
            userToUpdate.setRole(updatedUser.getRole());

            userService.saveUser(userToUpdate);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/authenticate-id")
    @Operation(summary = "Autenticar usuario por ID", description = "Valida las credenciales de un usuario por ID, email y rol.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    public ResponseEntity<String> authenticateById(
            @RequestParam Long id,
            @RequestParam String email,
            @RequestParam String rol) {

        boolean valid = userService.authenticateById(id, email, rol);

        if (valid) {
            return ResponseEntity.ok("ID authentication successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/search-sale-by-id/{id}")
    @Operation(summary = "Buscar ventas por usuario", description = "Obtiene todas las ventas asociadas a un usuario por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ventas encontradas"),
            @ApiResponse(responseCode = "404", description = "Usuario o ventas no encontradas")
    })
    public ResponseEntity<List<SaleDTO>> findAllSaleByUser(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        try {
            List<SaleDTO> saleList = userService.findAllSaleByUser(id);
            return ResponseEntity.ok(saleList);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}