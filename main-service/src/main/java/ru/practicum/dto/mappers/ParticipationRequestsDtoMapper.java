package ru.practicum.dto.mappers;

import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.model.ParticipationRequest;

public class ParticipationRequestsDtoMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {

        long eventId = 0;
        long requesterId = 0;

        if (request.getEvent() != null) {
            eventId = request.getEvent().getId();
        }

        if (request.getUser() != null) {
            requesterId = request.getUser().getId();
        }

        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                eventId,
                requesterId,
                request.getStatus()
        );
    }
}
