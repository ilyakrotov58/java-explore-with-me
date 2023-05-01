package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewUserDto;
import ru.practicum.dto.UserDto;
import ru.practicum.service.admin.interfaces.IUserAdminService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/users")
@Slf4j
public class UsersAdminController {

    private IUserAdminService userAdminService;

    @Autowired
    public UsersAdminController(IUserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam int[] ids,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return userAdminService.getUsers(ids, from, size);
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody NewUserDto newUserDto) {
        return new ResponseEntity<>(userAdminService.addUser(newUserDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable int userId) {
        userAdminService.deleteUser(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
