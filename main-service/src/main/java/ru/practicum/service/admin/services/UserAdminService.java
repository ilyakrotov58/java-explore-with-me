package ru.practicum.service.admin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.NewUserDto;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.mappers.UserDtoMapper;
import ru.practicum.exceptions.ConflictDataException;
import ru.practicum.repository.admin.IUserAdminRepository;
import ru.practicum.service.admin.interfaces.IUserAdminService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserAdminService implements IUserAdminService {

    final IUserAdminRepository userPrivateRepository;

    @Autowired
    public UserAdminService(IUserAdminRepository userPrivateRepository) {
        this.userPrivateRepository = userPrivateRepository;
    }

    @Override
    public List<UserDto> getUsers(int[] ids, int from, int size) {
        return userPrivateRepository.getUsers(ids, from, size)
                .stream()
                .map(UserDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(NewUserDto newUserDto) {

        if (userPrivateRepository.findAll()
                .stream()
                .anyMatch(u -> u.getName().equalsIgnoreCase(newUserDto.getName()))) {
            throw new ConflictDataException("User with this name has been already added");
        }

        var user = userPrivateRepository
                .save(UserDtoMapper.fromNewUserDtoToUser(newUserDto));

        return UserDtoMapper.toDto(user);
    }

    @Override
    public void deleteUser(long userId) {
        userPrivateRepository.deleteById(userId);
    }
}
