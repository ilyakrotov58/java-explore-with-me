package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.dto.requests.UpdateEventUserRequest;
import ru.practicum.service.priv.interfaces.IEventPrivateService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users")
@Slf4j
@RestController
public class EventsPrivateController {

    private IEventPrivateService eventPrivateService;

    @Autowired
    public EventsPrivateController(IEventPrivateService eventPrivateService) {
        this.eventPrivateService = eventPrivateService;
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsAddedByCurrentUser(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return eventPrivateService.getEventsAddedByCurrentUser(
                userId,
                from,
                size);
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> addEvent(
            @PathVariable int userId,
            @Valid
            @RequestBody NewEventDto newEventDto) {
        return new ResponseEntity<>(eventPrivateService.addEvent(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventAddedByCurrentUser(
            @PathVariable int userId,
            @PathVariable int eventId) {
        return eventPrivateService.getEventAddedByCurrentUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventAddedByCurrentUser(
            @PathVariable int userId,
            @PathVariable int eventId,
            @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventPrivateService.updateEventAddedByCurrentUser(
                userId,
                eventId,
                updateEventUserRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(
            @PathVariable int userId,
            @PathVariable int eventId) {
        return eventPrivateService.getParticipationRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequest(
            @PathVariable int userId,
            @PathVariable int eventId,
            @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return eventPrivateService.updateStatusRequest(
                userId,
                eventId,
                updateRequest);
    }
}
