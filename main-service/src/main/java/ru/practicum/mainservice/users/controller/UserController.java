package ru.practicum.mainservice.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.users.dto.NewUserRequest;
import ru.practicum.mainservice.users.dto.UserDto;
import ru.practicum.mainservice.users.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("UserController createUser: request to create new user {}", newUserRequest);
        UserDto response = userService.createUser(newUserRequest);
        log.info("UserController createUser: completed request to create new user {}", newUserRequest);
        return response;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "userId") Long id) {
        log.info("UserController deleteUser: request to delete user {}", id);
        userService.deleteUser(id);
        log.info("UserController deleteUser: completed request to delete user {}", id);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false, defaultValue = "") List<Long> ids,
                                  @RequestParam(name = "from", required = false, defaultValue = "0") Long offset,
                                  @RequestParam(name = "size", required = false, defaultValue = "10") Integer limit) {
        log.info("UserController getUsers: request to get users");
        List<UserDto> response = userService.getUsers(ids, offset, limit);
        log.info("UserController getUsers: completed request to get users");
        return response;
    }
}
