package com.acopl.microservice_branch.controller;

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

import com.acopl.microservice_branch.assembler.BranchModelAssembler;
import com.acopl.microservice_branch.dto.BranchDTO;
import com.acopl.microservice_branch.service.BranchService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/branches")
@Tag(name = "Sucursales HATEOAS", description = "Operaciones HATEOAS para sucursales")
public class BranchControllerV2 {

    @Autowired
    private BranchService branchService;

    @Autowired
    private BranchModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<BranchDTO>>> listAllBranches() {
        List<BranchDTO> branches = branchService.findAll();
        if (branches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<BranchDTO>> branchModels = branches.stream()
            .map(assembler::toModel)
            .toList();
        return ResponseEntity.ok(
            CollectionModel.of(branchModels,
                linkTo(methodOn(BranchControllerV2.class).listAllBranches()).withSelfRel()
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BranchDTO>> findById(@PathVariable Long id) {
        try {
            BranchDTO branch = branchService.findById(id);
            return ResponseEntity.ok(assembler.toModel(branch));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EntityModel<BranchDTO>> saveBranch(@RequestBody BranchDTO newBranch) {
        BranchDTO branch = branchService.save(newBranch);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(assembler.toModel(branch));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<BranchDTO>> updateBranch(@PathVariable Long id, @RequestBody BranchDTO branchDTO) {
        try {
            BranchDTO updatedBranch = branchService.updateBranch(id, branchDTO);
            return ResponseEntity.ok(assembler.toModel(updatedBranch));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        try {
            branchService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}