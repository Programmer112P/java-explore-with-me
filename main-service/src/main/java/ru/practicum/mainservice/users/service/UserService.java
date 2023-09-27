package ru.practicum.mainservice.users.service;

import ru.practicum.mainservice.users.dto.NewUserRequest;
import ru.practicum.mainservice.users.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUser(Long id);

    List<UserDto> getUsers(List<Long> ids, Long offset, Integer limit);

}
