package ru.clevertec.service;

import ru.clevertec.dto.requestDTO.UserRequestDTO;
import ru.clevertec.dto.responseDTO.UserResponseDTO;

public interface Service {

    UserResponseDTO create(UserRequestDTO userRequestDTO);

    UserResponseDTO findById(Long idUser);

    UserResponseDTO updatePatch(UserRequestDTO userRequestDTO, Long idUser);

    void delete(long idUser);
}
