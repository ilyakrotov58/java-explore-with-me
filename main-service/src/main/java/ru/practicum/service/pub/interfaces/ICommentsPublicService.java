package ru.practicum.service.pub.interfaces;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.dto.CommentDto;

import java.util.List;

public interface ICommentsPublicService {

    List<CommentDto> getAllComments(@PathVariable long eventId);

    CommentDto getCommentById(
            @PathVariable long eventId,
            @PathVariable long commentId);
}
