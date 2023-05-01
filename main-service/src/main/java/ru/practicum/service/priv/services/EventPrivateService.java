package ru.practicum.service.priv.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.mappers.EventDtoMapper;
import ru.practicum.dto.mappers.ParticipationRequestsDtoMapper;
import ru.practicum.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.dto.requests.UpdateEventUserRequest;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ConflictDataException;
import ru.practicum.exceptions.UserIsNotEventOwnerException;
import ru.practicum.model.Event;
import ru.practicum.model.domain.ActionState;
import ru.practicum.model.domain.EventState;
import ru.practicum.model.Location;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.domain.RequestState;
import ru.practicum.repository.priv.*;
import ru.practicum.service.priv.interfaces.IEventPrivateService;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventPrivateService implements IEventPrivateService {

    final IEventPrivateRepository eventPrivateRepository;
    final IUserPrivateRepository userPrivateRepository;
    final ICategoryPrivateRepository categoryPrivateRepository;
    final IParticipationRequestsPrivateRepository participationRequestsRepository;
    final ILocationPrivateRepository locationPrivateRepository;

    @Autowired
    public EventPrivateService(
            IEventPrivateRepository eventPrivateRepository,
            IUserPrivateRepository userPrivateRepository,
            ICategoryPrivateRepository categoryPrivateRepository,
            IParticipationRequestsPrivateRepository participationRequestsRepository,
            ILocationPrivateRepository locationPrivateRepository) {
        this.eventPrivateRepository = eventPrivateRepository;
        this.userPrivateRepository = userPrivateRepository;
        this.categoryPrivateRepository = categoryPrivateRepository;
        this.participationRequestsRepository = participationRequestsRepository;
        this.locationPrivateRepository = locationPrivateRepository;
    }

    @Override
    public List<EventShortDto> getEventsAddedByCurrentUser(long userId, int from, int size) {
        var events = eventPrivateRepository.getEventsAddedByCurrentUser(userId, from, size);

        return events
                .stream()
                .map(EventDtoMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto addEvent(long userId, NewEventDto newEventDto) {

        var event = EventDtoMapper.fromNewEventDtoToEvent(newEventDto);
        var user = userPrivateRepository.findById(userId);
        var category = categoryPrivateRepository.findById(newEventDto.getCategory());

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        } else {
            event.setInitiator(user.get());
        }

        if (category.isEmpty()) {
            throw new NotFoundException(String.format("Category with id = %s can't be found", newEventDto.getCategory()));
        } else {
            event.setCategory(category.get());
        }

        if (event.getLocation() != null) {
            var savedLocation = locationPrivateRepository.save(event.getLocation());
            event.setLocation(savedLocation);
        }

        if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ConflictDataException("Event date is before current date");
        }

        event.setState(EventState.PENDING);

        var savedEvent = eventPrivateRepository.save(event);

        return EventDtoMapper.toEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto getEventAddedByCurrentUser(long userId, long eventId) {

        var event = eventPrivateRepository.findById(eventId);
        var user = userPrivateRepository.findById(userId);

        if (event.isEmpty()) {
            throw new NotFoundException(String.format("Event with id = %s can't be found", eventId));
        }

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }

        if (event.get().getInitiator().getId() != userId) {
            throw new UserIsNotEventOwnerException(
                    "User with id: " + userId + " is not owner of the event with id: " + eventId);
        }

        return EventDtoMapper.toEventFullDto(event.get());
    }

    @Override
    public EventFullDto updateEventAddedByCurrentUser(long userId, long eventId, UpdateEventUserRequest request) {

        var event = eventPrivateRepository.findById(eventId);
        var user = userPrivateRepository.findById(userId);
        var category = categoryPrivateRepository.findById(request.getCategory());

        if (event.isEmpty()) {
            throw new NotFoundException(String.format("Event with id = %s can't be found", eventId));
        }

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }

        if (request.getAnnotation() != null) {
            event.get().setAnnotation(request.getAnnotation());
        }

        category.ifPresent(value -> event.get().setCategory(value));

        if (request.getDescription() != null) {
            event.get().setDescription(request.getDescription());
        }

        if (request.getEventDate() != null) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime eventDate;

            try {
                eventDate = LocalDateTime.parse(request.getEventDate(), formatter);
            } catch (DateTimeException ex) {
                throw new RuntimeException("DateTime format of start/end date should be: \"yyyy-MM-dd HH:mm:ss\"");
            }

            if (eventDate.isBefore(event.get().getEventDate())) {
                throw new ConflictDataException("Event date in request already passed");
            }

            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new ConflictDataException("Event date is before current date");
            }

            event.get().setEventDate(eventDate);
        }

        if (request.getLocation() != null) {

            var savedLocation = locationPrivateRepository.save(new Location(
                    0,
                    request.getLocation().getLat(),
                    request.getLocation().getLon()));

            event.get().setLocation(savedLocation);
        }

        if (request.getPaid() != null) {
            event.get().setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.get().setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.get().setRequestModeration(request.getRequestModeration());
        }

        if (event.get().getState() == EventState.PUBLISHED) {
            throw new ConflictDataException("Event was published");
        }

        if (request.getStateAction() != null) {
            if (request.getStateAction() == ActionState.CANCEL_REVIEW) {
                event.get().setState(EventState.CANCELED);
            } else if (request.getStateAction() == ActionState.PUBLISH_EVENT) {
                event.get().setState(EventState.PUBLISHED);
            } else if (request.getStateAction() == ActionState.SEND_TO_REVIEW) {
                event.get().setState(EventState.PENDING);
            }
        }

        if (request.getTitle() != null) {
            event.get().setTitle(request.getTitle());
        }

        var savedEvent = eventPrivateRepository.save(event.get());

        return EventDtoMapper.toEventFullDto(savedEvent);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(long userId, long eventId) {

        var user = userPrivateRepository.findById(userId);
        var eventInDb = eventPrivateRepository.findById(eventId);

        var eventsIds = new ArrayList<Long>();

        for (Event event : eventPrivateRepository.getEventsAddedByCurrentUser(userId, 0, 1000)) {
            eventsIds.add(event.getId());
        }

        var requests = participationRequestsRepository.getUserParticipationRequestsByEventId(eventsIds);

        if (eventInDb.isEmpty()) {
            throw new NotFoundException(String.format("Event with id = %s can't be found", eventId));
        }

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }

        var result = new ArrayList<ParticipationRequestDto>();

        for (ParticipationRequest request : requests) {
            request.setEvent(eventInDb.get());

            result.add(ParticipationRequestsDtoMapper.toParticipationRequestDto(request));
        }

        return result;
    }

    @Override
    public EventRequestStatusUpdateResult updateStatusRequest(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest) {

        var event = eventPrivateRepository.findById(eventId);
        var user = userPrivateRepository.findById(userId);

        List<EventRequestStatusUpdateResult.ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<EventRequestStatusUpdateResult.ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (event.isEmpty()) {
            throw new NotFoundException(String.format("Event with id = %s can't be found", eventId));
        }

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }

        for (long id : updateRequest.getRequestIds()) {

            var request = participationRequestsRepository.findById(id);

            if (request.isEmpty()) {
                throw new NotFoundException("Request with id %s not found");
            }

            if (updateRequest.getStatus() == RequestState.REJECTED
                    && request.get().getStatus() == RequestState.CONFIRMED) {
                throw new ConflictDataException("Cant cancel confirmed request");
            }

            request.get().setStatus(updateRequest.getStatus());

            participationRequestsRepository.save(request.get());

            if (request.get().getStatus() == RequestState.CANCELED ||
                    request.get().getStatus() == RequestState.REJECTED) {
                rejectedRequests.add(new EventRequestStatusUpdateResult.ParticipationRequestDto(
                        request.get().getId(),
                        request.get().getEvent().getId(),
                        request.get().getCreated().toString(),
                        request.get().getUser().getId(),
                        request.get().getStatus()
                ));
            } else if (request.get().getStatus() == RequestState.CONFIRMED) {
                confirmedRequests.add(new EventRequestStatusUpdateResult.ParticipationRequestDto(
                        request.get().getId(),
                        request.get().getEvent().getId(),
                        request.get().getCreated().toString(),
                        request.get().getUser().getId(),
                        request.get().getStatus()
                ));

                if (event.get().getConfirmedRequests() == event.get().getParticipantLimit()) {
                    throw new ConflictDataException("ConfirmedRequests == ParticipantLimit");
                } else {
                    event.get().setConfirmedRequests(event.get().getConfirmedRequests() + 1);
                    eventPrivateRepository.save(event.get());
                }
            }
        }

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
