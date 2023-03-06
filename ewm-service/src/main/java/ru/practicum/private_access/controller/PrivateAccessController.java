package ru.practicum.private_access.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.private_access.requests.service.dal.RequestService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class PrivateAccessController {

    private final RequestService service;
}
