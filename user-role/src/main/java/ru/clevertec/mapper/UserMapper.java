package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.dto.requestDTO.UserRequestDTO;
import ru.clevertec.dto.responseDTO.UserResponseDTO;
import ru.clevertec.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequestDTO userRequestDTO);

    UserResponseDTO toUserResponseDto(User user);
}
