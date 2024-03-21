package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.dto.requestDTO.UserRequestDTO;
import ru.clevertec.dto.responseDTO.UserResponseDTO;
import ru.clevertec.entity.User;
import ru.clevertec.exception.EntityNotFoundException;
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
     *
     * @param user user
     * @return UserResponseDTO
     */
    @Override
    @Transactional
    public UserResponseDTO create(User user) {

        return mapper.toUserResponseDto(repository.save(user));
    }

    /**
     * Поиск User по его id в базе данных.
     *
     * @param idUser id.
     * @return UserResponseDTO
     */
    @Override
    public UserResponseDTO findById(Long idUser) {

        return repository.findById(idUser)
                         .map(mapper::toUserResponseDto)
                         .orElseThrow(() -> EntityNotFoundException.of(Long.class));
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
                              .orElseThrow(() -> EntityNotFoundException.of(Long.class));

        if (userRequestDTO.getUsername() != null) {
            user.setUsername(userRequestDTO.getUsername());
        }

        if (userRequestDTO.getRole() != null) {
            user.setRole(userRequestDTO.getRole());
        }

        if (userRequestDTO.getPassword() != null) {
            user.setPassword(userRequestDTO.getPassword());
        }

        return mapper.toUserResponseDto(user);
    }

    /**
     * Поиск User по его email.
     *
     * @param username username.
     * @return Объект User.
     */
    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username)
                         .orElseThrow(() -> EntityNotFoundException.of(String.class));
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
                  .orElseThrow(() -> EntityNotFoundException.of(Long.class));
        repository.deleteById(idUser);
    }
}
