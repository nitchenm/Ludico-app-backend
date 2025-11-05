package com.acopl.microservice_branch.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acopl.microservice_branch.dto.BranchDTO;
import com.acopl.microservice_branch.model.Branch;
import com.acopl.microservice_branch.repository.BranchRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    public List<BranchDTO> findAll() {
        return branchRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public BranchDTO findById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        return toDTO(branch);
    }

    public BranchDTO save(BranchDTO branchDTO) {
        Branch branch = toEntity(branchDTO);
        Branch saved = branchRepository.save(branch);
        return toDTO(saved);
    }

    public BranchDTO updateBranch(Long id, BranchDTO updatedDTO) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        branch.setName(updatedDTO.getName());
        branch.setAddress(updatedDTO.getAddress());
        branch.setCity(updatedDTO.getCity());
        branch.setCountry(updatedDTO.getCountry());
        Branch saved = branchRepository.save(branch);
        return toDTO(saved);
    }

    public void deleteById(Long id) {
        if (!branchRepository.existsById(id)) {
            throw new RuntimeException("Branch not found with id: " + id);
        }
        branchRepository.deleteById(id);
    }

    private BranchDTO toDTO(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setAddress(branch.getAddress());
        dto.setCity(branch.getCity());
        dto.setCountry(branch.getCountry());
        return dto;
    }

    private Branch toEntity(BranchDTO dto) {
        Branch branch = new Branch();
        branch.setId(dto.getId());
        branch.setName(dto.getName());
        branch.setAddress(dto.getAddress());
        branch.setCity(dto.getCity());
        branch.setCountry(dto.getCountry());
        return branch;
    }
}