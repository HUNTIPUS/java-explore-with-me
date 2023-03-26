package ru.practicum.admin_access.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.admin_access.users.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
