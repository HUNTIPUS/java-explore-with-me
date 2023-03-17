package ru.practicum.admin_access.compilation_event.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.admin_access.compilations.model.Compilation;
import ru.practicum.private_access.events.model.Event;

import javax.persistence.*;

@Entity
@Table(name = "compilations_events")
@Getter
@Setter
public class CompilationEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_compilation", nullable = false)
    private Compilation compilation;
    @ManyToOne
    @JoinColumn(name = "id_event", nullable = false)
    private Event event;
}
