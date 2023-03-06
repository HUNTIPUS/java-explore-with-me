package ru.practicum.private_access.events.location.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "location")
@IdClass(LocationKey.class)
@Getter
@Setter
public class Location {

    @Id
    private Float lat;
    @Id
    private Float lon;
}
