package ru.practicum.admin_access.users.mapper;

import ru.practicum.admin_access.users.dto.UserDto;
import ru.practicum.admin_access.users.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto toUserDto(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static List<UserDto> toUserDtoList(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
