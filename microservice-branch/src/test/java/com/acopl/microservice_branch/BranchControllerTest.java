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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.acopl.microservice_branch.controller.BranchController;
import com.acopl.microservice_branch.dto.BranchDTO;
import com.acopl.microservice_branch.service.BranchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BranchController.class)
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BranchService branchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListAllBranches_withBranches() throws Exception {
        BranchDTO branch = new BranchDTO();
        branch.setId(1L);
        branch.setName("Sucursal");
        branch.setAddress("Calle 123");
        branch.setCity("Ciudad");
        branch.setCountry("País");

        when(branchService.findAll()).thenReturn(List.of(branch));

        mockMvc.perform(get("/api/v1/branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Sucursal"))
                .andExpect(jsonPath("$[0].address").value("Calle 123"));
    }

    @Test
    void testListAllBranches_noBranches() throws Exception {
        when(branchService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/branches"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById_found() throws Exception {
        BranchDTO branch = new BranchDTO();
        branch.setId(1L);
        branch.setName("Sucursal");

        when(branchService.findById(1L)).thenReturn(branch);

        mockMvc.perform(get("/api/v1/branches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Sucursal"));
    }

    @Test
    void testFindById_notFound() throws Exception {
        when(branchService.findById(99L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/branches/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveBranch() throws Exception {
        BranchDTO branch = new BranchDTO();
        branch.setId(1L);
        branch.setName("Sucursal");
        branch.setAddress("Calle 123");

        when(branchService.save(any(BranchDTO.class))).thenReturn(branch);

        mockMvc.perform(post("/api/v1/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branch)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Sucursal"));
    }

    @Test
    void testUpdateBranch_found() throws Exception {
        BranchDTO branch = new BranchDTO();
        branch.setId(1L);
        branch.setName("Sucursal Actualizada");

        when(branchService.updateBranch(eq(1L), any(BranchDTO.class))).thenReturn(branch);

        mockMvc.perform(put("/api/v1/branches/1")
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

        mockMvc.perform(put("/api/v1/branches/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branch)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBranch_success() throws Exception {
        doNothing().when(branchService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/branches/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBranch_notFound() throws Exception {
        doThrow(new RuntimeException()).when(branchService).deleteById(99L);

        mockMvc.perform(delete("/api/v1/branches/99"))
                .andExpect(status().isNotFound());
    }
}