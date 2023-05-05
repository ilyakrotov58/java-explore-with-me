package ru.practicum.dto.mappers;

import ru.practicum.dto.CommentDto;
import ru.practicum.dto.requests.NewCommentRequest;
import ru.practicum.model.Comment;

public class CommentDtoMapper {

    public static Comment fromNewCommentRequestToComment(NewCommentRequest newCommentRequest) {
        return new Comment(
                0,
                null,
                null,
                newCommentRequest.getText()
        );
    }

    public static CommentDto toDto(Comment comment) {

        var event = comment.getEvent();

        return new CommentDto(
                comment.getId(),
                new CommentDto.EventShortDto(
                        event.getId(),
                        event.getAnnotation(),
                        new CommentDto.EventShortDto.CategoryDto(
                                event.getCategory().getId(),
                                event.getCategory().getName()
                        ),
                        event.getEventDate().toString(),
                        new CommentDto.EventShortDto.UserShortDto(
                                comment.getUser().getId(),
                                comment.getUser().getName()
                        ),
                        event.isPaid(),
                        event.getTitle(),
                        event.getHits()
                ),
                comment.getUser(),
                comment.getText()
        );
    }

    public static Comment fromDto(CommentDto comment) {

        return new Comment(
                comment.getId(),
                null,
                comment.getUser(),
                comment.getText()
        );
    }
}
