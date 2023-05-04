package ru.practicum.service.priv.interfaces;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.requests.ParticipationRequestDto;

import java.util.List;

public interface IRequestPrivateService {

    List<ParticipationRequestDto> getParticipationRequests(
            @PathVariable long userId);

    ParticipationRequestDto createParticipationRequest(
            @PathVariable long userId,
            @RequestParam long eventId);

    ParticipationRequestDto cancelParticipationRequest(
            @PathVariable long userId,
            @PathVariable long requestId);
}
