package ru.practicum.admin_access.users.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.exceptoin.ObjectExcistenceException;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.admin_access.users.repository.UserRepository;
import ru.practicum.admin_access.users.service.dal.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    @Transactional
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ObjectExcistenceException(String.format("User with id=%s was not found", id)));
        repository.deleteById(id);
    }

    @Override
    public List<User> get(List<Long> ids, Integer from, Integer size) {
        if (ids.isEmpty()) {
            return repository.findAll(PageRequest.of(from > 0 ? from / size : 0,
                    size, Sort.unsorted())).toList();
        } else {
            return repository.findAllById(ids);
        }
    }
}
