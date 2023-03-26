package ru.practicum.admin_access.compilations.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin_access.compilation_event.model.CompilationEvent;
import ru.practicum.admin_access.compilation_event.repository.CompilationEventRepository;
import ru.practicum.admin_access.compilations.dto.CompilationDtoInput;
import ru.practicum.admin_access.compilations.dto.CompilationDtoOutput;
import ru.practicum.admin_access.compilations.mapper.CompilationMapper;
import ru.practicum.admin_access.compilations.model.Compilation;
import ru.practicum.admin_access.compilations.repository.CompilationRepository;
import ru.practicum.admin_access.compilations.service.dal.CompilationService;
import ru.practicum.exceptions.exception.ObjectExistenceException;
import ru.practicum.private_access.events.dto.EventShortDtoOutput;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.repository.EventRepository;
import ru.practicum.private_access.events.service.dal.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationEventRepository compilationEventRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Transactional
    @Override
    public CompilationDtoOutput create(CompilationDtoInput compilationDtoInput) {
        Compilation compilation = compilationRepository
                .save(CompilationMapper.toCompilation(compilationDtoInput));
        CompilationDtoOutput compilationDtoOutput = CompilationMapper.toCompilationDtoOutput(compilation);
        if (compilationDtoInput.getEvents() != null && !compilationDtoInput.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(compilationDtoInput.getEvents());
            CompilationEvent compilationEvent = new CompilationEvent();
            List<CompilationEvent> compilationEventList = new ArrayList<>();
            compilationEvent.setCompilation(compilation);
            for (Event event : events) {
                compilationEvent.setEvent(event);
                compilationEventList.add(compilationEvent);
            }
            compilationEventRepository.saveAll(compilationEventList);
            return appendEventToCompilation(compilationDtoOutput, events);
        }
        return appendEventToCompilation(compilationDtoOutput, List.of());
    }

    @Transactional
    @Override
    public CompilationDtoOutput update(Long id, CompilationDtoInput compilationDtoInput) {
        Compilation oldCompilation = compilationRepository.findById(id)
                .orElseThrow(() -> new ObjectExistenceException(String
                        .format("Compilation with id=%s was not found", id)));
        Compilation compilation = updateCompilation(oldCompilation,
                CompilationMapper.toCompilation(compilationDtoInput));
        CompilationDtoOutput compilationDtoOutput = CompilationMapper.toCompilationDtoOutput(compilation);
        if (!compilationDtoInput.getEvents().isEmpty()) {
            List<CompilationEvent> oldCompilationEvent = compilationEventRepository.getByCompilation(id);
            List<Event> newEvents = eventRepository.findAllById(compilationDtoInput.getEvents());
            CompilationEvent compilationEventNew = new CompilationEvent();
            List<CompilationEvent> compilationEventList = new ArrayList<>();
            compilationEventNew.setCompilation(compilation);
            if (!oldCompilationEvent.isEmpty()) {
                for (CompilationEvent compilationEvent : oldCompilationEvent) {
                    compilationEventRepository.deleteByCompilationAndEvent(compilationEvent
                            .getCompilation().getId(), compilationEvent.getEvent().getId());
                }
                for (Event event : newEvents) {
                    compilationEventNew.setEvent(event);
                    compilationEventList.add(compilationEventNew);
                }
                compilationEventRepository.saveAll(compilationEventList);
            } else {
                for (Event event : newEvents) {
                    compilationEventNew.setEvent(event);
                    compilationEventList.add(compilationEventNew);
                }
                compilationEventRepository.saveAll(compilationEventList);
            }
            appendEventToCompilation(compilationDtoOutput, newEvents);
        }
        return compilationDtoOutput;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        getById(id);
        List<CompilationEvent> oldCompilationEvent = compilationEventRepository.getByCompilation(id);
        for (CompilationEvent compilationEvent : oldCompilationEvent) {
            compilationEventRepository.deleteByCompilationAndEvent(compilationEvent
                    .getCompilation().getId(), compilationEvent.getEvent().getId());
        }
        compilationRepository.deleteById(id);
    }

    @Override
    public CompilationDtoOutput getById(Long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new ObjectExistenceException(String
                        .format("Compilation with id=%s was not found", id)));
        return appendEventToCompilation(CompilationMapper.toCompilationDtoOutput(compilation),
                compilationEventRepository.getByCompilation(id)
                        .stream()
                        .map(CompilationEvent::getEvent)
                        .collect(toList()));
    }

    @Override
    public List<CompilationDtoOutput> getByParams(Boolean pinned, Integer from, Integer size) {
        List<CompilationDtoOutput> compilationDtoOutputList = new ArrayList<>();
        Map<Compilation, List<CompilationEvent>> compilationEvents;
        List<Compilation> compilations;
        List<Long> eventIds;
        if (pinned != null) {
            compilationEvents = compilationEventRepository.getAllByCompilationPinned(pinned,
                            PageRequest.of(from > 0 ? from / size : 0, size))
                    .stream()
                    .collect(groupingBy(CompilationEvent::getCompilation));
            eventIds = compilationEventRepository.getAllByCompilationPinned(pinned,
                            PageRequest.of(from > 0 ? from / size : 0, size))
                    .stream()
                    .map(e -> e.getEvent().getId())
                    .collect(toList());
            if (new ArrayList<>(compilationEvents.keySet()).isEmpty()) {
                compilations = compilationRepository.getCompilationByParamWithPinned(pinned,
                        PageRequest.of(from > 0 ? from / size : 0, size));
            } else {
                compilations = compilationRepository.getCompilationByParam(pinned,
                        new ArrayList<>(compilationEvents.keySet()),
                        PageRequest.of(from > 0 ? from / size : 0, size));
            }
        } else {
            compilationEvents = compilationEventRepository.findAll(PageRequest.of(from > 0 ? from / size : 0, size,
                            Sort.by("id")))
                    .stream()
                    .collect(groupingBy(CompilationEvent::getCompilation));
            eventIds = compilationEventRepository.findAll(PageRequest.of(from > 0 ? from / size : 0, size,
                            Sort.by("id")))
                    .stream()
                    .map(e -> e.getEvent().getId())
                    .collect(toList());
            if (new ArrayList<>(compilationEvents.keySet()).isEmpty()) {
                compilations = compilationRepository.findAll(PageRequest.of(from > 0 ? from / size : 0, size)).toList();
            } else {
                compilations = compilationRepository.getCompilationWithoutParam(new ArrayList<>(compilationEvents
                                .keySet()),
                        PageRequest.of(from > 0 ? from / size : 0, size));
            }
        }

        for (Compilation compilation : compilations) {
            CompilationDtoOutput compilationDtoOutput = CompilationMapper.toCompilationDtoOutput(compilation);
            compilationDtoOutput.setEvents(List.of());
            compilationDtoOutputList.add(compilationDtoOutput);
        }
        List<EventShortDtoOutput> eventShortDtoOutputList = eventService.getEventShortDtoOutput(eventRepository
                .findAllById(eventIds));
        for (Compilation compilation : compilationEvents.keySet()) {
            List<Long> ids = compilationEvents.get(compilation)
                    .stream()
                    .map(e -> e.getEvent().getId())
                    .collect(toList());
            List<EventShortDtoOutput> eventShortDtoOutputListByCompilation = eventShortDtoOutputList
                    .stream()
                    .filter(e -> ids.contains(e.getId()))
                    .collect(toList());

            CompilationDtoOutput compilationDtoOutput = CompilationMapper.toCompilationDtoOutput(compilation);
            compilationDtoOutput.setEvents(eventShortDtoOutputListByCompilation);
            compilationDtoOutputList.add(compilationDtoOutput);
        }
        return compilationDtoOutputList;
    }

    private CompilationDtoOutput appendEventToCompilation(CompilationDtoOutput compilationDtoOutput,
                                                          List<Event> events) {
        compilationDtoOutput.setEvents(eventService.getEventShortDtoOutput(events));
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
