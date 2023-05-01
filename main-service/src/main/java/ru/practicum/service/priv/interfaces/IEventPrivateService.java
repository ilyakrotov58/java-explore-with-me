package ru.practicum.service.priv.interfaces;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.dto.requests.UpdateEventUserRequest;

import java.util.List;

public interface IEventPrivateService {

    List<EventShortDto> getEventsAddedByCurrentUser(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size);

    EventFullDto addEvent(
            @PathVariable long userId,
            @RequestBody NewEventDto newEventDto);

    EventFullDto getEventAddedByCurrentUser(
            @PathVariable long userId,
            @PathVariable long eventId);

    EventFullDto updateEventAddedByCurrentUser(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getParticipationRequests(
            @PathVariable long userId,
            @PathVariable long eventId);

    EventRequestStatusUpdateResult updateStatusRequest(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody EventRequestStatusUpdateRequest updateRequest);
}
