package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.requests.NewCommentRequest;
import ru.practicum.service.priv.interfaces.ICommentsPrivateService;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping("/users/comments")
@Slf4j
@RestController
public class CommentsPrivateController {

    private ICommentsPrivateService commentsPrivateService;

    @Autowired
    public CommentsPrivateController(ICommentsPrivateService commentsPrivateService) {
        this.commentsPrivateService = commentsPrivateService;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<CommentDto>> getAllComments(
            @PathVariable long eventId) {
        return new ResponseEntity<>(
                commentsPrivateService.getAllComments(eventId),
                HttpStatus.OK);
    }

    @GetMapping("/{eventId}/{userId}")
    public ResponseEntity<List<CommentDto>> getAllUserComments(
            @PathVariable long eventId,
            @PathVariable long userId) {
        return new ResponseEntity<>(commentsPrivateService.getAllUserComments(
                eventId, userId), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/{userId}")
    public ResponseEntity<CommentDto> addComment(
            @RequestBody NewCommentRequest newCommentRequest,
            @PathVariable long eventId,
            @PathVariable long userId) {
        return new ResponseEntity<>(commentsPrivateService.addComment(
                newCommentRequest, eventId, userId), HttpStatus.OK);
    }


    @PatchMapping("/{eventId}/{userId}/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @RequestBody NewCommentRequest newCommentRequest,
            @PathVariable long eventId,
            @PathVariable long userId,
            @PathVariable long commentId) {
        return new ResponseEntity<>(commentsPrivateService.updateComment(
                newCommentRequest, eventId, userId, commentId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/{commentId}")
    public ResponseEntity<HttpStatus> deleteComment(
            @PathVariable long userId,
            @PathVariable long commentId) {

        commentsPrivateService.deleteComment(userId, commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
