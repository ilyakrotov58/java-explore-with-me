package ru.practicum.service.pub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.mappers.EventDtoMapper;
import ru.practicum.model.Event;
import ru.practicum.model.domain.EventState;
import ru.practicum.model.domain.SortType;
import ru.practicum.repository.pub.IEventPublicRepository;
import ru.practicum.service.pub.interfaces.IEventsPublicService;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventsPublicService implements IEventsPublicService {

    final
    IEventPublicRepository eventPublicRepository;

    @Autowired
    public EventsPublicService(IEventPublicRepository eventPublicRepository) {
        this.eventPublicRepository = eventPublicRepository;
    }

    @Override
    public List<EventShortDto> getEventList(String text, int[] categories, Boolean paid, String rangeStart, String rangeEnd, boolean onlyAvailable, String sort, int from, int size) {

        LocalDateTime rangeStartParsed = null;
        LocalDateTime rangeEndParsed = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (rangeEnd != null) {
            try {
                rangeEndParsed = LocalDateTime.parse(rangeEnd, formatter);
            } catch (DateTimeException ex) {
                throw new RuntimeException("DateTime format start/end date should be: \"yyyy-MM-dd HH:mm:ss\"");
            }
        }

        if (rangeStart != null) {
            try {
                rangeStartParsed = LocalDateTime.parse(rangeStart, formatter);
            } catch (DateTimeException ex) {
                throw new RuntimeException("DateTime format of start/end date should be: \"yyyy-MM-dd HH:mm:ss\"");
            }
        }

        var filteredEvents = eventPublicRepository.getEvents(from, size)
                .stream()
                .filter(e -> e.getState() == EventState.PUBLISHED)
                .filter(e -> e.getParticipantLimit() > e.getConfirmedRequests())
                .collect(Collectors.toList());

        if (text != null) {
            filteredEvents = filteredEvents
                    .stream()
                    .filter(e -> e.getDescription().toLowerCase().contains(text.toLowerCase())
                            || e.getAnnotation().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (categories != null) {
            filteredEvents = filteredEvents
                    .stream()
                    .filter(e -> Arrays.stream(categories).anyMatch(c -> c == e.getCategory().getId()))
                    .collect(Collectors.toList());
        }

        if (onlyAvailable) {
            filteredEvents = filteredEvents
                    .stream()
                    .filter(e -> e.getConfirmedRequests() < e.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        if (paid != null) {
            if (paid) {
                filteredEvents = filteredEvents
                        .stream()
                        .filter(Event::isPaid)
                        .collect(Collectors.toList());
            } else {
                filteredEvents = filteredEvents
                        .stream()
                        .filter(e -> !e.isPaid())
                        .collect(Collectors.toList());
            }
        }

        if (rangeEnd != null && rangeStart != null) {

            LocalDateTime finalRangeStartParsed = rangeStartParsed;
            LocalDateTime finalRangeEndParsed = rangeEndParsed;
            filteredEvents = filteredEvents
                    .stream()
                    .filter(e -> e.getEventDate().isAfter(finalRangeStartParsed)
                            && e.getEventDate().isBefore(finalRangeEndParsed))
                    .collect(Collectors.toList());
        } else if (rangeStart != null) {

            LocalDateTime finalRangeStartParsed1 = rangeStartParsed;
            filteredEvents = filteredEvents
                    .stream()
                    .filter(e -> e.getEventDate().isAfter(finalRangeStartParsed1))
                    .collect(Collectors.toList());
        } else if (rangeEnd != null) {

            LocalDateTime finalRangeEndParsed1 = rangeEndParsed;
            filteredEvents = filteredEvents
                    .stream()
                    .filter(e -> e.getEventDate().isBefore(finalRangeEndParsed1))
                    .collect(Collectors.toList());
        }

        if (sort != null) {
            if (sort.equals(SortType.EVENT_DATE.toString())) {
                filteredEvents = filteredEvents
                        .stream()
                        .sorted((o1, o2) -> {
                            if (o1.getEventDate().isBefore(o2.getEventDate())) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }).collect(Collectors.toList());
            } else if (sort.equals(SortType.VIEWS.toString())) {
                filteredEvents = filteredEvents
                        .stream()
                        .sorted((o1, o2) -> {
                            if (o1.getHits() > (o2.getHits())) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }).collect(Collectors.toList());
            }
        }

        var eventsShortDto = new ArrayList<EventShortDto>();

        for (Event event : filteredEvents) {
            eventsShortDto.add(EventDtoMapper.toEventShortDto(event));
        }

        return eventsShortDto;
    }

    @Override
    public EventFullDto getEvent(long id) {
        var event = eventPublicRepository.getReferenceById(id);
        event.setHits(event.getHits() + 1);

        return EventDtoMapper.toEventFullDto(event);
    }
}
