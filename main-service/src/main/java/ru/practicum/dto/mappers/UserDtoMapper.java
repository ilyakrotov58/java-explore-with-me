package ru.practicum.dto.mappers;

import ru.practicum.dto.NewUserDto;
import ru.practicum.dto.UserDto;
import ru.practicum.model.User;

public class UserDtoMapper {

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User fromNewUserDtoToUser(NewUserDto newUserDto) {
        return new User(
                0,
                newUserDto.getName(),
                newUserDto.getEmail()
        );
    }
}
