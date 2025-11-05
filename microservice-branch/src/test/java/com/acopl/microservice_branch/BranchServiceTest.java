package com.acopl.microservice_branch;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.acopl.microservice_branch.dto.BranchDTO;
import com.acopl.microservice_branch.model.Branch;
import com.acopl.microservice_branch.repository.BranchRepository;
import com.acopl.microservice_branch.service.BranchService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    private Branch branch;

    @BeforeEach
    void setUp() {
        branch = new Branch();
        branch.setId(1L);
        branch.setName("Sucursal");
        branch.setAddress("Calle 123");
        branch.setCity("Ciudad");
        branch.setCountry("País");
    }

    @Test
    void testFindAll() {
        when(branchRepository.findAll()).thenReturn(List.of(branch));
        List<BranchDTO> result = branchService.findAll();
        assertEquals(1, result.size());
        assertEquals(branch.getName(), result.get(0).getName());
    }

    @Test
    void testFindById_found() {
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        BranchDTO result = branchService.findById(1L);
        assertEquals(branch.getName(), result.getName());
    }

    @Test
    void testFindById_notFound() {
        when(branchRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> branchService.findById(2L));
    }

    @Test
    void testSave() {
        when(branchRepository.save(any(Branch.class))).thenReturn(branch);
        BranchDTO dto = new BranchDTO();
        dto.setName("Sucursal");
        dto.setAddress("Calle 123");
        dto.setCity("Ciudad");
        dto.setCountry("País");
        BranchDTO result = branchService.save(dto);
        assertEquals(dto.getName(), result.getName());
    }

    @Test
    void testUpdateBranch_success() {
        BranchDTO dto = new BranchDTO();
        dto.setName("Nueva");
        dto.setAddress("Nueva Dir");
        dto.setCity("Nueva Ciudad");
        dto.setCountry("Nuevo País");

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(branchRepository.save(any(Branch.class))).thenAnswer(inv -> inv.getArgument(0));

        BranchDTO result = branchService.updateBranch(1L, dto);

        assertEquals("Nueva", result.getName());
        assertEquals("Nueva Dir", result.getAddress());
        assertEquals("Nueva Ciudad", result.getCity());
        assertEquals("Nuevo País", result.getCountry());
    }

    @Test
    void testUpdateBranch_notFound() {
        when(branchRepository.findById(2L)).thenReturn(Optional.empty());
        BranchDTO dto = new BranchDTO();
        assertThrows(RuntimeException.class, () -> branchService.updateBranch(2L, dto));
    }

    @Test
    void testDeleteById_success() {
        when(branchRepository.existsById(1L)).thenReturn(true);
        doNothing().when(branchRepository).deleteById(1L);
        branchService.deleteById(1L);
        verify(branchRepository).deleteById(1L);
    }

    @Test
    void testDeleteById_notFound() {
        when(branchRepository.existsById(2L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> branchService.deleteById(2L));
    }
}