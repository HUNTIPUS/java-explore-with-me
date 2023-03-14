package ru.practicum.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String app;
    String uri;
    String ip;
    @Column(name = "time_stamp")
    LocalDateTime timestamp;
    @Transient
    Long hits = 0L;
}
