package ru.practicum.private_access.events.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.admin_access.categories.model.Category;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.private_access.events.location.model.Location;
import ru.practicum.private_access.events.state.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String annotation;
    String title;
    String description;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @Column(name = "participant_limit")
    Integer participantLimit;
    Boolean paid;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    User user;
    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
    Category category;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "lat"),
            @JoinColumn(name = "lon")
    })
    Location location;
    @Enumerated(EnumType.STRING)
    State state;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
}
