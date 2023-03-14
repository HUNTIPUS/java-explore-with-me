package ru.practicum.admin_access.compilations.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin_access.compilations.dto.CompilationDtoInput;
import ru.practicum.admin_access.compilations.dto.CompilationDtoOutput;
import ru.practicum.admin_access.compilations.mapper.CompilationMapper;
import ru.practicum.admin_access.compilations.model.Compilation;
import ru.practicum.admin_access.compilations.repository.CompilationRepository;
import ru.practicum.admin_access.compilations.service.dal.CompilationService;
import ru.practicum.exceptions.exception.ObjectExistenceException;
import ru.practicum.private_access.events.mapper.EventMapper;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDtoOutput create(CompilationDtoInput compilationDtoInput) {
        Compilation compilation = compilationRepository
                .save(CompilationMapper.toCompilation(compilationDtoInput));
        CompilationDtoOutput compilationDtoOutput = CompilationMapper.toCompilationDtoOutput(compilation);
        if (compilationDtoInput.getEvents() != null && !compilationDtoInput.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(compilationDtoInput.getEvents());
            for (Event event : events) {
                event.setCompilation(compilation);
            }
            compilationDtoOutput.setEvents(EventMapper.toEventShortDtoOutputList(events));
        }
        return compilationDtoOutput;
    }

    @Override
    public CompilationDtoOutput update(Long id, CompilationDtoInput compilationDtoInput) {
        Compilation oldCompilation = compilationRepository.findById(id)
                .orElseThrow(() -> new ObjectExistenceException(String
                        .format("Compilation with id=%s was not found", id)));
        Compilation compilation = updateCompilation(oldCompilation,
                CompilationMapper.toCompilation(compilationDtoInput));
        CompilationDtoOutput compilationDtoOutput = CompilationMapper.toCompilationDtoOutput(compilation);
        if (!compilationDtoInput.getEvents().isEmpty()) {
            List<Event> oldEvents = eventRepository.getByCompilation(id);
            List<Event> newEvents = eventRepository.findAllById(compilationDtoInput.getEvents());
            if (!oldEvents.isEmpty()) {
                for (Event event : oldEvents) {
                    event.setCompilation(null);
                }
            } else {
                for (Event event : newEvents) {
                    event.setCompilation(compilation);
                }
            }
            compilationDtoOutput.setEvents(EventMapper.toEventShortDtoOutputList(newEvents));
        }
        return compilationDtoOutput;
    }

    @Override
    public void delete(Long id) {
        getById(id);
        List<Event> events = eventRepository.getByCompilation(id);
        if (!events.isEmpty()) {
            for (Event event : events) {
                event.setCompilation(null);
            }
        }
        compilationRepository.deleteById(id);
    }

    @Override
    public CompilationDtoOutput getById(Long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new ObjectExistenceException(String
                        .format("Compilation with id=%s was not found", id)));
        CompilationDtoOutput compilationDtoOutput = CompilationMapper.toCompilationDtoOutput(compilation);
        List<Event> events = eventRepository.getByCompilation(id);
        compilationDtoOutput.setEvents(EventMapper.toEventShortDtoOutputList(events));
        return compilationDtoOutput;
    }

    @Override
    public List<CompilationDtoOutput> getByParams(Boolean pinned, Integer from, Integer size) {
        List<CompilationDtoOutput> compilationDtoOutputList = new ArrayList<>();
        Map<Compilation, List<Event>> compilations = getEventsByCompilations(compilationRepository
                .getCompilationByParam(pinned, PageRequest.of(from > 0 ? from / size : 0, size)));
        for (Compilation compilation : compilations.keySet()) {
            compilationDtoOutputList.add(appendEventToCompilation(CompilationMapper
                    .toCompilationDtoOutput(compilation), compilations.get(compilation)));
        }
        return compilationDtoOutputList;
    }

    private Map<Compilation, List<Event>> getEventsByCompilations(List<Compilation> compilations) {
        return eventRepository.getEventByCompilation(compilations)
                .stream()
                .collect(groupingBy(Event::getCompilation, toList()));
    }

    private CompilationDtoOutput appendEventToCompilation(CompilationDtoOutput compilationDtoOutput,
                                                          List<Event> events) {
        compilationDtoOutput.setEvents(EventMapper.toEventShortDtoOutputList(events));
        return compilationDtoOutput;
    }

    private Compilation updateCompilation(Compilation compilation, Compilation newCompilation) {
        if (newCompilation.getTitle() != null && !newCompilation.getTitle().isBlank()) {
            compilation.setTitle(newCompilation.getTitle());
        }
        if (newCompilation.getPinned() != null) {
            compilation.setPinned(newCompilation.getPinned());
        }
        return compilation;
    }
}
