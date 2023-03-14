package ru.practicum.private_access.requests.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.requests.Status.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    User user;
    @ManyToOne
    @JoinColumn(name = "id_event", nullable = false)
    Event event;
    @Enumerated(EnumType.STRING)
    Status status;
    LocalDateTime created;
}
