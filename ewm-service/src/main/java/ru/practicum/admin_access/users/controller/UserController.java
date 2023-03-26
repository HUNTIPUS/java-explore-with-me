package ru.practicum.admin_access.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin_access.users.dto.UserDto;
import ru.practicum.admin_access.users.service.dal.UserService;
import ru.practicum.valid.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@Validated
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Validated(Create.class) UserDto userDto) {
        log.info("create user");
        return service.create(userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        service.delete(id);
        log.info("delete user with id={}", id);
    }

    @GetMapping
    public List<UserDto> getAll(@RequestParam(required = false) List<Long> ids,
                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("viewing users");
        return service.get(ids, from, size);
    }
}
