package ru.practicum.admin_access.users.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin_access.users.dto.UserDto;
import ru.practicum.admin_access.users.mapper.UserMapper;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.admin_access.users.repository.UserRepository;
import ru.practicum.admin_access.users.service.dal.UserService;
import ru.practicum.exceptions.exception.ObjectExistenceException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(userDto)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getById(id);
        repository.deleteById(id);
    }

    @Override
    public List<UserDto> get(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            return UserMapper.toUserDtoList(repository.findAll(PageRequest.of(from > 0 ? from / size : 0,
                    size)).toList());
        } else {
            return UserMapper.toUserDtoList(repository.findAllById(ids));
        }
    }

    @Override
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectExistenceException(String.format("User with id=%s was not found", id)));
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }
}
