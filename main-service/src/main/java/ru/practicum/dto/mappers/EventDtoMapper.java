package ru.practicum.dto.mappers;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.model.Event;
import ru.practicum.model.domain.EventState;
import ru.practicum.model.Location;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDtoMapper {

    public static CompilationDto.EventShortDto toCompilationDtoEventShortDto(
            Event event) {
        return new CompilationDto.EventShortDto(
                event.getId(),
                event.getAnnotation(),
                new CompilationDto.EventShortDto.CategoryDto(
                        event.getCategory().getId(),
                        event.getCategory().getName()),
                event.getConfirmedRequests(),
                event.getEventDate().toString(),
                new CompilationDto.EventShortDto.UserShortDto(
                        event.getInitiator().getId(),
                        event.getInitiator().getName()),
                event.isPaid(),
                event.getTitle(),
                event.getHits());
    }

    public static EventShortDto toEventShortDto(Event event) {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        var eventDate = event.getEventDate().format(formatter);

        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                new EventShortDto.CategoryDto(
                        event.getCategory().getId(),
                        event.getCategory().getName()
                ),
                event.getConfirmedRequests(),
                eventDate,
                new EventShortDto.UserShortDto(
                        event.getInitiator().getId(),
                        event.getInitiator().getName()
                ),
                event.isPaid(),
                event.getTitle(),
                event.getHits()
        );
    }

    public static EventFullDto toEventFullDto(Event event) {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        var eventDate = event.getEventDate().format(formatter);

        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                event.getDescription(),
                new EventFullDto.CategoryDto(
                        event.getCategory().getId(),
                        event.getCategory().getName()
                ),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                new EventFullDto.UserShortDto(
                        event.getInitiator().getId(),
                        event.getInitiator().getName()
                ),
                new EventFullDto.Location(
                        event.getLocation().getLat(),
                        event.getLocation().getLon()
                ),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.isRequestModeration(),
                event.getState(),
                eventDate,
                event.getTitle(),
                event.getHits()
        );
    }

    public static Event fromNewEventDtoToEvent(NewEventDto newEventDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime eventDate;

        try {
            eventDate = LocalDateTime.parse(newEventDto.getEventDate(), formatter);
        } catch (DateTimeException ex) {
            throw new RuntimeException("DateTime format of start/end date should be: \"yyyy-MM-dd HH:mm:ss\"");
        }

        return new Event(
                0,
                newEventDto.getAnnotation(),
                0,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                eventDate,
                newEventDto.isPaid(),
                newEventDto.getParticipantLimit(),
                LocalDateTime.now(),
                newEventDto.isRequestModeration(),
                EventState.PENDING,
                newEventDto.getTitle(),
                0,
                null,
                null,
                new Location(
                        newEventDto.getLocation().getId(),
                        newEventDto.getLocation().getLat(),
                        newEventDto.getLocation().getLon()));
    }
}