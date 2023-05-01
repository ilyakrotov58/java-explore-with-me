package ru.practicum.service.admin.interfaces;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewUserDto;
import ru.practicum.dto.UserDto;

import java.util.List;

public interface IUserAdminService {

    List<UserDto> getUsers(
            @RequestParam int[] ids,
            @RequestParam int from,
            @RequestParam int size);

    UserDto addUser(@RequestBody NewUserDto newUserDto);

    void deleteUser(@PathVariable long userId);
}
