package ru.practicum.service.pub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.mappers.CommentDtoMapper;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.repository.pub.ICommentsPublicRepository;
import ru.practicum.repository.pub.IEventPublicRepository;
import ru.practicum.service.pub.interfaces.ICommentsPublicService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentsPublicService implements ICommentsPublicService {

    final ICommentsPublicRepository commentsPublicRepository;
    final IEventPublicRepository eventPublicRepository;

    @Autowired
    public CommentsPublicService(
            ICommentsPublicRepository commentsPublicRepository,
            IEventPublicRepository eventPublicRepository) {
        this.commentsPublicRepository = commentsPublicRepository;
        this.eventPublicRepository = eventPublicRepository;
    }

    @Override
    public List<CommentDto> getAllComments(long eventId) {

        checkEvent(eventId);

        return commentsPublicRepository.getAllComments(eventId)
                .stream()
                .map(CommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long eventId, long commentId) {

        checkEvent(eventId);

        var comment = commentsPublicRepository.getCommentById(commentId);

        if (comment == null) {
            throw new NotFoundException(String.format("Comment with id %s cant be found", commentId));
        }

        return CommentDtoMapper.toDto(comment);
    }

    private void checkEvent(long eventId) {
        if (eventPublicRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException(String.format("Event with id %s can't be found", eventId));
        }
    }
}
