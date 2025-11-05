package com.acopl.microservice_branch;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.acopl.microservice_branch.model.Branch;
import com.acopl.microservice_branch.repository.BranchRepository;

@ActiveProfiles("test")
class DataLoaderTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private DataLoader dataLoader;

    @Test
    void testRunInsertsBranches() throws Exception {
        MockitoAnnotations.openMocks(this);

        dataLoader.run();

        // Cambia el número según cuántas sucursales inserta tu DataLoader
        verify(branchRepository, times(10)).save(any(Branch.class));
    }
}