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
    public ResponseEntity<List<EventShortDto>> getEventsAddedByCurrentUser(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                eventPrivateService.getEventsAddedByCurrentUser(
                        userId,
                        from,
                        size),
                HttpStatus.OK);
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> addEvent(
            @PathVariable int userId,
            @Valid @RequestBody NewEventDto newEventDto) {
        return new ResponseEntity<>(
                eventPrivateService.addEvent(userId, newEventDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventAddedByCurrentUser(
            @PathVariable int userId,
            @PathVariable int eventId) {
        return new ResponseEntity<>(
                eventPrivateService.getEventAddedByCurrentUser(userId, eventId),
                HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventAddedByCurrentUser(
            @PathVariable int userId,
            @PathVariable int eventId,
            @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return new ResponseEntity<>(
                eventPrivateService.updateEventAddedByCurrentUser(
                        userId,
                        eventId,
                        updateEventUserRequest),
                HttpStatus.OK);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getParticipationRequests(
            @PathVariable int userId,
            @PathVariable int eventId) {
        return new ResponseEntity<>(
                eventPrivateService.getParticipationRequests(userId, eventId),
                HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateStatusRequest(
            @PathVariable int userId,
            @PathVariable int eventId,
            @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return new ResponseEntity<>(
                eventPrivateService.updateStatusRequest(
                        userId,
                        eventId,
                        updateRequest),
                HttpStatus.OK);
    }
}
