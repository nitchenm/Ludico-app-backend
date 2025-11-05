package com.acopl.microservice_branch;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.acopl.microservice_branch.assembler.BranchModelAssembler;
import com.acopl.microservice_branch.controller.BranchControllerV2;
import com.acopl.microservice_branch.dto.BranchDTO;
import com.acopl.microservice_branch.service.BranchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BranchControllerV2.class)
class BranchControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BranchService branchService;

    @MockBean
    private BranchModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListAllBranches_withBranches() throws Exception {
        BranchDTO branch = new BranchDTO();
        branch.setId(1L);
        branch.setName("Sucursal");

        when(branchService.findAll()).thenReturn(List.of(branch));
        when(assembler.toModel(any(BranchDTO.class))).thenReturn(EntityModel.of(branch));

        mockMvc.perform(get("/api/v2/branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.branchDTOList[0].id").value(1L));
    }

    @Test
    void testListAllBranches_noBranches() throws Exception {
        when(branchService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/branches"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById_found() throws Exception {
        BranchDTO branch = new BranchDTO();
        branch.setId(1L);
        branch.setName("Sucursal");

        when(branchService.findById(1L)).thenReturn(branch);
        when(assembler.toModel(any(BranchDTO.class))).thenReturn(EntityModel.of(branch));

        mockMvc.perform(get("/api/v2/branches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testFindById_notFound() throws Exception {
        when(branchService.findById(99L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v2/branches/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveBranch() throws Exception {
        BranchDTO branch = new BranchDTO();
        branch.setId(1L);
        branch.setName("Sucursal");

        when(branchService.save(any(BranchDTO.class))).thenReturn(branch);
        when(assembler.toModel(any(BranchDTO.class))).thenReturn(EntityModel.of(branch));

        mockMvc.perform(post("/api/v2/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branch)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateBranch_found() throws Exception {
        BranchDTO branch = new BranchDTO();
        branch.setId(1L);
        branch.setName("Sucursal Actualizada");

        when(branchService.updateBranch(eq(1L), any(BranchDTO.class))).thenReturn(branch);
        when(assembler.toModel(any(BranchDTO.class))).thenReturn(EntityModel.of(branch));

        mockMvc.perform(put("/api/v2/branches/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sucursal Actualizada"));
    }

    @Test
    void testUpdateBranch_notFound() throws Exception {
        BranchDTO branch = new BranchDTO();
        
        when(branchService.updateBranch(eq(99L), any(BranchDTO.class)))
                .thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/v2/branches/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branch)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBranch_success() throws Exception {
        doNothing().when(branchService).deleteById(1L);

        mockMvc.perform(delete("/api/v2/branches/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBranch_notFound() throws Exception {
        doThrow(new RuntimeException()).when(branchService).deleteById(99L);

        mockMvc.perform(delete("/api/v2/branches/99"))
                .andExpect(status().isNotFound());
    }
}