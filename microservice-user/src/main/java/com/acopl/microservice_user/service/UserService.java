package com.acopl.microservice_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    //obtiene todos los usuarios
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
        dto.setRol(user.getRol());
        // agrega más campos si es necesario
        return dto;
    }

    //obtiene un user por su id
    // DOCUMENTAR POR EL NUEVO DTO
    public UserDTO findById(Long id){

        User userSearched = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));

        UserDTO userFound = new UserDTO();

        userFound.setId(userSearched.getId());
        userFound.setName(userSearched.getName());
        userFound.setEmail(userSearched.getEmail());
        userFound.setRol(userSearched.getRol());

        return userFound;
        //return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
    }

    //guarda un usuario
    public UserDTO saveUser(UserDTO userDTO){
        //¿Esto se podrá mejorar? ¿Manejo de errores?
        //DOCUMENTAR ESTA NUEVA FORMA DE REFACTORIZACION PARA ENTENDERLO
        User newUser = new User();

        newUser.setId(userDTO.getId());
        newUser.setName(userDTO.getName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setRol(userDTO.getRol());

        User savedUser = userRepository.save(newUser);

        UserDTO returningUser = new UserDTO();

        returningUser.setId(savedUser.getId());
        returningUser.setName(savedUser.getName());
        returningUser.setEmail(savedUser.getEmail());
        returningUser.setRol(savedUser.getRol());

        return returningUser;
    }

    //recuerda, es un void porque no necesitamos que retorne algo cuando
    //se elimina
    // Se elimina un usuario por su id
    public void deleteById(Long id){
        //¿Esto se podrá mejorar? ¿Manejo de errores?
        userRepository.deleteById(id);
    }

    //se actualiza usuario
    // se documenta la nuyeva implementación del userdto
    // el userdto es un objeto simplificado que sirve para
    // enviar y recibir datos sin exponer la entidad completa
    public UserDTO updateUser(Long id, UserDTO updatedUser){
        
        User UserToUpdate = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        UserToUpdate.setName(updatedUser.getName());
        UserToUpdate.setEmail(updatedUser.getEmail());
        UserToUpdate.setRol(updatedUser.getRol());

        User savedUser = userRepository.save(UserToUpdate);

        UserDTO returningUser = new UserDTO();

        returningUser.setId(savedUser.getId());
        returningUser.setName(savedUser.getName());
        returningUser.setEmail(savedUser.getEmail());
        returningUser.setRol(savedUser.getRol());

        return returningUser;
    }


    public boolean authenticateById(Long id, String email, String rol) {
        return userRepository.findById(id)
                .map(user ->
                    email != null && rol != null &&
                    java.util.Objects.equals(user.getEmail(), email) &&
                    java.util.Objects.equals(user.getRol(), rol)
                ).orElse(false);
    }


    @SuppressWarnings("unused")
    public List<SaleDTO> findAllSaleByUser(Long id){

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));

        List<SaleDTO> saleDTOList = clientSale.findAllSaleByUser(id);

        return saleDTOList;
    }
}
