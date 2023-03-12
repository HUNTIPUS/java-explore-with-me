package ru.practicum.admin_access.users.service.dal;

import ru.practicum.admin_access.users.dto.UserDto;
import ru.practicum.admin_access.users.model.User;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    void delete(Long id);

    List<UserDto> get(List<Long> ids, Integer from, Integer size);

    User getById(Long id);

    List<User> getAll();
}
