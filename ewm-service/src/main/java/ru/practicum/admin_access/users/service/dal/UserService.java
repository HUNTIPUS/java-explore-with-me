package ru.practicum.admin_access.users.service.dal;

import ru.practicum.admin_access.users.model.User;

import java.util.List;

public interface UserService {

    User create(User user);
    void delete(Long id);
    List<User> get(List<Long> ids, Integer from, Integer size);

}
