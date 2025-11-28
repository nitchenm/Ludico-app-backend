package com.acopl.microservice_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acopl.microservice_user.client.clientSale;
import com.acopl.microservice_user.dto.SaleDTO;
import com.acopl.microservice_user.dto.UserDTO;
import com.acopl.microservice_user.model.User;
import com.acopl.microservice_user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private clientSale clientSale;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // obtiene todos los usuarios
    public List<UserDTO> findall() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDTO).toList();
    }

    // Método auxiliar para convertir User a UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        // agrega más campos si es necesario
        return dto;
    }

    // obtiene un user por su id
    // DOCUMENTAR POR EL NUEVO DTO
    public UserDTO findById(Long id) {

        User userSearched = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO userFound = convertToDTO(userSearched);

        return userFound;
        // return userRepository.findById(id).orElseThrow(()-> new
        // RuntimeException("User not found"));
    }

    public UserDTO saveUser(UserDTO userDTO) {
        if (userRepository.existsByName(userDTO.getName())) {
            throw new RuntimeException("Username already exists.");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole("USER"); // default role

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public void deleteById(Long id) {

        // ¿Esto se podrá mejorar? ¿Manejo de errores?
        try {
            userRepository.deleteById(id);

        } catch (Exception e) {

        }
    }

    // se actualiza usuario
    // se documenta la nuyeva implementación del userdto
    // el userdto es un objeto simplificado que sirve para
    // enviar y recibir datos sin exponer la entidad completa
    public UserDTO updateUser(Long id, UserDTO updatedUser) {

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        userToUpdate.setName(updatedUser.getName());
        userToUpdate.setEmail(updatedUser.getEmail());
        userToUpdate.setRole(updatedUser.getRole());

        User savedUser = userRepository.save(userToUpdate);

        UserDTO returningUser = new UserDTO();

        returningUser.setId(savedUser.getId());
        returningUser.setName(savedUser.getName());
        returningUser.setEmail(savedUser.getEmail());
        returningUser.setRole(savedUser.getRole());

        return returningUser;
    }

    public boolean authenticateById(Long id, String email, String role) {
        return userRepository.findById(id)
                .map(user -> email != null && role != null &&
                        java.util.Objects.equals(user.getEmail(), email) &&
                        java.util.Objects.equals(user.getRole(), role))
                .orElse(false);
    }

    @SuppressWarnings("unused")
    public List<SaleDTO> findAllSaleByUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));

        List<SaleDTO> saleDTOList = clientSale.findAllSaleByUser(id);

        return saleDTOList;
    }
}
