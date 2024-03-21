package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.clientFeign.UserClientFeign;
import ru.clevertec.dto.requestDTO.UserRequestDTO;
import ru.clevertec.dto.responseDTO.UserResponseDTO;
import ru.clevertec.entity.User;
import ru.clevertec.service.UserService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserClientFeign repositoryFeign;

    /**
     * Создание User.
     *
     * @param user user
     * @return UserResponseDTO
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDTO create(User user) {
        return repositoryFeign.create(user).getBody();
    }

    /**
     * Поиск User по его id в базе данных.
     *
     * @param idUser id.
     * @return UserResponseDTO
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDTO findById(Long idUser) {

        return repositoryFeign.findById(idUser).getBody();
    }

    /**
     * Обновление User.
     *
     * @param userRequestDTO userRequestDTO
     * @param idUser         id
     * @return UserResponseDTO
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDTO updatePatch(UserRequestDTO userRequestDTO, Long idUser) {

        return repositoryFeign.updatePatch(userRequestDTO, idUser).getBody();

    }

    /**
     * Поиск User по его email.
     *
     * @param username username.
     * @return Объект User.
     */
    @Override
    public User findByUsername(String username) {

        return repositoryFeign.findByUsername(username).getBody();
    }

    /**
     * Удаление User по его id.
     *
     * @param idUser id.
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(long idUser) {

        repositoryFeign.delete(idUser);
    }
}
