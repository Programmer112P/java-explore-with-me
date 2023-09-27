package ru.practicum.mainservice.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.pagination.OffsetBasedPageRequest;
import ru.practicum.mainservice.users.dto.NewUserRequest;
import ru.practicum.mainservice.users.dto.UserDto;
import ru.practicum.mainservice.users.mapper.UserMapper;
import ru.practicum.mainservice.users.model.User;
import ru.practicum.mainservice.users.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = userMapper.getEntityFromNewUserDto(newUserRequest);
        try {
            User created = userRepository.save(user);
            return userMapper.getDtoFromEntity(created);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, Long offset, Integer limit) {
        OffsetBasedPageRequest pageable = new OffsetBasedPageRequest(offset, limit);
        List<User> users;
        if (!ids.isEmpty()) {
            users = userRepository.findAllByIdIn(ids, pageable);
        } else {
            users = userRepository.findAll(pageable).toList();
        }
        return userMapper.getDtoListFromEntityList(users);
    }

}
