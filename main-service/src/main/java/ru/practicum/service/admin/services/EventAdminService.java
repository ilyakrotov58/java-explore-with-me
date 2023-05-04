package ru.practicum.service.admin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.mappers.EventDtoMapper;
import ru.practicum.dto.requests.UpdateEventAdminRequest;
import ru.practicum.exceptions.ConflictDataException;
import ru.practicum.model.Event;
import ru.practicum.model.domain.ActionState;
import ru.practicum.model.domain.EventState;
import ru.practicum.model.Location;
import ru.practicum.repository.admin.ICategoryAdminRepository;
import ru.practicum.repository.admin.IEventAdminRepository;
import ru.practicum.repository.admin.ILocationAdminRepository;
import ru.practicum.service.admin.interfaces.IEventAdminService;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventAdminService implements IEventAdminService {

    private final IEventAdminRepository eventAdminRepository;
    private final ICategoryAdminRepository categoryAdminRepository;
    private final ILocationAdminRepository locationAdminRepository;

    @Autowired
    public EventAdminService(
            IEventAdminRepository eventAdminRepository,
            ICategoryAdminRepository categoryAdminRepository,
            ILocationAdminRepository locationAdminRepository) {
        this.eventAdminRepository = eventAdminRepository;
        this.categoryAdminRepository = categoryAdminRepository;
        this.locationAdminRepository = locationAdminRepository;
    }

    @Override
    public List<EventFullDto> getEventsList(
            long[] users,
            String[] states,
            long[] categories,
            String rangeStart,
            String rangeEnd,
            int from,
            int size) {

        LocalDateTime rangeStartParsed = null;
        LocalDateTime rangeEndParsed = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Event> eventsWithFilterByDate;

        var eventsWithoutDateFilter = eventAdminRepository.findAll();

        if (users != null) {
            eventsWithoutDateFilter = eventsWithoutDateFilter
                    .stream()
                    .filter(e -> Arrays.stream(users).anyMatch(u -> u == e.getInitiator().getId()))
                    .collect(Collectors.toList());
        }

        if (states != null) {
            eventsWithoutDateFilter = eventsWithoutDateFilter
                    .stream()
                    .filter(e -> Arrays.stream(states).anyMatch(s -> s.equals(e.getState().toString())))
                    .collect(Collectors.toList());
        }

        if (categories != null) {
            eventsWithoutDateFilter = eventsWithoutDateFilter
                    .stream()
                    .filter(e -> Arrays.stream(categories).anyMatch(c -> c == (e.getCategory().getId())))
                    .collect(Collectors.toList());
        }

        if (rangeEnd != null) {
            try {
                rangeEndParsed = LocalDateTime.parse(rangeEnd, formatter);
            } catch (DateTimeException ex) {
                throw new RuntimeException("DateTime format of start/end date should be: \"yyyy-MM-dd HH:mm:ss\"");
            }
        }

        if (rangeStart != null) {
            try {
                rangeStartParsed = LocalDateTime.parse(rangeStart, formatter);
            } catch (DateTimeException ex) {
                throw new RuntimeException("DateTime format of start/end date should be: \"yyyy-MM-dd HH:mm:ss\"");
            }
        }

        if (rangeStartParsed != null && rangeEndParsed != null) {

            LocalDateTime finalRangeStartParsed = rangeStartParsed;
            LocalDateTime finalRangeEndParsed = rangeEndParsed;

            eventsWithFilterByDate = eventsWithoutDateFilter
                    .stream()
                    .filter(e -> e.getEventDate().isAfter(finalRangeStartParsed)
                            && e.getEventDate().isBefore(finalRangeEndParsed))
                    .collect(Collectors.toList());
        } else if (rangeStart != null) {

            LocalDateTime finalRangeStartParsed1 = rangeStartParsed;
            eventsWithFilterByDate = eventsWithoutDateFilter
                    .stream()
                    .filter(e -> e.getEventDate().isAfter(finalRangeStartParsed1))
                    .collect(Collectors.toList());
        } else if (rangeEnd != null) {

            LocalDateTime finalRangeEndParsed1 = rangeEndParsed;
            eventsWithFilterByDate = eventsWithoutDateFilter
                    .stream()
                    .filter(e -> e.getEventDate().isBefore(finalRangeEndParsed1))
                    .collect(Collectors.toList());
        } else {
            eventsWithFilterByDate = eventsWithoutDateFilter;
        }

        return eventsWithFilterByDate
                .stream()
                .map(EventDtoMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest request) {

        var event = eventAdminRepository.getReferenceById(eventId);

        Location location = null;

        if (request.getLocation() != null) {
            location = locationAdminRepository
                    .save(new Location(0, request.getLocation().getLat(), request.getLocation().getLon()));
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getCategory() != null) {
            var category = categoryAdminRepository.getReferenceById(request.getCategory());
            event.setCategory(category);
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getEventDate() != null) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime eventDate;

            try {
                eventDate = LocalDateTime.parse(request.getEventDate(), formatter);
            } catch (DateTimeException ex) {
                throw new RuntimeException("DateTime format of start/end date should be: \"yyyy-MM-dd HH:mm:ss\"");
            }

            if (eventDate.isBefore(event.getEventDate())) {
                throw new ConflictDataException("Event date at request already passed");
            }

            event.setEventDate(eventDate);
        }

        if (request.getLocation() != null) {
            event.setLocation(location);
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getStateAction() != null) {
            if (request.getStateAction() == ActionState.PUBLISH_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictDataException("Event already published");
                }

                if (event.getState() == EventState.CANCELED) {
                    throw new ConflictDataException("Cant publish canceled or rejected event");
                }
                event.setState(EventState.PUBLISHED);
            } else if (request.getStateAction() == ActionState.REJECT_EVENT) {

                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictDataException("Cant reject published event");
                }
                event.setState(EventState.CANCELED);
            }
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        var updatedEvent = eventAdminRepository.save(event);

        return EventDtoMapper.toEventFullDto(updatedEvent);
    }
}
