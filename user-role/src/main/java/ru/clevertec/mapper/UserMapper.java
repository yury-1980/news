package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.dto.responseDTO.UserResponseDTO;
import ru.clevertec.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toUserResponseDto(User user);
}
