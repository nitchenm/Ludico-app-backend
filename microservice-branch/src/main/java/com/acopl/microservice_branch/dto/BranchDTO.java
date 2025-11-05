package com.acopl.microservice_branch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {

    @Schema(description = "ID de la sucursal", example = "1")
    private Long id;

    @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
    private String name;

    @Schema(description = "Dirección de la sucursal", example = "Av. Principal 123")
    private String address;

    @Schema(description = "Ciudad de la sucursal", example = "Ciudad")
    private String city;

    @Schema(description = "País de la sucursal", example = "País")
    private String country;

}
