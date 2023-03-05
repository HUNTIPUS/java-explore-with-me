package ru.practicum.admin_access.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin_access.users.dto.UserDto;
import ru.practicum.admin_access.users.mapper.UserMapper;
import ru.practicum.admin_access.users.service.dal.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {

    private final UserService service;

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("create user");
        return UserMapper.toUserDto(service.create(UserMapper.toUser(userDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
        log.info("delete user with id = {}", id);
    }

    @GetMapping
    public List<UserDto> getAll(@RequestParam(required = false) List<Long> ids,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "20") Integer size) {
        log.info("viewing users");
        return UserMapper.toUserDtoList(service.get(ids, from, size));
    }
}
