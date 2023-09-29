package ru.practicum.mainservice.comments.service;

import ru.practicum.mainservice.comments.dto.CommentDto;
import ru.practicum.mainservice.comments.dto.CommentUpdateDto;
import ru.practicum.mainservice.comments.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(NewCommentDto newCommentDto);

    CommentDto updateComment(CommentUpdateDto commentUpdateDto, Long userId);

    void deleteComment(Long userId, Long commentId);

    List<CommentDto> getEventComments(Long eventId, Long offset, Integer limit);
}
