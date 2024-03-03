package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.dto.requestDTO.UserRequestDTO;
import ru.clevertec.dto.responseDTO.UserResponseDTO;
import ru.clevertec.entity.User;
import ru.clevertec.exeption.EntityNotFoundExeption;
import ru.clevertec.mapper.UserMapper;
import ru.clevertec.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements ru.clevertec.service.Service {

    private final UserRepository repository;
    private final UserMapper mapper;

    /**
     * Создание нового User.
     * @param userRequestDTO userRequestDTO
     * @return UserResponseDTO
     */
    @Override
    @Transactional
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {

        return mapper.toUserResponseDto(repository.save(mapper.toUser(userRequestDTO)));
    }

    /**
     * Поиск User по его id в базе данных.
     * @param idUser id.
     * @return UserResponseDTO
     */
    @Override
    public UserResponseDTO findById(Long idUser) {

        return repository.findById(idUser)
                .map(mapper::toUserResponseDto)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));
    }

    /**
     * Обновление User.
     *
     * @param userRequestDTO userRequestDTO
     * @param idUser         id
     * @return UserResponseDTO
     */
    @Override
    @Transactional
    public UserResponseDTO updatePatch(UserRequestDTO userRequestDTO, Long idUser) {
        User user = repository.findById(idUser)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));

        if (userRequestDTO.getUserName() != null) {
            user.setUserName(userRequestDTO.getUserName());
        }

        if (userRequestDTO.getType() != null) {
            user.setRole(userRequestDTO.getType());
        }

        if (userRequestDTO.getPassword() != null) {
            user.setPassword(userRequestDTO.getPassword());
        }

        return mapper.toUserResponseDto(user);
    }

    /**
     * Удаление User по его id.
     *
     * @param idUser id.
     */
    @Override
    @Transactional
    public void delete(long idUser) {
        repository.findById(idUser)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));
        repository.deleteById(idUser);
    }
}
