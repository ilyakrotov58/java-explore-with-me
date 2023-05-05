package ru.practicum.service.priv.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.mappers.CommentDtoMapper;
import ru.practicum.dto.requests.NewCommentRequest;
import ru.practicum.exceptions.ConflictDataException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.priv.ICommentsPrivateRepository;
import ru.practicum.repository.priv.IEventPrivateRepository;
import ru.practicum.repository.priv.IUserPrivateRepository;
import ru.practicum.service.priv.interfaces.ICommentsPrivateService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentPrivateService implements ICommentsPrivateService {

    final ICommentsPrivateRepository commentsPrivateRepository;
    final IEventPrivateRepository eventPrivateRepository;
    final IUserPrivateRepository userPrivateRepository;

    @Autowired
    public CommentPrivateService(
            ICommentsPrivateRepository commentsPrivateRepository,
            IEventPrivateRepository eventPrivateRepository,
            IUserPrivateRepository userPrivateRepository) {
        this.commentsPrivateRepository = commentsPrivateRepository;
        this.eventPrivateRepository = eventPrivateRepository;
        this.userPrivateRepository = userPrivateRepository;
    }

    @Override
    public List<CommentDto> getAllComments(long eventId) {

        checkEventId(eventId);
        return commentsPrivateRepository.getAllComments(eventId)
                .stream()
                .map(CommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getAllUserComments(long eventId, long userId) {

        checkEventId(eventId);
        checkUserId(userId);

        return commentsPrivateRepository.getAllUserComments(eventId, userId)
                .stream()
                .map(CommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(NewCommentRequest newCommentRequest, long eventId, long userId) {

        var user = checkUserId(userId);
        var event = checkEventId(eventId);

        var comment = CommentDtoMapper.fromNewCommentRequestToComment(newCommentRequest);
        comment.setUser(user);
        comment.setEvent(event);

        comment = commentsPrivateRepository.save(comment);
        var commentDto = CommentDtoMapper.toDto(comment);
        commentDto.setId(comment.getId());

        return commentDto;
    }

    @Override
    public CommentDto updateComment(
            NewCommentRequest newCommentRequest,
            long eventId,
            long userId,
            long commentId) {

        var event = checkEventId(eventId);
        var user = checkUserId(userId);

        if (eventPrivateRepository.findById(commentId).isEmpty()) {
            throw new NotFoundException(
                    String.format("Comment with id %s can't be found", commentId));
        }

        var comment = CommentDtoMapper.fromNewCommentRequestToComment(newCommentRequest);
        comment.setEvent(event);
        comment.setUser(user);
        comment.setId(commentId);

        var commentDto = CommentDtoMapper.toDto(commentsPrivateRepository.save(comment));
        commentDto.setId(commentId);
        return commentDto;
    }

    @Override
    public void deleteComment(long userId, long commentId) {

        var comment = commentsPrivateRepository.findById(commentId);

        checkUserId(userId);

        if (comment.isEmpty()) {
            throw new NotFoundException(
                    String.format("Comment with id %s can't be found", commentId));
        }

        if (comment.get().getUser().getId() != userId) {
            throw new ConflictDataException("User can't delete not his own comment");
        }

        commentsPrivateRepository.deleteById(commentId);
    }

    private User checkUserId(long userId) {

        var user = userPrivateRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(String.format(
                    "User with id %s can't be found",
                    userId));
        }

        return user.get();
    }

    private Event checkEventId(long eventId) {

        var event = eventPrivateRepository.findById(eventId);

        if (event.isEmpty()) {
            throw new NotFoundException(String.format(
                    "Event with id %s can't be found",
                    eventId));
        }

        return event.get();
    }
}
