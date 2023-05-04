package ru.practicum.service.priv.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.mappers.ParticipationRequestsDtoMapper;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ConflictDataException;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.domain.EventState;
import ru.practicum.model.domain.RequestState;
import ru.practicum.repository.priv.*;
import ru.practicum.service.priv.interfaces.IRequestPrivateService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RequestPrivateService implements IRequestPrivateService {

    final IParticipationRequestsPrivateRepository participationRequestsRepository;
    final IUserPrivateRepository userPrivateRepository;
    final IEventPrivateRepository eventPrivateRepository;

    @Autowired
    public RequestPrivateService(
            IParticipationRequestsPrivateRepository participationRequestsRepository,
            IUserPrivateRepository userPrivateRepository,
            IEventPrivateRepository eventPrivateRepository) {
        this.participationRequestsRepository = participationRequestsRepository;
        this.userPrivateRepository = userPrivateRepository;
        this.eventPrivateRepository = eventPrivateRepository;
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(long userId) {

        var user = userPrivateRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }

        var requests = participationRequestsRepository.getUserParticipationRequests(userId);

        return requests
                .stream()
                .map(ParticipationRequestsDtoMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createParticipationRequest(long userId, long eventId) {

        var event = eventPrivateRepository.findById(eventId);

        if (event.isEmpty()) {
            throw new NotFoundException(String.format("Event with id = %s can't be found", eventId));
        }

        var user = userPrivateRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }

        if (participationRequestsRepository.findAll()
                .stream()
                .anyMatch(r -> r.getUser().getId() == userId
                        && r.getEvent().getId() == eventId)) {
            throw new ConflictDataException("Request already exists");
        }

        if (event.get().getInitiator().getId() == userId) {
            throw new ConflictDataException("Initiator cant add request");
        }

        if (event.get().getState() != EventState.PUBLISHED) {
            throw new ConflictDataException("Event was not published");
        }

        if (event.get().getParticipantLimit() == event.get().getConfirmedRequests()) {
            throw new ConflictDataException("ParticipantLimit == ConfirmedRequests");
        }

        var request = new ParticipationRequest(
                0,
                event.get(),
                user.get(),
                RequestState.PENDING,
                LocalDateTime.now());

        var requestInDb = participationRequestsRepository.save(request);
        var requestDto = ParticipationRequestsDtoMapper.toParticipationRequestDto(requestInDb);

        requestDto.setEvent(eventId);
        requestDto.setRequester(userId);

        if (!event.get().isRequestModeration()) {
            event.get().setConfirmedRequests(event.get().getConfirmedRequests() + 1);
            eventPrivateRepository.save(event.get());
        }

        return requestDto;
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(long userId, long requestId) {

        var requestInDb = participationRequestsRepository.findById(requestId);

        if (requestInDb.isEmpty()) {
            throw new NotFoundException(String.format("Request with id = %s can't be found", requestId));
        }

        var user = userPrivateRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }

        requestInDb.get().setStatus(RequestState.CANCELED);

        return ParticipationRequestsDtoMapper.toParticipationRequestDto(requestInDb.get());
    }
}
