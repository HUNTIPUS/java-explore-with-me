package ru.practicum.public_access.comments.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.private_access.events.model.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "id_author", nullable = false)
    User author;
    @ManyToOne
    @JoinColumn(name = "id_event", nullable = false)
    Event event;
    String description;
    LocalDateTime created;
    @Column(name = "edited_on")
    LocalDateTime editedOn;
}
