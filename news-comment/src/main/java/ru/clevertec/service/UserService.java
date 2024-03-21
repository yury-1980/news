package ru.clevertec.service;

import ru.clevertec.dto.requestDTO.UserRequestDTO;
import ru.clevertec.dto.responseDTO.UserResponseDTO;
import ru.clevertec.entity.User;

public interface UserService {

    UserResponseDTO create(User user);

    UserResponseDTO findById(Long idUser);

    UserResponseDTO updatePatch(UserRequestDTO userRequestDTO, Long idUser);

    User findByUsername(String email);

    void delete(long idUser);
}
