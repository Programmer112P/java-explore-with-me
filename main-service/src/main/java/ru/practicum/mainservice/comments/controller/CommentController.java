package ru.practicum.mainservice.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comments.dto.CommentDto;
import ru.practicum.mainservice.comments.dto.CommentUpdateDto;
import ru.practicum.mainservice.comments.dto.NewCommentDto;
import ru.practicum.mainservice.comments.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comments")
@Slf4j
@Validated
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("CommentController createComment: request to create comment");
        CommentDto response = commentService.createComment(newCommentDto);
        log.info("CommentController createComment: completed request to create comment");
        return response;
    }

    @PatchMapping("/{userId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        log.info("CommentController updateComment: request to update comment with id {}", commentUpdateDto.getId());
        CommentDto response = commentService.updateComment(commentUpdateDto, userId);
        log.info("CommentController updateComment: completed request to update comment with id {}", commentUpdateDto.getId());
        return response;
    }

    @DeleteMapping("/{userId}/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.info("CommentController deleteComment: request to delete comment with id {}", commentId);
        commentService.deleteComment(userId, commentId);
        log.info("CommentController deleteComment: completed request to delete comment with id {}", commentId);
    }

    @GetMapping("/{eventId}")
    public List<CommentDto> getEventComments(@PathVariable Long eventId,
                                             @RequestParam(name = "from", required = false, defaultValue = "0") Long offset,
                                             @RequestParam(name = "size", required = false, defaultValue = "10") Integer limit) {
        log.info("CommentController getEventComments: request to get event {} comments", eventId);
        List<CommentDto> response = commentService.getEventComments(eventId, offset, limit);
        log.info("CommentController getEventComments: completed request to get event {} comments", eventId);
        return response;
    }
}
