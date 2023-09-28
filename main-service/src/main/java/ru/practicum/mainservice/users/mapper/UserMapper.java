package ru.practicum.mainservice.users.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.mainservice.users.dto.NewUserRequest;
import ru.practicum.mainservice.users.dto.UserDto;
import ru.practicum.mainservice.users.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User getEntityFromNewUserDto(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public UserDto getDtoFromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public List<UserDto> getDtoListFromEntityList(List<User> users) {
        return users.stream().map(this::getDtoFromEntity).collect(Collectors.toList());
    }
}
