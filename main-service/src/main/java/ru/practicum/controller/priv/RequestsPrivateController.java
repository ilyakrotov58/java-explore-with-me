package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.service.priv.interfaces.IRequestPrivateService;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users")
@Slf4j
@RestController
public class RequestsPrivateController {

    private IRequestPrivateService requestPrivateService;

    @Autowired
    public RequestsPrivateController(IRequestPrivateService requestPrivateService) {
        this.requestPrivateService = requestPrivateService;
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getParticipationRequests(
            @PathVariable int userId) {
        return new ResponseEntity<>(
                requestPrivateService.getParticipationRequests(userId),
                HttpStatus.OK);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> createParticipationRequest(
            @PathVariable int userId,
            @RequestParam int eventId) {
        return new ResponseEntity<>(
                requestPrivateService.createParticipationRequest(
                        userId,
                        eventId),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelParticipationRequest(
            @PathVariable int userId,
            @PathVariable int requestId) {
        return new ResponseEntity<>(
                requestPrivateService.cancelParticipationRequest(
                        userId,
                        requestId),
                HttpStatus.OK);
    }
}
