package ru.practicum.service.priv.interfaces;

import ru.practicum.dto.CommentDto;
import ru.practicum.dto.requests.NewCommentRequest;

import java.util.List;

public interface ICommentsPrivateService {

    List<CommentDto> getAllComments(long eventId);

    List<CommentDto> getAllUserComments(long eventId, long userId);

    CommentDto addComment(NewCommentRequest newCommentRequest, long eventId, long userId);

    CommentDto updateComment(
            NewCommentRequest newCommentRequest,
            long eventId,
            long userId,
            long commentId);

    void deleteComment(long userId, long commentId);
}
