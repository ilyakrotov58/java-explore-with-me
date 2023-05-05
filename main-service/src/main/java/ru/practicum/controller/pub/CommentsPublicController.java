package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CommentDto;
import ru.practicum.service.pub.interfaces.ICommentsPublicService;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/comments")
@Slf4j
@RestController
public class CommentsPublicController {

    private ICommentsPublicService commentsPublicService;

    @Autowired
    public CommentsPublicController(ICommentsPublicService commentsPublicService) {
        this.commentsPublicService = commentsPublicService;
    }


    @GetMapping("/{eventId}")
    public ResponseEntity<List<CommentDto>> getAllComments(
            @PathVariable long eventId) {
        return new ResponseEntity<>(
                commentsPublicService.getAllComments(eventId),
                HttpStatus.OK);
    }

    @GetMapping("/{eventId}/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable long eventId,
            @PathVariable long commentId) {
        return new ResponseEntity<>(
                commentsPublicService.getCommentById(eventId, commentId),
                HttpStatus.OK);
    }
}
