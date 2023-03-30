package ru.practicum.admin_access.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.admin_access.users.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
